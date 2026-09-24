import java.io.IOException; // the type of error that can occur when reading a file
import java.nio.file.Files; //provides methods for reading files
import java.nio.file.Path; // represents the location of the files
import java.util.List; // allows us to store multiple lines in a list

public class newcode { // defines the class. The files must be named FileHandler.java.

    public static void main(String[] args) throws IOException { //Java starts running the program here; a file-reading error may be passed on
        List<String> lines = Files.readAllLines( // read every line of the CSV into a list of Strings
            Path.of("Maastricht_Events_Calendar_Final_Calendar_.csv") //location of the CSV file
        ); // finish the readAllLines statement

        String[] headers = lines.get(0).split(","); // get the first line (headers) and separate it into colomn names
        int sportIndex = findColumn(headers, "Sport"); //Counter : no matching events have been found yet. 
        int locationIndex = findColumn(headers, "Indoor/Outdoor");

        int indoorSportEvents = 0;

        for (int i = 1; i < lines.size(); i++) { // Start at 1: skip header
            String[] event = lines.get(i).split(",");

            if (event[sportIndex].trim().equalsIgnoreCase("yes")
                    && event[locationIndex].trim().equalsIgnoreCase("indoor")) {
                indoorSportEvents++;
            }
        }

        System.out.println(indoorSportEvents);
    }

    public static int findColumn(String[] headers, String name) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].trim().equalsIgnoreCase(name)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Column not found: " + name);
    }
}
