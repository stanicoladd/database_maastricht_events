error id: file:///C:/Users/moham/OneDrive/Documents/dummy%20project%201-1/group-5/Dummy%20project/FileHandler.java:java/nio/file/Files#
file:///C:/Users/moham/OneDrive/Documents/dummy%20project%201-1/group-5/Dummy%20project/FileHandler.java
empty definition using pc, found symbol in pc: java/nio/file/Files#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 49
uri: file:///C:/Users/moham/OneDrive/Documents/dummy%20project%201-1/group-5/Dummy%20project/FileHandler.java
text:
```scala
import java.io.IOException;
import java.nio.file.@@Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class FileHandler {

    /*
     * this methods read csv file columnWise and get its content into a ist of strings
     * Each list represent a column in the csv file
     * so the first list contains all the IDS and the second list
     * contains all the grades related to course1 and so on
     * with this type of reading it becomes easier to compute statistics about courses
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
                                .map(row -> {
                                    String value = (colIndex < row.length) ? row[colIndex].trim() : "";
                                    return "NG".equalsIgnoreCase(value) ? "0.0" : value;
                                })
                                .toList())
                            .toList();                          
        }catch(IOException exception){
            System.out.println("Not able to read the file");
            return Collections.emptyList();
        } 
    }


  
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: java/nio/file/Files#