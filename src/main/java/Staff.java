import com.google.api.client.util.DateTime;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static java.time.temporal.TemporalAdjusters.previous;
import static java.time.temporal.TemporalAdjusters.next;

import java.util.*;

public class Staff {

    private static final ArrayList<Staff> allStaff = new ArrayList<>();
    private static final ArrayList<Staff> availableOfficers = new ArrayList<>();
    private static int staffNumber = 1;
    private String name;
    private String email;
    private int timeCommitment;
    private boolean adminPrivileges;
    private int uniqueStaffNumber;
    private boolean isDeliveryOfficer;

    /**
     * Constructor for objects of the class Staff.
     *
     * @param email          a String containing the email address of the officer.
     * @param name           a String containing a full name for the delivery officer.
     * @param timeCommitment an int containing the percentage time commitment for the delivery officer.
     */
    public Staff(String name, String email, int timeCommitment, boolean adminPrivileges, boolean isDeliveryOfficer) {
        this.email = email;
        this.name = name;
        this.timeCommitment = timeCommitment;
        this.adminPrivileges = adminPrivileges;
        this.isDeliveryOfficer = isDeliveryOfficer;
    }

    /**
     * Getter method for the name of the delivery officer.
     *
     * @return a String of the name of the delivery officer.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for the name of the delivery Officer, also updates the external text file.
     *
     * @param name a String of the new name of the delivery officer.
     * @throws IOException if error in writing to Staff details text file.
     */
    public void setName(String name) throws IOException {
        this.name = name;
        updateStaffInformation();
    }

    /**
     * Getter method for the percentage time commitment of the delivery officer.
     *
     * @return an int of the time commitment of the delivery officer.
     */
    public int getTimeCommitment() {
        return timeCommitment;
    }

    /**
     * Setter method for the percentage time commitment of the delivery officer.
     *
     * @param timeCommitment an int of the new percentage time commitment of the delivery officer.
     * @throws IOException if error in writing to Staff details text file.
     */
    public void setTimeCommitment(int timeCommitment) throws IOException {
        this.timeCommitment = timeCommitment;
        updateStaffInformation();
    }

    /**
     * Getter method for the email of the delivery officer.
     *
     * @return String of the email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter method for the email address of the delivery officer.
     *
     * @param email String of the new email address.
     * @throws IOException if error in writing to Staff details text file.
     */
    public void setEmail(String email) throws IOException {
        this.email = email;
        updateStaffInformation();
    }

    /**
     * Getter method for unique staff number.
     *
     * @return an int containing the unique Staff number.
     */
    public int getUniqueStaffNumber() {
        return uniqueStaffNumber;
    }

    /**
     * Setter method for the unique staff number.
     *
     * @param uniqueStaffNumber an int for the new unique staff number.
     */
    public void setUniqueStaffNumber(int uniqueStaffNumber) {
        this.uniqueStaffNumber = uniqueStaffNumber;
    }

    /**
     * Getter method for whether delivery officer has admin privileges or not.
     *
     * @return boolean, true if they are an admin, false if not.
     */
    public boolean getAdminPrivileges() {
        return adminPrivileges;
    }

    /**
     * Setter method for delivery officer admin privileges.
     *
     * @param adminPrivileges boolean value, true for admin privileges, false for none.
     * @throws IOException if error in writing to Staff details text file.
     */
    public void setAdminPrivileges(boolean adminPrivileges) throws IOException {
        this.adminPrivileges = adminPrivileges;
        updateStaffInformation();
    }

    public boolean isDeliveryOfficer() {
        return isDeliveryOfficer;
    }

    public void setDeliveryOfficer(boolean deliveryOfficer) {
        isDeliveryOfficer = deliveryOfficer;
    }

    /**
     * Gets all events currently assigned to the delivery officer and return them in a list.
     *
     * @return an ArrayList of Event objects assigned to the delivery officer.
     */
    private ArrayList<TechnocampsEvent> getAllEvents() {
        ArrayList<TechnocampsEvent> listEvents = new ArrayList<>();
        for (TechnocampsEvent event : TechnocampsEvent.getAllEvents()) {
            for (Staff staff : event.getListOfStaff()) {
                String officerName = staff.getName().toUpperCase();
                if (officerName.equals(name.toUpperCase())) {
                    listEvents.add(event);
                }
            }
        }
        return listEvents;
    }

