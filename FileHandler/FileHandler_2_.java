import java.io.IOException; // The error type used if reading the file fails
import java.nio.file.Files; //provides the method for reading the file
import java.nio.file.Path; // represent the path to the CSV file location
import java.util.List; // stores the lines of the file in a list

public class newcode { // defines the class. The files must be named FileHandler.java.

    public static void main(String[] args) throws IOException { //Java starts running the program here; a file-reading error may be passed on
        
        List<String> lines = Files.readAllLines( // read every line of the CSV into a list of Strings
            Path.of("Maastricht_Events_Calendar_Final_Calendar_.csv") //location of the CSV file
        ); // finish the readAllLines statement

        String[] headers = lines.get(0).split(","); // splits the first line into column names
        int sportIndex = findColumn(headers, "Sport"); //Finds the position of the Sport column
        int locationIndex = findColumn(headers, "Indoor/Outdoor"); //Finds th eposition of the Indoor/outdoor column

        // here we start writing the code for the third question: "What % of sport events are indoor vs.outdoor?"
        //we first count the number of indoor sport events:
        int indoorSportEvents = 0; //starts the count at 0

        for (int i = 1; i < lines.size(); i++) { // goes through each event, skipping the header
            String[] event = lines.get(i).split(","); //splits this event's line into values

            if (event[sportIndex].trim().equalsIgnoreCase("yes")//check whether sport is "yes"
                    && event[locationIndex].trim().equalsIgnoreCase("indoor")) { //also checks  whether the location is "indoor"
                indoorSportEvents++; //adds one when both conditions are true
            }
        }

        System.out.println("Number of indoor events: " + indoorSportEvents); 

        //now we count for the outdoor sports events:
        int outdoorSportEvents = 0;

        for (int i = 1; i < lines.size(); i++){
            String[] event = lines.get(i).split(",");

            if (event[sportIndex].trim().equalsIgnoreCase("yes") //check it is a sport event
                && event[locationIndex].trim().equalsIgnoreCase("outdoor")){ //check whether it is outdoor
                outdoorSportEvents++; //adds one when both conditions are true
            }
        }

        //answer to question 3
        int totalSportEvents = indoorSportEvents + outdoorSportEvents;
        if(totalSportEvents > 0){
            double indoorPercentage = 100.0 * indoorSportEvents / totalSportEvents;
            double outdoorPercentage = 100.0 * outdoorSportEvents / totalSportEvents;

            System.out.println("Indoor sport events: " + indoorPercentage + "%");
            System.out.println("OUtdoor sport events: "+outdoorPercentage + "%");
        }
    }

    public static int findColumn(String[] headers, String name) { //finds a column by its name and returns its position
        for (int i = 0; i < headers.length; i++) { //check each column name
            if (headers[i].trim().equalsIgnoreCase(name)) { // compare it with the name we want
                return i; // return its position when found
            }
        }
        throw new IllegalArgumentException("Column not found: " + name); //reports an error if the column is missing
    }
}

