//package FileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class FileHandler{

    /*
     * this methods read csv file columnWise and get its content into a ist of strings
     * Each list represent a column in the csv file
     * so the first list contains all the Events's names and the second list
     * contains the organizations and so on
     * with this type of reading it becomes easier to compute statistics about events
     */

    public static List<List<String>>readCSV(String fileName){
        try(Stream<String> lineRead = Files.lines(Path.of(fileName))){
            List<String[]> rows = lineRead
                .map(line -> line.split(","))
                .toList();
            if(rows.isEmpty()) return Collections.emptyList();
            int numberOfAttributes = rows.getFirst().length;
            return IntStream.range(0, numberOfAttributes)
                            .mapToObj(colIndex -> rows.stream()
                                .map(row -> (colIndex < row.length) ? row[colIndex].trim().strip() : "")
                                    .toList())
                                .toList();                        
        }catch(IOException exception){
            System.out.println("Not able to read the file");
            return Collections.emptyList();
        } 
    }

    public static List<String> getColumnByHeader(List<List<String>> calender, String header){
        return calender.stream()
            .filter(column -> column.getFirst().equalsIgnoreCase(header))
            .findFirst()
            .orElse(List.of());
    }

    public static int numberOfEventsForAGivenMonth(List<List<String>> calendar, String month){
        return (int)getColumnByHeader(calendar, "start month")
            .stream()
            .filter(x -> x.equalsIgnoreCase(month)).count();
        
    }

    public static int numberOfSportEvents(List<List<String>> calendar){
        return calendar.stream()
                .filter(col -> col.getFirst().equalsIgnoreCase("Sport"))
                .findFirst()
                .map(col -> col.stream()
                        .skip(1) // skip header
                        .filter("yes"::equalsIgnoreCase)
                        .count()
                )
                .orElse(0L).intValue();
    }

    public static int numberOfSportEventsIndoor(List<List<String>> calendar){
        List<String> sport = getColumnByHeader(calendar, "Sport");
        List<String> indoorOutdoor = getColumnByHeader(calendar, "Indoor/Outdoor");

         return (int)IntStream.range(1, Math.min(sport.size(), indoorOutdoor.size()))
                .filter(i -> sport.get(i).equalsIgnoreCase("yes")
                        && indoorOutdoor.get(i).equalsIgnoreCase("indoor"))
                .count();
    }

    public static int numberOfSportEventsOutdoor(List<List<String>> calendar){
        List<String> sport = getColumnByHeader(calendar, "Sport");
        List<String> indoorOutdoor = getColumnByHeader(calendar, "Indoor/Outdoor");

        return (int) IntStream.range(1, Math.min(sport.size(), indoorOutdoor.size()))
                .filter(i -> sport.get(i).equalsIgnoreCase("yes")
                        && indoorOutdoor.get(i).equalsIgnoreCase("outdoor"))
                .count();
    }

    public static void main(String[] args){
        List<List<String>> calendar = readCSV("Maastricht_Events_Calendar_Final_Calendar_.csv");
        System.out.println(numberOfSportEventsIndoor(calendar));

    }


}