    /**
     * Gets all events currently assigned to the delivery officer after a specified date and returns
     * them in a list. If no dates are provided, start date is set to the startDate specified in Main class and an open
     * end date is set.
     *
     * @param name      String name of delivery officer.
     * @param startDate String of start date in (dd/mm/yyyy) format.
     * @return an ArrayList of Event objects assigned to the delivery officer.
     * @throws ParseException if dates are incorrectly formatted.
     */
    private ArrayList<TechnocampsEvent> getAllEvents(String name, String startDate)
            throws ParseException {
        Date firstSearchDate = Validation.convertStringToDate(startDate);
        ArrayList<TechnocampsEvent> listEvents = new ArrayList<>();
        for (TechnocampsEvent event : TechnocampsEvent.getAllEvents()) {
            String eventDate = event.getDateOfEvent().toStringRfc3339();
            Date eventDateFormatted = Validation.convertDateTimeToUKFormat(eventDate);
            for (Staff staff : event.getListOfStaff()) {
                String officerName = staff.getName().toUpperCase();
                if (officerName.equals(name.toUpperCase())) {

                    if (firstSearchDate.compareTo(eventDateFormatted) <= 0) {
                        listEvents.add(event);
                    }
                }
            }
        }
        return listEvents;
    }

    /**
     * Gets all events currently assigned to the delivery officer between two specified dates and returns
     * them in a list. If no dates are provided, start date is set to the startDate specified in Main class and an open
     * end date is set.
     *
     * @param name      String name of delivery officer.
     * @param startDate String of start date in (dd/mm/yyyy) format.
     * @param endDate   String of end date in (dd/mm/yyyy) format.
     * @return an ArrayList of Event objects assigned to the delivery officer.
     * @throws ParseException if dates are incorrectly formatted.
     */
    private ArrayList<TechnocampsEvent> getAllEvents(String name, String startDate, String endDate)
            throws ParseException {
        Date firstSearchDate = Validation.convertStringToDate(startDate);
        Date secondSearchDate = Validation.convertStringToDate(endDate);
        ArrayList<TechnocampsEvent> listEvents = new ArrayList<>();
        for (TechnocampsEvent event : TechnocampsEvent.getAllEvents()) {
            String eventDate = event.getDateOfEvent().toStringRfc3339();
            Date eventDateFormatted = Validation.convertDateTimeToUKFormat(eventDate);
            for (Staff staff : event.getListOfStaff()) {
                String officerName = staff.getName().toUpperCase();
                if (officerName.equals(name.toUpperCase())) {
                    if (firstSearchDate.compareTo(eventDateFormatted) <= 0 &&
                            secondSearchDate.compareTo(eventDateFormatted) >= 0) {
                        listEvents.add(event);
                    }
                }
            }
        }
        return listEvents;
    }

    /**
     * Prints information of all the teaching commitments assigned to the delivery officer after a given date.
     *
     * @param name      The name of the delivery officer specified.
     * @param startDate String of start date in (dd/mm/yyyy) format.
     * @throws ParseException if dates are incorrectly formatted.
     */
    private void printOfficerEvents(String name, String startDate) throws ParseException {
        int numberEvents = getOfficerNumberOfEvents(startDate);
        System.out.println("Delivery Officer: " + getName() + " | Number of Events: " + numberEvents);
        if (getAllEvents().size() == 0) {
            System.out.println("No Events assigned.");
        } else {
            for (TechnocampsEvent event : getAllEvents(name, startDate)) {
                String eventType = event.getEventType();
                if (eventType.equals("Workshop") || eventType.equals("Technoclub") || eventType.equals("Technoteach")) {
                    event.printOfficerEventDetails();
                }
            }
        }
    }

    /**
     * Prints information of all the teaching commitments assigned to the delivery officer between two given dates.
     *
     * @param name      The name of the delivery officer specified.
     * @param startDate String of start date in (dd/mm/yyyy) format.
     * @param endDate   String of end date in (dd/mm/yyyy) format.
     * @throws ParseException if dates are incorrectly formatted.
     */
    private void printOfficerEvents(String name, String startDate, String endDate) throws ParseException {
        int numberEvents = getOfficerNumberOfEvents(startDate, endDate);
        System.out.println("Delivery Officer: " + getName() + " | Number of Events: " + numberEvents);
        if (getAllEvents().size() == 0) {
            System.out.println("No Events assigned.");
        } else {
            for (TechnocampsEvent event : getAllEvents(name, startDate, endDate)) {
                String eventType = event.getEventType();
                if (eventType.equals("Workshop") || eventType.equals("Technoclub") || eventType.equals("Technoteach")) {
                    event.printOfficerEventDetails();
                }
            }
        }
    }

