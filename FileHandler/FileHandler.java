package FileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class for reading and processing CSV files in a column‑major format.
 * <p>
 * The CSV is interpreted such that each column becomes a {@code List<String>},
 * and the entire file becomes a {@code List<List<String>>}. This makes it easy
 * to compute statistics on event attributes (e.g., sport events, indoor/outdoor,
 * monthly counts).
 * <p>
 * The first element of each column list is assumed to be the header.
 */
public class FileHandler {


    /**
     * Reads a CSV file and returns its content in a column‑major structure.
     * <p>
     * Each column of the CSV becomes a {@code List<String>}, where:
     * <ul>
     *     <li>index 0 = header</li>
     *     <li>index 1..n = values</li>
     * </ul>
     *
     * @param fileName the path to the CSV file
     * @return a list of columns, each column being a list of strings;
     *         returns an empty list if the file cannot be read or is empty
     */
    public static List<List<String>> readCSV(String fileName) {
        try (Stream<String> lineRead = Files.lines(Path.of(fileName))) {
            List<String[]> rows = lineRead
                    .map(line -> line.split(","))
                    .toList();
            if (rows.isEmpty()) return Collections.emptyList();
            int numberOfAttributes = rows.getFirst().length;
            return IntStream.range(0, numberOfAttributes)
                    .mapToObj(i -> rows.stream()
                            .map(row -> (i < row.length && !row[i].isBlank())
                                    ? row[i].trim()
                                    : "unknown")
                            .toList())
                            .toList();

        } catch (IOException exception) {
            System.out.println("Not able to read the file");
            return Collections.emptyList();
        }
    }


    /**
     * Retrieves a column by its header name.
     *
     * @param calendar the column‑major CSV structure
     * @param header   the header to search for (case‑insensitive)
     * @return the column list if found; otherwise an empty list
     */
    public static List<String> getColumnByHeader(List<List<String>> calendar, String header) {
        return calendar.stream()
                .filter(column -> column.getFirst().equalsIgnoreCase(header))
                .findFirst()
                .orElse(List.of());
    }

    /**
     * Counts how many events occur in a given month.
     *
     * @param calendar the column‑major CSV structure
     * @param month    the month number as a string (e.g., "1", "2", ..., "12")
     * @return the number of events whose "start month" matches the given month
     */
    public static int numberOfEventsForAGivenMonth(List<List<String>> calendar, String month) {
        List<String> start = getColumnByHeader(calendar, "start month");
        List<String> end = getColumnByHeader(calendar, "end month");
        int m = Integer.parseInt(month);

        List<Integer> matchingIndices = IntStream.range(1,calendar.getFirst().size()) // skip header at index 0
                .filter(i -> {
                    int s = Integer.parseInt(start.get(i));
                    int e = Integer.parseInt(end.get(i));
                    return (s <= e) ? (s <= m && m <= e) : (m >= s || m <= e); // handles year wrap
                })
                .boxed()
                .toList();

        // Rebuild full rows (all columns) for the matching event indices
        return matchingIndices.stream()
                .map(i -> calendar.getFirst().get(i)).toList().size();

    }

    public static int monthWithTheHighestNumberOfEvents(List<List<String>> calendar) {
        int m = 0; int temp; int j = 0;
        for (int i = 1;  i < 13; i++) {
            temp = numberOfEventsForAGivenMonth(calendar, String.valueOf(i));
            if (temp > m) {
                m = temp;
                j = i;
            }
        }
        return j;
    }

    /**
     * Counts how many events are marked as sport events.
     *
     * @param calendar the column‑major CSV structure
     * @return the number of rows where the "Sport" column equals "yes"
     */
    public static int numberOfSportEvents(List<List<String>> calendar) {
        return calendar.stream()
                .filter(col -> col.getFirst().equalsIgnoreCase("Sport"))
                .findFirst()
                .map(col -> col.stream()
                        .skip(1)
                        .filter("yes"::equalsIgnoreCase)
                        .count())
                .orElse(0L)
                .intValue();
    }

