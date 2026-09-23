package org.example;

// LocalDate ne permite sa lucram cu date calendaristice
// de exemplu: 14 March 2027
import java.time.LocalDate;


// Clasa Event reprezinta UN eveniment din Excel
public class Event {

    // =========================
    // VARIABILELE EVENIMENTULUI
    // =========================

    // Numele evenimentului
    private String eventName;

    // Organizatia care organizeaza evenimentul
    private String organization;

    // Locatia evenimentului
    private String location;

    // Indoor, Outdoor sau Both
    private String setting;

    // true = este eveniment sportiv
    // false = nu este eveniment sportiv
    private boolean sport;

    // true = este eveniment muzical
    // false = nu este eveniment muzical
    private boolean music;

    // Data de inceput
    private int startYear;
    private int startMonth;
    private int startDay;

    // Data de sfarsit
    private int endYear;
    private int endMonth;
    private int endDay;

    // Durata evenimentului
    private int duration;


    // =========================
    // CONSTRUCTOR
    // =========================

    // Constructorul este folosit atunci cand cream
    // un obiect Event cu:
    //
    // new Event(...)

    public Event(String eventName,
                 String organization,
                 String location,
                 String setting,
                 boolean sport,
                 boolean music,
                 int startYear,
                 int endYear,
                 int startMonth,
                 int endMonth,
                 int startDay,
                 int endDay,
                 int duration) {

        // "this.eventName" = variabila obiectului
        // "eventName" = valoarea primita de constructor

        this.eventName = eventName;
        this.organization = organization;
        this.location = location;
        this.setting = setting;

        this.sport = sport;
        this.music = music;

        this.startYear = startYear;
        this.endYear = endYear;

        this.startMonth = startMonth;
        this.endMonth = endMonth;

        this.startDay = startDay;
        this.endDay = endDay;

        this.duration = duration;
    }


    // =========================
    // GETTERS
    // =========================

    // Returneaza numele evenimentului
    public String getEventName() {
        return eventName;
    }


    // Returneaza organizatia
    public String getOrganization() {
        return organization;
    }


    // Returneaza locatia
    public String getLocation() {
        return location;
    }


    // Returneaza Indoor / Outdoor / Both
    public String getSetting() {
        return setting;
    }


    // Returneaza true daca este eveniment sportiv
    public boolean isSport() {
        return sport;
    }


    // Returneaza true daca este eveniment muzical
    public boolean isMusic() {
        return music;
    }


    // Returneaza anul de inceput
    public int getStartYear() {
        return startYear;
    }


    // Returneaza anul de sfarsit
    public int getEndYear() {
        return endYear;
    }


    // Returneaza luna de inceput
    public int getStartMonth() {
        return startMonth;
    }


    // Returneaza luna de sfarsit
    public int getEndMonth() {
        return endMonth;
    }


    // Returneaza ziua de inceput
    public int getStartDay() {
        return startDay;
    }


    // Returneaza ziua de sfarsit
    public int getEndDay() {
        return endDay;
    }


    // Returneaza durata
    public int getDuration() {
        return duration;
    }


    // =========================
    // DATA DE INCEPUT
    // =========================

    // Combina anul, luna si ziua intr-o data completa.
    //
    // De exemplu:
    // startYear = 2027
    // startMonth = 3
    // startDay = 14
    //
    // devine 14 March 2027

    public LocalDate getStartDate() {

        return LocalDate.of(
                startYear,
                startMonth,
                startDay
        );
    }


    // =========================
    // DATA DE SFARSIT
    // =========================

    public LocalDate getEndDate() {

        return LocalDate.of(
                endYear,
                endMonth,
                endDay
        );
    }


    // =========================
    // toString
    // =========================

    // @Override inseamna ca modificam metoda toString()
    // pe care Java o are deja pentru obiecte.

    // Astfel, daca scriem:
    //
    // System.out.println(event);
    //
    // Java va afisa informatiile de mai jos.

    @Override
    public String toString() {

        return eventName
                + " | "
                + getStartDate()
                + " - "
                + getEndDate();
    }
}