    /**
     * Calculates the average number of all events for those delivery officers who have 100% time commitment to
     * Technocamps.
     *
     * @return a double containing the average number of events for 100% delivery officers.
     * @throws ParseException if date entered isn't formatted correctly.
     */
    private double averageOfficerNumberOfEvents() throws ParseException {
        double totalNumberOfEvents = 0;
        double currentNumberOfEvents;
        double counter = 0;
        String startDate = Validation.convertDateTimeToUKFormat(Main.getStartingDate());

        for (Staff staff : getAllStaffList()) {
            if (staff.isDeliveryOfficer()) {
                if (staff.getTimeCommitment() == 100) {
                    currentNumberOfEvents = staff.getOfficerNumberOfEvents(startDate);
                    totalNumberOfEvents = totalNumberOfEvents + currentNumberOfEvents;
                    counter++;
                }
            }
        }
        return totalNumberOfEvents / counter;
    }

    /**
     * Calculates the average number of events after a given date for those delivery officers who have 100% time
     * commitment to Technocamps.
     *
     * @param startDate a String in format (dd/MM/yyyy) for the start search date.
     * @return a double containing the average number of events for 100% delivery officers.
     * @throws ParseException if date entered isn't formatted correctly.
     */
    private double averageOfficerNumberOfEvents(String startDate) throws ParseException {
        double totalNumberOfEvents = 0;
        double currentNumberOfEvents;
        double counter = 0;
        for (Staff staff : getAllStaffList()) {
            if (staff.isDeliveryOfficer()) {
                if (staff.getTimeCommitment() == 100) {
                    currentNumberOfEvents = staff.getOfficerNumberOfEvents(startDate);
                    totalNumberOfEvents = totalNumberOfEvents + currentNumberOfEvents;
                    counter++;
                }
            }
        }
        return totalNumberOfEvents / counter;
    }

    /**
     * Calculates the average number of events for those delivery officers who have 100% time commitment to
     * Technocamps, between two given dates.
     *
     * @param startDate a String in format (dd/MM/yyyy) for the start search date.
     * @param endDate   a String in format (dd/MM/yyyy) for the end search date.
     * @return a double containing the average number of events for 100% delivery officers.
     * @throws ParseException if date entered isn't formatted correctly.
     */
    private double averageOfficerNumberOfEvents(String startDate, String endDate) throws ParseException {
        double totalNumberOfEvents = 0;
        double currentNumberOfEvents;
        double counter = 0;
        if (startDate == null) {
            startDate = Validation.convertDateTimeToUKFormat(Main.getStartingDate());
        }
        for (Staff staff : getAllStaffList()) {
            if (staff.isDeliveryOfficer()) {
                if (staff.getTimeCommitment() == 100) {
                    currentNumberOfEvents = staff.getOfficerNumberOfEvents(startDate, endDate);
                    totalNumberOfEvents = totalNumberOfEvents + currentNumberOfEvents;
                    counter++;
                }
            }
        }
        return totalNumberOfEvents / counter;
    }

    /**
     * Prints the number of all events assigned to the delivery Officer.
     *
     * @throws ParseException if date entered isn't formatted correctly.
     */
    public void printOfficerNumberOfEvents() throws ParseException {
        System.out.printf("%-25s %-20s %-10s", getName(), getOfficerNumberOfEvents(),
                Math.round((averageOfficerNumberOfEvents() * (getTimeCommitment() / 100.0))));
    }

    /**
     * Prints the number of events after a given date, assigned to the delivery Officer.
     *
     * @param startDate a String of a date in format (dd/MM/yyyy).
     * @throws ParseException if date entered isn't formatted correctly.
     */
    public void printOfficerNumberOfEvents(String startDate) throws ParseException {
        System.out.printf("%-25s %-20s %-10s", getName(), getOfficerNumberOfEvents(startDate),
                Math.round((averageOfficerNumberOfEvents(startDate) * (getTimeCommitment() / 100.0))));
    }

