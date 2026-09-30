import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.*;

/**
 * A utility class for loading, transforming, and analyzing CSV event data.
 * It answers four questions:
 * 1. Number of events per month
 * 2. Busiest month
 * 3. Indoor vs outdoor sport events
 * 4. Sport events occurring during exam months
 * 5. % of music events are indoor vs. outdoor
 */
public class FileHandler {

    /**
     * Main entry point. Loads the CSV, computes all statistics, and prints results.
     */
    public static void main(String[] args) throws IOException {

        List<String[]> rows = loadCSV("FileHandler\\dataexcel.csv", ";");

        String[] headers = rows.get(0);

        int sportIndex = findColumn(headers, "Sport");
        int locationIndex = findColumn(headers, "Indoor/Outdoor");
        int monthIndex = findColumn(headers, "Start month");

        Map<Integer, Long> eventsPerMonth = countEventsPerMonth(rows, monthIndex);
        long indoorEvents = countEvents(rows, sportIndex, locationIndex, "indoor");
        long outdoorEvents = countEvents(rows, sportIndex, locationIndex, "outdoor");

        System.out.println("Busiest month: " + busiestMonthSummary(eventsPerMonth));
        printIndoorOutdoorStats(indoorEvents, outdoorEvents);
        printEventsPerMonth(eventsPerMonth);
        printMusicPercentages(rows);

        System.out.println("\nSport events during exam period: " + sportEventsExamPeriod(rows));

        List<Long> counts = FileHandler.getEventsPerMonthList(eventsPerMonth);
        EventChart chart =  EventChart.fromEvents(counts, Arrays.stream(Month.values())
                                .map(Enum::toString).collect(Collectors.toList()));

        EventChart chart1 = EventChart.fromEvents(List.of(indoorEvents, outdoorEvents), List.of("Indoor", "Outdoor"));
        JFrame frame1 = new JFrame("Events per Month");
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame frame2 = new JFrame("Events per Month");
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame1.add(chart);
        frame2.add(chart1);

        frame1.setSize(900, 600);
        frame1.setVisible(true);

        frame2.setSize(900, 600);

        frame2.setVisible(true);

    }

    /**
     * Loads a CSV file into a list of String arrays, each representing a row.
     *
     * @param fileName  the CSV file path
     * @param delimiter the delimiter used in the CSV
     * @return a list of rows, each row represented as a String[]
     */
    public static List<String[]> loadCSV(String fileName, String delimiter){
        try (Stream<String> lines = Files.lines(Path.of(fileName))) {
            return lines
                    .map(line -> line.split(delimiter))
                    .collect(Collectors.toList());
        } catch (IOException ioexception) {
            System.out.println("Error reading file " + fileName);
            return Collections.emptyList();
        }
    }

    /**
     * Converts a row-wise CSV structure into a column-wise structure.
     *
     * @param rows list of rows (String[])
     * @return list of columns, each column represented as a List<String>
     */
    public static List<List<String>> toColumnWise(List<String[]> rows) {
        if (rows.isEmpty()) return List.of();

        int columnCount = rows.get(0).length;

        return IntStream.range(0, columnCount)
                .mapToObj(col ->
                        rows.stream()
                                .map(row -> row[col])
                                .toList()
                )
                .toList();
    }

