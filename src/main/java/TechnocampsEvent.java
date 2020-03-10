import com.google.api.client.util.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class TechnocampsEvent {

    private static final ArrayList<TechnocampsEvent> allEvents = new ArrayList<>();

    private final String uniqueID;
    private DateTime dateOfEvent;
    private final DateTime startTime;
    private final DateTime endTime;
    private final String universityCode;
    private final String eventType;
    private final String schoolName;
    private final String keyStage;
    private final String workshop;
    private final ArrayList<Staff> listOfStaff;


    /**
     * Constructor for TechnocampsEvent objects.
     *
     * @param dateOfEvent a String containing a date for the event in the format dd/mm/yyyy
     * @param startTime a String containing a start time for the event in the 24hr format - 00:00
     * @param endTime a String containing an end time for the event in the 24hr format - 00:00
     * @param universityCode a String code stating the University the event belongs to.
     * @param eventType a String stating the type of event i.e. workshop.
     * @param schoolName a String containing the name of the school.
     * @param keyStage a String containing the key stage of the class
     * @param workshop a string containing the name of the workshop to be delivered.
     * @param listOfStaff an Arraylist containing Delivery Officers assigned to the event.
     */
    public TechnocampsEvent(String uniqueID, DateTime dateOfEvent, DateTime startTime, DateTime endTime,
                            String universityCode, String eventType,
                            String schoolName, String keyStage, String workshop,
                            ArrayList<Staff> listOfStaff) {
        this.dateOfEvent = dateOfEvent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.universityCode = universityCode;
        this.eventType = eventType;
        this.schoolName = schoolName;
        this.keyStage = keyStage;
        this.workshop = workshop;
        this.listOfStaff = listOfStaff;
        this.uniqueID = uniqueID;
    }

    /**
     * Prints a string containing the details of the event to the console. It is used when a specific
     * delivery officer is not specified and displays a list of all the delivery officers assigned to the event object.
     */
    public void printEventDetails() {
        if (startTime.isDateOnly()) {
            System.out.println(Validation.convertDateTimeToUKFormat(dateOfEvent) +
                    " | " + "   All Day   " + " | " + eventType + " | " + schoolName + " | "
                    + keyStage + " | " + workshop);
        } else {
            System.out.println(Validation.convertDateTimeToUKFormat(dateOfEvent) +
                    " | " + startTime.toStringRfc3339().split("T")[1].substring(0, 5) + " - " +
                    endTime.toStringRfc3339().split("T")[1].substring(0, 5) + " | " + eventType
                    + " | " + schoolName + " | " + keyStage + " | " + workshop);
        }
        System.out.println("Delivery Officers Assigned:");
        for (Staff staff : listOfStaff) {
            System.out.println(staff.getName());
        }
        System.out.println();
    }


    /**
     * Prints a string containing the details of the event to the console. It is used when a specific
     * delivery officer is named.
     */
    public void printOfficerEventDetails() {
        if (startTime.isDateOnly()) {
            System.out.println(Validation.convertDateTimeToUKFormat(dateOfEvent) +
                    " | " + "   All Day   " + " | " + eventType + " | " + schoolName + " | "
                    + keyStage + " | " + workshop);
        } else {
            System.out.println(Validation.convertDateTimeToUKFormat(dateOfEvent) +
                    " | " + startTime.toStringRfc3339().split("T")[1].substring(0, 5) + " - " +
                    endTime.toStringRfc3339().split("T")[1].substring(0, 5) + " | " + eventType
                    + " | " + schoolName + " | " + keyStage + " | " + workshop);
        }

    }

    public static void printAllEvents() {
        for (TechnocampsEvent event : TechnocampsEvent.getAllEvents()) {
            if (event.getEventType().equals("Workshop") || event.getEventType().equals("Technoclub")
                    || event.getEventType().equals("Technoteach") || event.getEventType().equals("AL")
                    || event.getEventType().equals("Unavailable")) {
                event.printEventDetails();
            }

        }
    }

    public static void printAllEvents(String startDate) throws ParseException {
        System.out.println();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date firstSearchDate = new Date();
        if (!(startDate == null)) {
            firstSearchDate = format.parse(startDate);
        }
        if (TechnocampsEvent.getAllEvents().size() == 0) {
            System.out.println("No events in Calendar.");
        } else {
            System.out.println("Future Events after: " + startDate + "\n");
            for (TechnocampsEvent event : TechnocampsEvent.getAllEvents()) {
                String eventDate = event.getDateOfEvent().toStringRfc3339();
                Date eventDateFormatted = format.parse(eventDate.substring(8, 10) + "/" +
                        eventDate.substring(5, 7) + "/" + eventDate.substring(0, 4));
                if (event.getEventType().equals("Workshop") || event.getEventType().equals("Technoclub") ||
                        event.getEventType().equals("Technoteach") || event.getEventType().equals("AL")
                        || event.getEventType().equals("Unavailable")) {
                    if (firstSearchDate.compareTo(eventDateFormatted) <= 0) {
                        event.printEventDetails();
                    }
                }
            }
        }
    }


    /**
     * Iterates through every event stored and prints out details of each. The events can be searched
     * between two dates specified if provided.
     *
     * @param startDate A String which should be of format dd/MM/yyyy.
     * @param endDate   A String which should be of format dd/MM/yyyy.
     * @throws ParseException if dates provided are poorly formatted.
     */

    public static void printAllEvents(String startDate, String endDate) throws ParseException {
        System.out.println();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date firstSearchDate = new Date();
        Date secondSearchDate = new Date();
        if (!(startDate == null)) {
            firstSearchDate = format.parse(startDate);
        }
        if (!(endDate == null)) {
            secondSearchDate = format.parse(endDate);
        }
        if (TechnocampsEvent.getAllEvents().size() == 0) {
            System.out.println("No events in Calendar.");
        } else {
            System.out.println("Events Between: " + startDate + " - " + endDate + "\n");
            for (TechnocampsEvent event : TechnocampsEvent.getAllEvents()) {
                String eventDate = event.getDateOfEvent().toStringRfc3339();
                Date eventDateFormatted = format.parse(eventDate.substring(8, 10) + "/" +
                        eventDate.substring(5, 7) + "/" + eventDate.substring(0, 4));
                if (event.getEventType().equals("Workshop") || event.getEventType().equals("Technoclub") ||
                        event.getEventType().equals("Technoteach") || event.getEventType().equals("AL")
                        || event.getEventType().equals("Unavailable")) {
                    if (endDate == null) {
                        if (firstSearchDate.compareTo(eventDateFormatted) <= 0) {
                            event.printEventDetails();
                        }
                    } else {

                        if (firstSearchDate.compareTo(eventDateFormatted) <= 0 &&
                                secondSearchDate.compareTo(eventDateFormatted) >= 0) {
                            event.printEventDetails();
                        }
                    }
                }
            }
        }
    }


    /**
     * Assigns delivery officer objects to an event. Not Implemented yet.
     *
     * @param name A string containing the name of the delivery officer to be assigned.
     */
    public void assignDeliveryOfficer(String name) {
        boolean officerFound = false;
        for (Staff deliveryofficer : getListOfStaff()) {
            if (deliveryofficer.getName().toUpperCase().equals(name.toUpperCase())) {
                officerFound = true;
                break;
            }
        }
        if (officerFound) {
            System.out.println("Officer already assigned to event.");
        }else {

            for (Staff staff : Staff.getAllStaffList()) {
                if (staff.getName().equals(name)) {
                    getListOfStaff().add(staff);
                    officerFound = true;
                }
            }
            if (!officerFound) {
                System.out.println("No member of staff by that name exists.");
            }
        }
    }

    /**
     * Removes a delivery officer from an event. Not Implemented Yet.
     *
     * @param name a String containing the name of a delivery officer.
     */
    public void removeDeliveryOfficer(String name) {
        boolean officerFound = false;
        for (Staff deliveryofficer : getListOfStaff()) {
            if (deliveryofficer.getName().toUpperCase().equals(name.toUpperCase())) {
                officerFound = true;
                getListOfStaff().remove(deliveryofficer);
                System.out.println(name + "Has been removed from this event.");
                break;
            }
        }
        if (!officerFound) {
            System.out.println("No staff member by the name " + name + " is assigned to this event.");
        }
    }

    /**
     * Changes the date of an event and removes all delivery officers assigned to it.
     *
     * @param newDate a String of the new date in the format dd/mm/yyyy.
     */
    public void setDate(DateTime newDate) {
        setDateOfEvent(newDate);
        getListOfStaff().clear();
    }

    /**
     * Getter method for the date of an event.
     *
     * @return a String of the date in the format dd/mm/yyyy.
     */
    public DateTime getDateOfEvent() {
        return dateOfEvent;
    }

    /**
     * Setter method for the date of an event.
     *
     * @param dateOfEvent a String of the new date in the format dd/mm/yyyy
     */
    public void setDateOfEvent(DateTime dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }

    /**
     * Getter method for the end time of an event.
     *
     * @return a String of the time in the format 00:00.
     */
    public DateTime getEndTime() {
        return endTime;
    }

    /**
     * Getter method for the type of event.
     *
     * @return a String of the type of event.
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Getter method for the name of the event.
     *
     * @return a String containing the name of the school.
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * Getter method for the key stage of the event.
     * @return a String containing a 3 character Key stage code.
     */
    public String getKeyStage() {
        return keyStage;
    }

    /**
     * Getter method for the start time of the event.
     *
     * @return a String of the time in the format 00:00.
     */
    public DateTime getStartTime() {
        return startTime;
    }

    /**
     * Getter method for the unique ID of the event.
     *
     * @return a String made up of the date and initials of the school name. Possibly with an extra digit if there are
     * clashes.
     */
    public String getUniqueID() {
        return uniqueID;
    }

    /**
     * Getter method for the workshop name.
     *
     * @return a String containing the workshop name.
     */
    public String getWorkshop() {
        return workshop;
    }

    /**
     * Getter method for the university code.
     *
     * @return A string code of length 4 or less.
     */
    public String getUniversityCode() {
        return universityCode;
    }

    /**
     * Getter method for the list of Delivery Officers assigned to an event object.
     *
     * @return An ArrayList of Delivery officers.
     */
    public ArrayList<Staff> getListOfStaff() {
        return listOfStaff;
    }

    public static ArrayList<TechnocampsEvent> getAllEvents() {
        return allEvents;
    }

    /**
     * Allows searching for events by month and year.
     *
     * @return An ArrayList containing startDate and endDate for the selected month (startDate, endDate).
     */
    public static ArrayList<String> searchByMonth() {
        Scanner scanner = new Scanner(System.in);
        LocalDate currentDate = LocalDate.now();
        int y = currentDate.getYear();
        int monthChosen = 0;
        String monthName = null;
        while (monthChosen == 0) {

            System.out.println("Current Year: " + y);
            System.out.println("P) Previous Year\n" +
                    "1) January\n" +
                    "2) February\n" +
                    "3) March\n" +
                    "4) April\n" +
                    "5) May\n" +
                    "6) June\n" +
                    "7) July\n" +
                    "8) August\n" +
                    "9) September\n" +
                    "10) October\n" +
                    "11) November\n" +
                    "12) December\n" +
                    "N) Next Year");
            System.out.println("Please choose an option: ");
            String response = scanner.nextLine().trim().toUpperCase();
            switch (response) {
                case "P":
                    y = y - 1;
                    break;
                case "1":
                    monthChosen = 1;
                    monthName = "January";
                    break;
                case "2":
                    monthChosen = 2;
                    monthName = "February";
                    break;
                case "3":
                    monthChosen = 3;
                    monthName = "March";
                    break;
                case "4":
                    monthChosen = 4;
                    monthName = "April";
                    break;
                case "5":
                    monthChosen = 5;
                    monthName = "May";
                    break;
                case "6":
                    monthChosen = 6;
                    monthName = "June";
                    break;
                case "7":
                    monthChosen = 7;
                    monthName = "July";
                    break;
                case "8":
                    monthChosen = 8;
                    monthName = "August";
                    break;
                case "9":
                    monthChosen = 9;
                    monthName = "September";
                    break;
                case "10":
                    monthChosen = 10;
                    monthName = "October";
                    break;
                case "11":
                    monthChosen = 11;
                    monthName = "November";
                    break;
                case "12":
                    monthChosen = 12;
                    monthName = "December";
                    break;
                case "N":
                    y = y + 1;
                    break;
            }
        }
        YearMonth yearMonthObject = YearMonth.of(y, monthChosen);
        String startDate = "01/" + String.format("%02d", monthChosen) + "/" + y;
        int daysInMonth = yearMonthObject.lengthOfMonth();
        String endDate = String.format("%02d", daysInMonth) + "/" + String.format("%02d", monthChosen) + "/" + y;
        ArrayList<String> searchDates = new ArrayList<>();
        searchDates.add(startDate);
        searchDates.add(endDate);
        System.out.println("Events in Month: " + monthName + " " + y);
        return searchDates;
    }

    @Override
    public String toString() {
        return "TechnocampsEvent{" +
                //"uniqueID='" + uniqueID + '\'' +
                ", dateOfEvent=" + dateOfEvent +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", universityCode='" + universityCode + '\'' +
                ", eventType='" + eventType + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", keyStage='" + keyStage + '\'' +
                ", workshop='" + workshop + '\'' +
                ", listOfStaff=" + listOfStaff +
                '}';
    }

}