    /**
     * Prints the number of events assigned to the delivery Officer, between two specific dates.
     *
     * @param startDate a String of a date in format (dd/MM/yyyy).
     * @param endDate   a String of a date in format (dd/MM/yyyy).
     * @throws ParseException if date entered isn't formatted correctly.
     */
    public void printOfficerNumberOfEvents(String startDate, String endDate) throws ParseException {
        System.out.printf("%-25s %-20s %-10s", getName(), getOfficerNumberOfEvents(startDate, endDate),
                Math.round((averageOfficerNumberOfEvents(startDate, endDate) * (getTimeCommitment() / 100.0))));
    }

    /**
     * Displays the name of the delivery officer.
     */
    public void displayName() {
        System.out.println(getName());
    }

    /**
     * Gets the officer number of all events since the calendar starting date.
     *
     * @return an int of the number of events for the delivery officer.
     * @throws ParseException when date entered isn't formatted correctly.
     */
    private int getOfficerNumberOfEvents() {
        double counter = 0.0;
        for (TechnocampsEvent event : TechnocampsEvent.getAllEvents()) {
            for (Staff staff : event.getListOfStaff()) {
                String officerName = staff.getName().toUpperCase();
                if (officerName.equals(name.toUpperCase())) {
                    String eventType = event.getEventType();
                    if (eventType.equals("Workshop") || eventType.equals("Technoteach")) {
                        counter++;
                    } else if (eventType.equals("Technoclub")) {
                        counter = counter + 0.5;
                    }
                }
            }
        }
        return (int) Math.round(counter);
    }

    /**
     * Gets the officer number of future events after a given start date.
     *
     * @param startDate a String in format (dd/MM/yyyy) for the start search date.
     * @return an int of the number of events for the delivery officer.
     * @throws ParseException when date entered isn't formatted correctly.
     */
    private int getOfficerNumberOfEvents(String startDate) throws ParseException {
        Date firstSearchDate = Validation.convertStringToDate(startDate);
        double counter = 0.0;
        for (TechnocampsEvent event : TechnocampsEvent.getAllEvents()) {
            Date eventDate = Validation.convertDateTimeToUKFormat(event.getDateOfEvent().toStringRfc3339());
            for (Staff staff : event.getListOfStaff()) {
                String officerName = staff.getName().toUpperCase();
                String eventType;
                if (officerName.equals(name.toUpperCase())) {
                    if (firstSearchDate.compareTo(eventDate) <= 0) {
                        eventType = event.getEventType();
                        if (eventType.equals("Workshop") || eventType.equals("Technoteach")) {
                            counter++;
                        } else if (eventType.equals("Technoclub")) {
                            counter = counter + 0.5;
                        }
                    }
                }
            }
        }
        return (int) Math.round(counter);
    }


    /**
     * Gets the officer number of events between two given dates.
     *
     * @param startDate a String in format (dd/MM/yyyy) for the start search date.
     * @param endDate   a String in format (dd/MM/yyyy) for the end search date.
     * @return an int of the number of events for the delivery officer.
     * @throws ParseException when date entered isn't formatted correctly.
     */
    private int getOfficerNumberOfEvents(String startDate, String endDate) throws ParseException {
        Date firstSearchDate = Validation.convertStringToDate(startDate);
        Date secondSearchDate = Validation.convertStringToDate(endDate);
        double counter = 0.0;
        for (TechnocampsEvent event : TechnocampsEvent.getAllEvents()) {
            Date eventDate = Validation.convertDateTimeToUKFormat(event.getDateOfEvent().toStringRfc3339());
            for (Staff staff : event.getListOfStaff()) {
                String officerName = staff.getName().toUpperCase();
                String eventType = event.getEventType();
                if (officerName.equals(name.toUpperCase())) {
                    if (firstSearchDate.compareTo(eventDate) <= 0 &&
                            secondSearchDate.compareTo(eventDate) >= 0) {
                        if (eventType.equals("Workshop") || eventType.equals("Technoteach")) {
                            counter++;
                        } else if (eventType.equals("Technoclub")) {
                            counter = counter + 0.5;
                        }
                    }
                }
            }
        }
        return (int) Math.round(counter);
    }

