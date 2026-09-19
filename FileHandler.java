import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class FileHandler {


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

    public static List<String> getColumnByHeader(List<List<String>> calender, String header) {
        return calender.stream()
                .filter(column -> column.get(0).equalsIgnoreCase(header))
                .findFirst()
                .orElse(List.of());
    }

    public static int numberOfeventsForAGivenMonth(List<List<String>> calendar, String month){
        return (int)getColumnByHeader(calendar, "start month")
                .stream()
                .filter(x -> x.equalsIgnoreCase(month)).count();

    }

    static void main(){
        List<List<String>> calendar = readCSV("Maastricht_Events_Calendar(Final Calendar).csv");
        //calendar.stream().forEach(l  ->System.out.println(l.getFirst()));
        System.out.println(calendar.get(0).get(0));
        System.out.println(calendar.get(0).get(0).equalsIgnoreCase("Event Name"));
    }

}