    /**
     * Counts how many sport events are indoor.
     *
     * @param calendar the column‑major CSV structure
     * @return the number of rows where "Sport" = "yes" and "Indoor/Outdoor" = "indoor"
     */
    public static int numberOfSportEventsIndoor(List<List<String>> calendar) {
        List<String> sport = getColumnByHeader(calendar, "Sport");
        List<String> indoorOutdoor = getColumnByHeader(calendar, "Indoor/Outdoor");

        return (int) IntStream.range(1, Math.min(sport.size(), indoorOutdoor.size()))
                .filter(i -> sport.get(i).equalsIgnoreCase("yes")
                        && indoorOutdoor.get(i).equalsIgnoreCase("indoor"))
                .count();
    }

    /**
     * Counts how many sport events are outdoor.
     *
     * @param calendar the column‑major CSV structure
     * @return the number of rows where "Sport" = "yes" and "Indoor/Outdoor" = "outdoor"
     */
    public static int numberOfSportEventsOutdoor(List<List<String>> calendar) {
        List<String> sport = getColumnByHeader(calendar, "Sport");
        List<String> indoorOutdoor = getColumnByHeader(calendar, "Indoor/Outdoor");

        return (int) IntStream.range(1, Math.min(sport.size(), indoorOutdoor.size()))
                .filter(i -> sport.get(i).equalsIgnoreCase("yes")
                        && indoorOutdoor.get(i).equalsIgnoreCase("outdoor"))
                .count();
    }

    public static int numberOfSportEventsMonth(List<List<String>> calendar,  String month) {
        List<String> sport = getColumnByHeader(calendar, "Sport");
        List<String> startMonth = getColumnByHeader(calendar, "Start month");
        List<String> endMonth = getColumnByHeader(calendar, "End month");
        int m = Integer.parseInt(month);
        List<Integer> matchingIndices = IntStream.range(1,calendar.getFirst().size()) // skip header at index 0
                .filter(i -> {
                    int s = Integer.parseInt(startMonth.get(i));
                    int e = Integer.parseInt(endMonth.get(i));

                    return (s <= e) ? (s <= m && m <= e) : (m >= s || m <= e); // handles year wrap
                })
                .boxed()
                .toList();
        return matchingIndices.stream()
                .filter(x -> sport.get(x).equalsIgnoreCase("yes"))
                .map(i -> calendar.getFirst().get(i)).toList().size();
    }

    public static int sportEventsExamPeriod(List<List<String>> calendar) {
        List<String> month = List.of("3","5", "12", "10");
        return
                month.stream()
                    .map(m -> numberOfSportEventsMonth(calendar, m))
                    .reduce(Integer::sum)
                    .orElse(0);
    }


    static void main() {
        List<List<String>> calendar = readCSV("FileHandler\\MaastrichtEventsCalendarFinalCalendar.csv");

        List<String> month = List.of("January", "February", "March", "April", "May", "June",
                "July", "Augustus", "September", "October","November", "December");
        System.out.println("The number of Events per month is divided as follows:");
        for (int i = 1; i < 13; i++) {
            System.out.println(month.get(i - 1) + " = " + numberOfEventsForAGivenMonth(calendar, String.valueOf(i)));
        }
        System.out.println("****************************************************************");

        System.out.println("The month with the highest number of events is " + month.get(monthWithTheHighestNumberOfEvents(calendar) - 1));


        System.out.printf("the relative percentage of indoor sport event is  %.2f%s\n",(double)numberOfSportEventsIndoor(calendar) / (double)numberOfSportEvents(calendar) * 100, "%");

        System.out.printf("the relative percentage of outdoor sport event is %.2f%s\n",(double)numberOfSportEventsOutdoor(calendar) / (double)numberOfSportEvents(calendar) * 100, "%");

        System.out.println("The number of sport event during exam period is : " +sportEventsExamPeriod(calendar));

    }
}