    /**
     * Getter for list of all staff.
     *
     * @return an ArrayList containing Delivery Officers.
     */
    public static ArrayList<Staff> getAllStaffList() {
        return allStaff;
    }

    /**
     * Prints all available Delivery Officers on the input date.
     *
     * @param date a String in the format dd/mm/yyyy
     */
    public static void displayAvailableOfficers(String date) throws ParseException {
        availableOfficers.clear();
        availableOfficers.addAll(Staff.getAllStaffList());
        for (TechnocampsEvent event : TechnocampsEvent.getAllEvents()) {
            DateTime eventDate = event.getDateOfEvent();
            if (Validation.convertDateTimeToUKFormat(eventDate).equals(date)) {
                for (Staff staff : event.getListOfStaff()) {
                    for (Staff availableOfficer : new ArrayList<>(availableOfficers)) {
                        if (staff.getName().equals(availableOfficer.getName())) {
                            availableOfficers.remove(availableOfficer);
                        }
                    }
                }
            }
        }
        System.out.println("Available Officers: | Events that Week | Events Compared to Average for -2 weeks + 2 weeks");
        for (Staff staff : availableOfficers) {
            if(staff.isDeliveryOfficer()) {
                System.out.printf("%-25s\t %-15s %20s", staff.getName(),
                        getNumberOfEventsForWeek(staff.getName(), date),
                        staff.getEventsComparedToAverageForMonth(staff.getName(), date));
                System.out.println();
            }
        }
        System.out.println();
        recommendOfficerForWorkshop(date);
    }

    /**
     * Populates the Staff list with delivery officers.
     *
     * @throws IOException if issues with reading from staff details text file.
     */
    public static void populateStaffList() throws IOException {
        File staffFile = new File("/Users/lukeclement/ownCloud/Luke_Clement/DA/Programming 2" +
                "/CalendarTesting/src/Staff.txt");
        Scanner readFile = new Scanner(staffFile);
        while (readFile.hasNextLine()) {
            ArrayList<String> line = new ArrayList<>(Arrays.asList(readFile.nextLine().split(",")));
            String name = line.get(0).trim();
            String email = line.get(1).trim();
            int teachingCommitment = Integer.parseInt(line.get(2).trim());
            boolean adminPrivileges = Boolean.parseBoolean(line.get(3).trim());
            boolean isDeliveryOfficer = Boolean.parseBoolean(line.get(4).trim());
            addNewStaffMember(name, email, teachingCommitment, adminPrivileges, isDeliveryOfficer);
        }
    }

    /**
     * Prints out a list of all current staff.
     */
    public static void printAllStaff() {
        System.out.println("Current Staff:");
        for (Staff staff : Staff.getAllStaffList()) {
            System.out.print(staff.getUniqueStaffNumber() + ")\t");
            staff.displayName();
        }
    }

    /**
     * Checks whether an input name matches any current staff members.
     *
     * @param name a String of a full name.
     * @return a boolean value of whether the name entered belongs to a current staff member.
     */
    public static boolean staffMemberExists(String name) {
        boolean officerFound = false;
        try {
            int numberEntered = Integer.parseInt(name);
            for (Staff staff : Staff.getAllStaffList()) {
                if (staff.getUniqueStaffNumber() == numberEntered) {
                    officerFound = true;
                    break;
                }
            }
            return officerFound;
        } catch (NumberFormatException e) {
            for (Staff deliveryofficer : Staff.getAllStaffList()) {
                if (deliveryofficer.getName().toUpperCase().equals(name.toUpperCase())) {
                    officerFound = true;
                    break;
                }
            }
            return officerFound;
        }
    }

    /**
     * Allows finding of a particular delivery officer and returns them.
     *
     * @param name String containing a name to search for dfelivery officer.
     * @return DeliveryOfficer object searched for.
     */
    public static Staff findStaffMember(String name) {
        try {
            int numberEntered = Integer.parseInt(name);
            Staff chosenOfficer = null;
            for (Staff staff : Staff.getAllStaffList()) {
                if (staff.getUniqueStaffNumber() == numberEntered) {
                    chosenOfficer = staff;
                    break;
                }
            }
            return chosenOfficer;

        } catch (NumberFormatException e) {
            Staff chosenOfficer = null;
            for (Staff staff : Staff.getAllStaffList()) {
                if (staff.getName().toUpperCase().equals(name.toUpperCase())) {
                    chosenOfficer = staff;
                    break;
                }
            }
            return chosenOfficer;
        }
    }