    /**
     * Finds the index of a column by its header name.
     *
     * @param headers array of header names
     * @param name    the column name to search for
     * @return index of the column
     * @throws IllegalArgumentException if the column is not found
     */
    public static int findColumn(String[] headers, String name) {
        return IntStream.range(0, headers.length)
                .filter(i -> headers[i].replace("\"", "").trim().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Column not found: " + name));
    }

    /**
     * Safely parses an integer from a row at a given index.
     *
     * @param row   the row array
     * @param index the index to parse
     * @return parsed integer or -1 if invalid
     */
    private static int safeParseInt(String[] row, int index) {
        try {
            return Integer.parseInt(row[index].replace("\"", "").trim());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Retrieves a column by its header name from a column-wise structure.
     *
     * @param calendar column-wise CSV structure
     * @param header   the column header to search for
     * @return the column as a List<String>, or empty list if not found
     */
    public static List<String> getColumnByHeader(List<List<String>> calendar, String header) {
        return calendar.stream()
                .filter(column -> column.get(0).equalsIgnoreCase(header))
                .findFirst()
                .orElse(List.of());
    }

    //__________________________________________________________________
    //______________________QUESTION 1 _________________________________

    /**
     * Counts sport events occurring in a specific month, considering wrap-around ranges.
     *
     * @param calendar column-wise CSV structure
     * @param month    month number as a string
     * @return number of sport events in that month
     */
    public static int numberOfSportEventsMonth(List<List<String>> calendar, String month) {

        List<String> sport = getColumnByHeader(calendar, "Sport");
        List<String> startMonth = getColumnByHeader(calendar, "Start month");
        List<String> endMonth = getColumnByHeader(calendar, "End month");

        int m = Integer.parseInt(month);

        return (int) IntStream.range(1, sport.size())
                .filter(i -> {
                    int s = Integer.parseInt(startMonth.get(i));
                    int e = Integer.parseInt(endMonth.get(i));
                    return (s <= e) ? (s <= m && m <= e) : (m >= s || m <= e);
                })
                .filter(i -> sport.get(i).equalsIgnoreCase("yes"))
                .count();
    }

    /**
     * Counts events per month using the "Start month" column.
     *
     * @param rows       row-wise CSV structure
     * @param monthIndex index of the "Start month" column
     * @return map of month → number of events
     */
    public static Map<Integer, Long> countEventsPerMonth(List<String[]> rows, int monthIndex) {
        return rows.stream()
                .skip(1)
                .map(row -> safeParseInt(row, monthIndex))
                .filter(month -> month >= 1 && month <= 12)
                .collect(Collectors.groupingBy(
                        month -> month,
                        Collectors.counting()
                ));
    }

    /**
     * Returns a list of event counts for each month (1–12).
     *
     * @param eventsPerMonth map of month → number of events
     * @return list of 12 values, each representing the number of events for that month
     */
    public static List<Long> getEventsPerMonthList(Map<Integer, Long> eventsPerMonth) {
        return IntStream.rangeClosed(1, 12)
                .mapToObj(month -> eventsPerMonth.getOrDefault(month, 0L))
                .toList();
    }


    //__________________________________________________________________
    //______________________QUESTION 2 _________________________________
    /**
     * Prints the busiest month based on event counts.
     *
     * @param eventsPerMonth map of month → number of events
     */
    public static String busiestMonthSummary(Map<Integer, Long> eventsPerMonth) {
        return eventsPerMonth.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> monthName(entry.getKey()) + " " +
                        "(" + entry.getValue() + " events)")
                .orElse("No data");
    }

    //__________________________________________________________________
    //______________________QUESTION 3 _________________________________
    /**
     * Counts sport events that match a given indoor/outdoor type.
     *
     * @param rows          row-wise CSV structure
     * @param sportIndex    index of the "Sport" column
     * @param locationIndex index of the "Indoor/Outdoor" column
     * @param type          "indoor" or "outdoor"
     * @return number of matching events
     */
    public static long countEvents(List<String[]> rows, int sportIndex, int locationIndex, String type) {
        return rows.stream()
                .skip(1)
                .filter(row -> row.length > Math.max(sportIndex, locationIndex))
                .filter(row -> row[sportIndex].replace("\"", "").trim().equalsIgnoreCase("yes"))
                .filter(row -> row[locationIndex].replace("\"", "").trim().equalsIgnoreCase(type))
                .count();
    }

    //__________________________________________________________________
    //______________________QUESTION 4 _________________________________

    /**
     * Computes the total number of sport events occurring during exam months:
     * March, May, October, December.
     *
     * @param rows row-wise CSV structure
     * @return total number of sport events during exam months
     */
    public static int sportEventsExamPeriod(List<String[]> rows) {

        List<List<String>> calendar = toColumnWise(rows);

        List<String> examMonths = List.of("3", "5", "10", "12");

        return examMonths.stream()
                .mapToInt(m -> numberOfSportEventsMonth(calendar, m))
                .sum();
    }


    //__________________________________________________________________
    //______________________QUESTION 5 _________________________________
    /**
     * Computes the percentage of music events that match a given location type
     * (e.g., "indoor" or "outdoor").
     *
     * @param rows     the row-wise CSV data
     * @param locationType  "indoor" or "outdoor"
     * @return percentage of music events that match the given location type
     */
    public static double computeMusicPercentage(List<String[]> rows, String locationType) {

        // Convert to column-wise structure
        List<List<String>> calendar = toColumnWise(rows);

        // Extract needed columns
        List<String> music = getColumnByHeader(calendar, "Music");
        List<String> location = getColumnByHeader(calendar, "Indoor/Outdoor");

        // Count total music events
        long totalMusic = IntStream.range(1, music.size())
                .filter(i -> music.get(i).equalsIgnoreCase("yes"))
                .count();

        if (totalMusic == 0) {
            return 0.0;
        }

        // Count music events matching the requested location type
        long matching = IntStream.range(1, music.size())
                .filter(i -> music.get(i).equalsIgnoreCase("yes"))
                .filter(i -> location.get(i).equalsIgnoreCase(locationType))
                .count();

        return (100.0 * matching) / totalMusic;
    }

    //__________________________________________________________________
    //______________________Printing Results _________________________________

    /**
     * Prints the number of events for each month.
     *
     * @param eventsPerMonth map of month → number of events
     */
    public static void printEventsPerMonth(Map<Integer, Long> eventsPerMonth) {
        System.out.println("\nEvents per month:");

        IntStream.rangeClosed(1, 12)
                .forEach(month ->
                        System.out.println(monthName(month) + ": " + eventsPerMonth.getOrDefault(month, 0L))
                );
    }

    /**
     * Prints the percentage of music events that are indoor and outdoor.
     *
     * @param rows the row-wise CSV data
     */
    public static void printMusicPercentages(List<String[]> rows) {

        double indoorPct = computeMusicPercentage(rows, "indoor");
        double outdoorPct = computeMusicPercentage(rows, "outdoor");

        System.out.printf("Percentage of indoor music events: %.2f%%\n", indoorPct);
        System.out.printf("Percentage of outdoor music events: %.2f%%\n", outdoorPct);
    }

    /**
     * Prints indoor/outdoor sport event counts and percentages.
     *
     * @param indoor  number of indoor sport events
     * @param outdoor number of outdoor sport events
     */
    public static void printIndoorOutdoorStats(long indoor, long outdoor) {
        long total = indoor + outdoor;

        System.out.println("Indoor events: " + indoor);
        System.out.println("Outdoor events: " + outdoor);

        if (total > 0) {
            System.out.printf("The relative percentage of sport event Indoor is %.2f%s\n",
                    (100.0 * indoor / total), "%");
            System.out.printf("The relative percentage of sport event Outdoor is %.2f%s\n",
                    (100.0 * outdoor / total), "%");
        }
    }

    //__________________________________________________________________________________
    //__________________________________________________________________________________

    /**
     * Converts a month number (1–12) into a formatted month name.
     *
     * @param month month number
     * @return formatted month name
     */
    public static String monthName(int month) {
        return Month.values()[month - 1].name().charAt(0)
                + Month.values()[month - 1].name().substring(1).toLowerCase();
    }

    /**
     * Enum representing the 12 months of the year.
     */
    public enum Month {
        JANUARY, FEBRUARY, MARCH, APRIL,
        MAY, JUNE, JULY, AUGUST,
        SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
    }
}
