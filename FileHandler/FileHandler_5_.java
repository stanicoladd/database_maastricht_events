import java.io.IOException; // The error type used if reading the file fails
import java.nio.file.Files; //provides the method for reading the file
import java.nio.file.Path; // represent the path to the CSV file location
import java.util.List; // stores the lines of the file in a list

public class FileHandler_5_{ // defines the class. The files must be named FileHandler.java.

    public static void main(String[] args) throws IOException { //Java starts running the program here; a file-reading error may be passed on
        
        List<String> lines = Files.readAllLines( // read every line of the CSV into a list of Strings
            Path.of("dataexcel.csv") //location of the CSV file
        ); // finish the readAllLines statement

        String[] headers = lines.get(0).split(";"); // splits the first line into column names
        int sportIndex = findColumn(headers, "Sport"); //Finds the position of the Sport column
        int locationIndex = findColumn(headers, "Indoor/Outdoor"); //Finds th eposition of the Indoor/outdoor column
        int monthIndex = findColumn(headers, "Start month"); // Finds the position of the Start month column


        // Setup for Question 1: Array of month names and an array to hold counts
        String[] months = {
        "January", "February", "March", "April",
        "May", "June", "July", "August",
         "September", "October", "November", "December"
};


int[] eventsPerMonth = new int[12];
        // here we start writing the code for the third question: "What % of sport events are indoor vs.outdoor?"
        //we first count the number of indoor sport events:
        int indoorSportEvents = 0; //starts the count at 0

        for (int i = 1; i < lines.size(); i++) { // goes through each event, skipping the header
            String[] event = lines.get(i).split(";"); //splits this event's line into values


            // Question 1: Reads the month number and increments the counter
if (event.length > monthIndex) {
    try {
        int monthValue = Integer.parseInt(event[monthIndex].replace("\"", "").trim());
        if (monthValue >= 1 && monthValue <= 12) {
            eventsPerMonth[monthValue - 1]++;
        }
    } catch (NumberFormatException ignored) {}
}

            if (event.length > Math.max(sportIndex, locationIndex)) {
    if (event[sportIndex].replace("\"", "").trim().equalsIgnoreCase("yes")
            && event[locationIndex].replace("\"", "").trim().equalsIgnoreCase("indoor")) {
        indoorSportEvents++;
    }
}
            }

        System.out.println("Number of indoor events: " + indoorSportEvents); 

        //now we count for the outdoor sports events:
        int outdoorSportEvents = 0;

        for (int i = 1; i < lines.size(); i++){
            String[] event = lines.get(i).split(";");

            if (event[sportIndex].trim().equalsIgnoreCase("yes") //check it is a sport event
                && event[locationIndex].trim().equalsIgnoreCase("outdoor")){ //check whether it is outdoor
                outdoorSportEvents++; //adds one when both conditions are true
            }
        }
        System.out.println("Number of outdoor sport events : " + outdoorSportEvents);
        

        //answer to question 3
        int totalSportEvents = indoorSportEvents + outdoorSportEvents;
        if(totalSportEvents > 0){
            double indoorPercentage = 100.0 * indoorSportEvents / totalSportEvents;
            double outdoorPercentage = 100.0 * outdoorSportEvents / totalSportEvents;

            System.out.println("Percentage of indoor sport events: " + indoorPercentage + "%");
            System.out.println("Percentage of outdoor sport events: "+outdoorPercentage + "%");
        }
        // Answer to Question 1: Print the total number of events for each month
System.out.println("\nNumber of events per month:");
for (int i = 0; i < 12; i++) {
    System.out.println(months[i] + ": " + eventsPerMonth[i]);
}
// Question 2: Find the month with the highest number of events
        int busiestMonth = 0; // Starts assuming January (index 0) has the most events

        for (int i = 1; i < 12; i++) { // Loops through February to December
            if (eventsPerMonth[i] > eventsPerMonth[busiestMonth]) {
                busiestMonth = i; // Updates the index if a month has more events
            }
        }

        // Output for Question 2
        System.out.println("\nMonth with the highest number of events: " 
                + months[busiestMonth] + " (" + eventsPerMonth[busiestMonth] + " events)");
    }

    public static int findColumn(String[] headers, String name) { //finds a column by its name and returns its position
        for (int i = 0; i < headers.length; i++) { //check each column name
            if (headers[i].replace("\"", "").trim().equalsIgnoreCase(name)) { // compare it with the name we want
                return i; // return its position when found
            }
        }
        throw new IllegalArgumentException("Column not found: " + name); //reports an error if the column is missing
    }
}