    /**
     * Adds staff members to the list of all staff and to the external staff details text file.
     *
     * @param fullName        a String containing a staff member's full name.
     * @param email           a String containing a staff member's email address.
     * @param timeCommitment  an int of the percentage of time the staff member has allocated to Technocamps.
     * @param adminPrivileges a boolean containing staff member's admin privileges.
     * @throws IOException if issues with writing to staff details text file.
     */
    public static void addNewStaffMember(String fullName, String email, int timeCommitment, boolean adminPrivileges, boolean isDeliveryOfficer) throws IOException {
        Staff.getAllStaffList().add(new Staff(fullName, email, timeCommitment, adminPrivileges, isDeliveryOfficer));
        allStaff.sort(Comparator.comparing(Staff::getName)); //Sorts Delivery Officers by name.
        staffNumber = 1;
        for (Staff officer : allStaff) {
            officer.setUniqueStaffNumber(staffNumber);
            staffNumber++;
        }
        updateStaffInformation();
    }

    /**
     * Removes staff members from the list of staff and the external staff details text file.
     *
     * @param officerToDelete A DeliveryOfficer object to be removed.
     * @throws IOException if issues with writing to staff details text file.
     */
    public static void removeStaffMember(Staff officerToDelete) throws IOException {
        Staff.getAllStaffList().remove(officerToDelete);
        allStaff.sort(Comparator.comparing(Staff::getName)); //Sorts Delivery Officers by name.
        staffNumber = 1;
        for (Staff officer : allStaff) {
            officer.setUniqueStaffNumber(staffNumber);
            staffNumber++;
        }
        updateStaffInformation();
    }

    /**
     * Prints a list of all future events, for a named Officer, after a given date.
     *
     * @param name      the name of a delivery Officer.
     * @param startDate a String in the format (dd/mm/yyyy)
     * @throws ParseException if dates are incorrectly formatted.
     */
    public static void printStaffEventLists(String name, String startDate) throws ParseException {
        for (Staff staff : Staff.getAllStaffList()) {
            if (staff.getName().toUpperCase().equals(name.toUpperCase())) {
                staff.printOfficerEvents(name, startDate);
                break;
            }
        }
    }


    /**
     * Prints a list of all events for a named Officer between two given dates.
     *
     * @param name      the name of a delivery Officer.
     * @param startDate a String in the format (dd/mm/yyyy)
     * @param endDate   a String in the format (dd/mm/yyyy)
     * @throws ParseException if dates are incorrectly formatted.
     */
    public static void printStaffEventLists(String name, String startDate, String endDate) throws ParseException {
        for (Staff staff : Staff.getAllStaffList()) {
            if (staff.getName().toUpperCase().equals(name.toUpperCase())) {
                staff.printOfficerEvents(name, startDate, endDate);
                break;
            }
        }
    }

    /**
     * Prints the number of all events for all staff members.
     *
     * @throws ParseException if date is formatted incorrectly.
     */
    public static void printStaffNumberOfEvents() throws ParseException {
        String startDate = Validation.convertDateTimeToUKFormat(Main.getStartingDate());
        System.out.println("All past and future Events Since: " + startDate);
        System.out.println("Delivery Officer  |  Number Of Events  | Recommended number of events for period:");
        for (Staff staff : Staff.getAllStaffList()) {
            if (staff.isDeliveryOfficer()) {
                staff.printOfficerNumberOfEvents();
                System.out.println();
            }
        }
    }

    /**
     * Prints the number of events for all staff members from a given date.
     *
     * @param startDate starting date to search from.
     * @throws ParseException if date is formatted incorrectly.
     */
    public static void printStaffNumberOfEvents(String startDate) throws ParseException {
        System.out.println("Future Events from " + startDate);
        System.out.println("Delivery Officer  |  Number Of Events  | Recommended number of events for period:");
        for (Staff staff : Staff.getAllStaffList()) {
            if (staff.isDeliveryOfficer()) {
                staff.printOfficerNumberOfEvents(startDate);
                System.out.println();
            }
        }
    }

