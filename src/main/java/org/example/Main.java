package org.example;

// Importam List pentru ca vom avea o lista de evenimente
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Numele fisierului Excel pe care vrem sa il citim.
        // Fisierul trebuie sa se numeasca events.xlsx
        // si sa fie pus in folderul principal EventAnalyzer,
        // langa pom.xml.
        String filePath = "events.xlsx";

        try {

            // ExcelReader citeste fisierul Excel.
            // Metoda readEvents() transforma fiecare rand
            // din Excel intr-un obiect Event.
            //
            // Toate obiectele Event sunt puse in lista "events".
            List<Event> events = ExcelReader.readEvents(filePath);


            // Afisam cate evenimente au fost citite din Excel.
            System.out.println(
                    "Number of events: " + events.size()
            );


            // Trecem prin fiecare eveniment din lista.
            //
            // Se citeste:
            // "pentru fiecare Event numit event din lista events"
            for (Event event : events) {

                // Afisam evenimentul.
                //
                // Java va folosi metoda toString()
                // pe care am scris-o in Event.java.
                System.out.println(event);
            }


        } catch (Exception e) {

            // Daca apare o eroare la citirea Excel-ului,
            // programul ajunge aici.

            System.out.println(
                    "ERROR: " + e.getMessage()
            );

            // Afiseaza mai multe detalii despre eroare.
            // Este foarte util pentru debugging.
            e.printStackTrace();
        }
    }
}