    /**
     * Prints the number of events for each individual staff member between two given dates.
     *
     * @param startDate a String in the format (dd/mm/yyyy)
     * @param endDate   a String in the format (dd/mm/yyyy)
     * @throws ParseException if dates are incorrectly formatted.
     */
    public static void printStaffNumberOfEvents(String startDate, String endDate) throws ParseException {
        System.out.println("Events Between: " + startDate + " - " + endDate);
        System.out.println("Delivery Officer  |  Number Of Events  | Recommended number of events for period:");
        for (Staff staff : Staff.getAllStaffList()) {
            if (staff.isDeliveryOfficer()) {
                staff.printOfficerNumberOfEvents(startDate, endDate);
                System.out.println();
            }
        }
    }

    /**
     * Updates the external staff information text file.
     *
     * @throws IOException if issues with writing to external staff details text file.
     */
    public static void updateStaffInformation() throws IOException {
        FileWriter outputToFile = new FileWriter
                ("/Users/lukeclement/ownCloud/Luke_Clement/DA/Programming 2/CalendarTesting/src/Staff.txt");
        int counter = 0;
        for (Staff officer : allStaff) {
            outputToFile.write(officer.getName() + ", " + officer.getEmail() + ", " + officer.getTimeCommitment()
                    + ", " + officer.getAdminPrivileges() + ", " + officer.isDeliveryOfficer());
            counter++;
            if (counter != allStaff.size()) {
                outputToFile.write("\n");
            }
        }
        outputToFile.close();
    }

    /**
     * Counts the amount of staff members with admin privileges.
     *
     * @return number of admins.
     */
    public static int getAdministratorsCount() {
        int counter = 0;
        for (Staff staff : allStaff) {
            if (staff.getAdminPrivileges()) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Gets the number of teaching commitments for the delivery Officer that week.
     *
     * @param name name of Delivery Officer.
     * @param date date in the format (dd/mm/yyyy).
     * @return number of events for the delivery officer that week.
     * @throws ParseException if date is formatted incorrectly.
     */
    public static int getNumberOfEventsForWeek(String name, String date) throws ParseException {
        if (Validation.isValidDate(date)) {
            String formattedDate = date.substring(6) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2);
            LocalDate dateOfWeek = LocalDate.parse(formattedDate);
            LocalDate monday = dateOfWeek.with(previousOrSame(DayOfWeek.MONDAY));
            LocalDate sunday = dateOfWeek.with(nextOrSame(DayOfWeek.SUNDAY));
            String mondayDate = Validation.convertLocalDateToUKFormat(monday);
            String sundayDate = Validation.convertLocalDateToUKFormat(sunday);
            return findStaffMember(name).getOfficerNumberOfEvents(mondayDate, sundayDate);
        } else {
            throw new ParseException("Invalid Date", 1);
        }
    }

    /**
     * Gets the number of teaching commitments for the delivery Officer that week.
     *
     * @param d Delivery Officer object.
     * @param date date in the format (dd/mm/yyyy).
     * @return number of events for the delivery officer that week.
     * @throws ParseException if date is formatted incorrectly.
     */
    public static int getNumberOfEventsForWeek(Staff d, String date) throws ParseException {
        if (Validation.isValidDate(date)) {
            String formattedDate = date.substring(6) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2);
            LocalDate dateOfWeek = LocalDate.parse(formattedDate);
            LocalDate monday = dateOfWeek.with(previousOrSame(DayOfWeek.MONDAY));
            LocalDate sunday = dateOfWeek.with(nextOrSame(DayOfWeek.SUNDAY));
            String mondayDate = Validation.convertLocalDateToUKFormat(monday);
            String sundayDate = Validation.convertLocalDateToUKFormat(sunday);
            return d.getOfficerNumberOfEvents(mondayDate, sundayDate);
        } else {
            throw new ParseException("Invalid Date", 1);
        }
    }

    /**
     * Compares the delivery officer's number of events for the surrounding month of the given date, and the average
     * events of 100% delivery officers for the surrounding month. Returns a value based off the comparison which
     * is scaled based of the time commitment of the delivery officer.
     *
     * @param name name of delivery officer.
     * @param date date in the format (dd/mm/yyyy).
     * @return a value comparing number of events for the week
     * @throws ParseException if date is formatted incorrectly.
     */
    public double getEventsComparedToAverageForMonth(String name, String date) throws ParseException {
        double comparedToAverage = 0.0;
        if (Validation.isValidDate(date)) {
            String formattedDate = date.substring(6) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2);
            LocalDate dateOfWeek = LocalDate.parse(formattedDate);
            LocalDate monday = null;
            LocalDate sunday = null;
            for (int i = 0; i < 2; i++) {                                       //Retrieves date of Monday 2 weeks prior
                monday = dateOfWeek.with(previous(DayOfWeek.MONDAY));
            }
            for (int i = 0; i < 2; i++) {                                       //Retrieves date of Sunday 2 weeks after
                sunday = dateOfWeek.with(next(DayOfWeek.SUNDAY));
            }
            if (monday != null && sunday != null) {
                String mondayDate = Validation.convertLocalDateToUKFormat(monday);
                String sundayDate = Validation.convertLocalDateToUKFormat(sunday);
                double average = (averageOfficerNumberOfEvents(mondayDate, sundayDate)) * (getTimeCommitment()/100.0);
                comparedToAverage = findStaffMember(name).getOfficerNumberOfEvents(mondayDate, sundayDate) - average;

            } else {
                System.out.println("Date is null.");
            }
        } else {
            throw new ParseException("Invalid Date", 1);
        }
        return comparedToAverage;
    }

    /**
     * Compares the delivery officer's number of events for the surrounding month of the given date, and the average
     * events of 100% delivery officers for the surrounding month. Returns a value based off the comparison which
     * is scaled based of the time commitment of the delivery officer.
     *
     * @param s a Staff object.
     * @param date date in the format (dd/mm/yyyy).
     * @return a value comparing number of events for the week
     * @throws ParseException if date is formatted incorrectly.
     */
    public double getEventsComparedToAverageForMonth(Staff s, String date) throws ParseException {
        double comparedToAverage = 0.0;
        if (Validation.isValidDate(date)) {
            String formattedDate = date.substring(6) + "-" + date.substring(3, 5) + "-" + date.substring(0, 2);
            LocalDate dateOfWeek = LocalDate.parse(formattedDate);
            LocalDate monday = null;
            LocalDate sunday = null;
            for (int i = 0; i < 2; i++) {                                       //Retrieves date of Monday 2 weeks prior
                monday = dateOfWeek.with(previous(DayOfWeek.MONDAY));
            }
            for (int i = 0; i < 2; i++) {                                       //Retrieves date of Sunday 2 weeks after
                sunday = dateOfWeek.with(next(DayOfWeek.SUNDAY));
            }
            if (monday != null && sunday != null) {
                String mondayDate = Validation.convertLocalDateToUKFormat(monday);
                String sundayDate = Validation.convertLocalDateToUKFormat(sunday);
                double average = (averageOfficerNumberOfEvents(mondayDate, sundayDate)) * (getTimeCommitment()/100.0);
                comparedToAverage = s.getOfficerNumberOfEvents(mondayDate, sundayDate) - average;

            } else {
                System.out.println("Date is null.");
            }
        } else {
            throw new ParseException("Invalid Date", 1);
        }
        return comparedToAverage;
    }

    /**
     * Recommends a deliveryOfficer for a workshop based on the number of events that week. If two or more delivery
     * Officers have the same amount of events that week, then their monthly number compared to the average is
     * considered.
     *
     * @param date date in the format (dd/mm/yyyy).
     * @throws ParseException if date incorrectly formatted.
     */
    public static void recommendOfficerForWorkshop(String date) throws ParseException {
        Staff leastEvents = getAllStaffList().get(0);
        double currentEvents;
        double smallest = getAllStaffList().get(0).getOfficerNumberOfEvents(date);
        for (Staff s : allStaff) {
            if (s.isDeliveryOfficer()) {
                currentEvents = getNumberOfEventsForWeek(s, date);
                if (smallest > currentEvents) {
                    smallest = currentEvents;
                    leastEvents = s;
                } else if (smallest == currentEvents) {
                    if (s.getEventsComparedToAverageForMonth(s, date) < leastEvents.getEventsComparedToAverageForMonth(s, date))
                        leastEvents = s;
                }
            }
        }
        System.out.println("Recommended Officer | Events that Week | Compared to Average for Surrounding Month");
        System.out.printf("%-25s\t %-15s %20s", leastEvents.getName(),
                getNumberOfEventsForWeek(leastEvents.getName(), date),
                leastEvents.getEventsComparedToAverageForMonth(leastEvents.getName(), date));
    }
}

