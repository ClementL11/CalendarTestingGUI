import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Main {

    private static final DateTime startingDate = new DateTime("2019-09-01T08:00:00+00:00");
    private static final String calendarEmail = "shared@technocamps.com";

    /**
     * Getter for the startingDate to set the earliest date calendar entries are loaded from.
     *
     * @return DateTime in the standard Google API form.
     */
    public static DateTime getStartingDate() {
        return startingDate;
    }

    /**
     * Main method of the program. It ensures the staff list is populated and all events are read in from
     * the API before opening the user interface.
     *
     * @param args string arguments used when given through the console.
     * @throws IOException              if text file of Staff member details does not exist.
     * @throws GeneralSecurityException if issue with Google Calendar API credentials.
     */
    public static void main(String... args) throws IOException, GeneralSecurityException {
        //test();

        Staff.populateStaffList();
        CalendarQuickstart.getEventsFromCalendar(startingDate, calendarEmail);
        for (Staff staff : Staff.getAllStaffList()) {
            CalendarQuickstart.getIndividualUnavailability(getStartingDate(), staff, staff.getEmail());
        }
        System.out.println();
        userInterface();
    }

    /**
     * Opens the user interface for the program. It allows a user to login and runs the main menu based on
     * their admin credentials.
     */
    public static void userInterface() {
        System.out.println("Welcome to the Technocamps Booking System. Please select your name:\n\n");
        boolean memberExists = true;
        do {
            Staff user = Validation.logIn();
            if (user == null) {
                memberExists = false;
                System.out.println("\nNot a valid choice!\n");
            } else {
                    mainMenu(user, user.getAdminPrivileges());

            }
        } while (!memberExists);

    }

    /**
     * This method is used for testing individual methods when needed.
     */
    private static void test() throws IOException {
        Staff.populateStaffList();

        userInterface();

        for (TechnocampsEvent event : TechnocampsEvent.getAllEvents()) {
            System.out.println(event.toString());
        }


    }

    /**
     * Main menu method providing a structure for a simple console-based user interface.
     *
     * @param user The current Delivery Officer logged in to the system.
     * @param adminPrivileges A boolean showing whether the User is an admin or not.
     */
    private static void mainMenu(Staff user, boolean adminPrivileges) {

        try {

            System.out.print("*******************************************************************\n" +
                    "1) View All Events from Shared Calendar \n2) View Individual Staff Events\n" +
                    "3) Find Available Delivery Officers for Specific Date\n" +
                    "4) Find Number of Events of All Staff\n");
            if (adminPrivileges) {
                System.out.print("5) Edit Staff Details\n");
                System.out.print("6) Add New/Remove Staff Member\n");
            }
            System.out.println("X) Log Out\n*******************************************************************");
            System.out.println("Please select an option: ");

            Scanner in = new Scanner(System.in);
            String mainMenuResponse = in.nextLine().trim().toLowerCase();
            switch (mainMenuResponse) {
                case "1":
                    viewEventsFromSharedCalendar(in);
                    break;
                case "2":
                    viewIndividualStaffEvents(user, in, adminPrivileges);
                    break;
                case "3":
                    findAvailableDeliveryOfficersForDate(in);
                    break;
                case "4":
                    viewNumberOfEvents(in);
                    break;
                case "5":
                    if (adminPrivileges) {
                        editStaffDetails(user, in, adminPrivileges);
                    }
                    break;
                case "6":
                    if (adminPrivileges) {
                        addRemoveStaff(user, in);
                    }
                    break;
                case "x":
                    System.out.println("You have successfully logged out.");
                    userInterface();
            }
            System.out.println();
            mainMenu(user, adminPrivileges);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    /**
     * Menu option for searching the entire shared calendar of the delivery team. A choice to view all
     * events or only ones between input specific dates can be made.
     *
     * @param scanner this is a scanner that reads the system input previously used in the main menu method.
     * @throws ParseException if input dates are formatted incorrectly.
     */
    private static void viewEventsFromSharedCalendar(Scanner scanner) throws ParseException {
        String response;
        System.out.print("*******************************************************************\n" +
                "Would you like to: \n" +
                "1) View all Events from Shared Calendar\n" +
                "2) View all Future Events from Shared Calendar\n" +
                "3) View Events between given Dates\n" +
                "4) View Events in a Chosen Month\n" +
                "*******************************************************************\n");
        response = scanner.nextLine().trim();
        switch (response) {
            case "1":
                System.out.println("\nAll Events: \n");
                TechnocampsEvent.printAllEvents();
                break;
            case "2":
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date todayDate = new Date();
                TechnocampsEvent.printAllEvents(dateFormat.format(todayDate));
                break;
            case "3":
                String startDate = null;
                String endDate = null;
                boolean validDate;
                do {
                    validDate = true;
                    System.out.println("Please enter a start date or press enter to return to main menu");
                    String inputDate = scanner.nextLine().trim();
                    if (inputDate.equals("")) {
                        break;
                    } else {
                        try {
                            Validation.checkValidDate(inputDate);
                            startDate = inputDate;
                        } catch (ParseException e) {
                            System.out.println("Invalid Date Format! (To return to the main menu press Enter)");
                            validDate = false;
                        }
                    }
                } while (!validDate);
                if (!(startDate == null)) {
                    do {
                        validDate = true;
                        System.out.println("Please enter an end date or leave blank for open end date.");
                        String inputDate = scanner.nextLine().trim();
                        if (inputDate.equals("")) {
                            break;
                        } else {
                            try {
                                Validation.checkValidDate(inputDate);
                                endDate = inputDate;
                            } catch (ParseException e) {
                                System.out.println("Invalid Date Format! (To return to the main menu press Enter)");
                                validDate = false;
                            }
                        }
                    } while (!validDate);
                    if (endDate == null) {
                        TechnocampsEvent.printAllEvents(startDate);
                    } else {
                        TechnocampsEvent.printAllEvents(startDate, endDate);
                    }
                }
                break;
            case "4":
                ArrayList<String> dates = TechnocampsEvent.searchByMonth();
                String startDateMonth = dates.get(0);
                String endDateMonth = dates.get(1);
                TechnocampsEvent.printAllEvents(startDateMonth, endDateMonth);
                break;
            case "":
                break;
        }
    }

    /**
     * Menu option for viewing an individual delivery officer's events. A choice can be made to view
     * all of their events or only ones between input specific dates.
     *
     * @param user            The current Delivery Officer logged in to the system.
     * @param scanner         this is a scanner that reads the system input previously used in the main menu method.
     * @param adminPrivileges A boolean showing whether the User is an admin or not.
     * @throws ParseException if dates entered are incorrectly formatted.
     */
    public static void viewIndividualStaffEvents(Staff user, Scanner scanner, boolean adminPrivileges)
            throws ParseException {
        System.out.println();
        Staff.printAllStaff();
        System.out.println();
        String chosenStaffMemberName;
        Staff chosenStaffMember = null;
        boolean validName;
        do {
            validName = true;
            System.out.println("Please select a staff member:  (Leave blank to return to main menu)");
            chosenStaffMemberName = scanner.nextLine().trim();
            if (chosenStaffMemberName.equals("")) {
                mainMenu(user, adminPrivileges);
            } else if (!Staff.staffMemberExists(chosenStaffMemberName)) {
                System.out.println("No staff member by that name found!");
                validName = false;
            } else {
                chosenStaffMember = Staff.findStaffMember(chosenStaffMemberName);
            }
        } while (!validName);
        System.out.println("Chosen Officer: " + chosenStaffMember.getName());
        System.out.print("\n*******************************************************************\n" + "Would You " +
                "Like To\n" +
                "1) See All Events\n" +
                "2) See All Future Events\n" +
                "3) Choose Dates To Search Between\n" +
                "4) Search by Month"
                + "\n*******************************************************************\n");
        String response = scanner.nextLine().trim();
        System.out.println();
        switch (response) {
            case "1": {
                String startDate = Validation.convertDateTimeToUKFormat(getStartingDate());
                Staff.printStaffEventLists(chosenStaffMember.getName(), startDate);
                break;
            }
            case "2": {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date todayDate = new Date();
                Staff.printStaffEventLists(chosenStaffMember.getName(), dateFormat.format(todayDate));
                break;
            }
            case "3": {
                String startDate = null;
                String endDate = null;
                boolean validDate;
                do {
                    validDate = true;
                    System.out.println("Please enter a start date (leave blank to return to main menu)");
                    String inputDate = scanner.nextLine().trim();
                    if (inputDate.equals("")) {
                        break;
                    } else {
                        try {
                            Validation.checkValidDate(inputDate);
                            startDate = inputDate;
                        } catch (ParseException e) {
                            System.out.println("Invalid Date Format! (To return to the main menu press Enter)");
                            validDate = false;
                        }
                    }
                } while (!validDate);
                if (!(startDate == null)) {
                    do {
                        validDate = true;
                        System.out.println("Please enter an end date or leave blank for open end date");
                        String inputDate = scanner.nextLine().trim();
                        if (inputDate.equals("")) {
                            break;
                        } else {
                            try {
                                Validation.checkValidDate(inputDate);
                                endDate = inputDate;
                            } catch (ParseException e) {
                                System.out.println("Invalid Date Format! (To return to the main menu press Enter)");
                                validDate = false;
                            }
                        }
                    } while (!validDate);
                    if (endDate == null) {
                        Staff.printStaffEventLists(chosenStaffMember.getName(), startDate);
                    } else {
                        Staff.printStaffEventLists(chosenStaffMember.getName(), startDate, endDate);
                    }
                }

                break;
            }
            case "4":
                ArrayList<String> dates = TechnocampsEvent.searchByMonth();
                String startDate = dates.get(0);
                String endDate = dates.get(1);
                Staff.printStaffEventLists(chosenStaffMember.getName(), startDate, endDate);
                break;
            case "":
                break;
        }
    }

    /**
     * Menu option for finding available delivery officers on an input date.
     *
     * @param scanner this is a scanner that reads the system input previously used in the main menu method.
     */
    public static void findAvailableDeliveryOfficersForDate(Scanner scanner) {
        boolean validResponse;
        do {
            validResponse = true;
            System.out.println("Please enter a date (dd/mm/yyyy) to search for available officers: ");
            String response = scanner.nextLine().trim();
            if (response.equals("")) {
                break;
            } else {
                try {
                    Validation.checkValidDate(response);
                    Staff.displayAvailableOfficers(response);
                } catch (ParseException e) {
                    System.out.println("Invalid Date Format! (To return to the main menu press Enter)");
                    validResponse = false;
                }
            }
        } while (!validResponse);
    }

    /**
     * Allows a user to view numbers of events for all Delivery Officers, either total number of events
     * since the calendar starting point, future events from current date, or events between two dates. The end search
     * date can be left open to find future dates from as given start date.
     *
     * @param scanner this is a scanner that reads the system input previously used in the main menu method.
     * @throws ParseException if date is not formatted incorrectly
     */
    public static void viewNumberOfEvents(Scanner scanner) throws ParseException {
        System.out.print("\n*******************************************************************\n" + "Would you " +
                "like to\n" +
                "1) See Total Event Numbers \n" +
                "2) See Future Event Numbers\n" +
                "3) Choose Dates to Search Between\n" +
                "4) Search By Month\n" +
                "*******************************************************************\n");
        String response = scanner.nextLine().trim();
        System.out.println();
        switch (response) {
            case "1":
                Staff.printStaffNumberOfEvents();

                break;
            case "2":
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date todayDate = new Date();
                Staff.printStaffNumberOfEvents(dateFormat.format(todayDate));
                break;
            case "3":
                String startDate = null;
                String endDate = null;
                boolean validDate;
                do {
                    validDate = true;
                    System.out.println("Please enter a start date or press enter to return to main menu");
                    String inputDate = scanner.nextLine().trim();
                    if (inputDate.equals("")) {
                        break;
                    } else {
                        try {
                            Validation.checkValidDate(inputDate);
                            startDate = inputDate;
                        } catch (ParseException e) {
                            System.out.println("Invalid Date Format! (To return to the main menu press Enter)");
                            validDate = false;
                        }
                    }
                } while (!validDate);
                if (!(startDate == null)) {
                    do {
                        validDate = true;
                        System.out.println("Please enter an specific end date. Enter nothing for an open end date.");
                        String inputDate = scanner.nextLine().trim();
                        if (inputDate.equals("")) {
                            break;
                        } else {
                            try {
                                Validation.checkValidDate(inputDate);
                                endDate = inputDate;
                            } catch (ParseException e) {
                                System.out.println("Invalid Date Format! (To return to the main menu press Enter)");
                                validDate = false;
                            }
                        }
                    } while (!validDate);
                    if (endDate == null) {
                        Staff.printStaffNumberOfEvents(startDate);
                    } else {
                        Staff.printStaffNumberOfEvents(startDate, endDate);
                    }
                }
                break;
            case "4":
                ArrayList<String> dates = TechnocampsEvent.searchByMonth();
                startDate = dates.get(0);
                endDate = dates.get(1);
                Staff.printStaffNumberOfEvents(startDate, endDate);
                break;
        }
    }

    /**
     * Allows users with admin privileges to edit staff details. These changes are then reflected in the
     * text file where the details are stored. If the user is the only admin, they cannot remove their admin privileges.
     *
     * @param user            The current Delivery Officer logged in to the system.
     * @param scanner         this is a scanner that reads the system input previously used in the main menu method.
     * @param adminPrivileges A boolean showing whether the User is an admin or not.
     * @throws IOException if there is an error with the Staff text file the information is written to.
     */
    public static void editStaffDetails(Staff user, Scanner scanner, boolean adminPrivileges)
            throws IOException {
        Staff.printAllStaff();
        String chosenStaffMemberName;
        boolean validName;
        do {
            validName = true;
            System.out.println("Please select a staff member:  (Leave blank to return to main menu)");
            chosenStaffMemberName = scanner.nextLine().trim();
            if (chosenStaffMemberName.equals("")) {
                mainMenu(user, adminPrivileges);
            }
            if (!Staff.staffMemberExists(chosenStaffMemberName)) {
                System.out.println("No staff member by that name found!");
                validName = false;
            }
        } while (!validName);
        Staff chosenStaffMember = Staff.findStaffMember(chosenStaffMemberName);
        System.out.print("Chosen Staff Member: " + chosenStaffMember.getName());
        System.out.print("\n*******************************************************************\n" + "Would you " +
                "like to\n" +
                "1) Edit Name \n" +
                "2) Edit Email Address \n" +
                "3) Edit Time Commitment \n" +
                "4) Edit Admin Privileges " +
                "\n*******************************************************************\n");
        String response = scanner.nextLine().trim();
        System.out.println();
        switch (response) {
            case "1":
                System.out.println("Current Name: " + chosenStaffMember.getName());
                System.out.println("Please Enter A New Name: ");
                response = scanner.nextLine().trim();
                if (response.equals("")) {
                    System.out.println("No name entered, details have not been saved.");
                } else {
                    boolean confirmation = Validation.confirmChoice("New name: " + response);
                    if (confirmation) {
                        chosenStaffMember.setName(response);
                        System.out.println("The name has been successfully changed!");
                    } else {
                        System.out.println("Changes Cancelled.");
                    }
                }
                break;
            case "2":
                System.out.println("Current Email Address: " + chosenStaffMember.getEmail());
                System.out.println("Please Enter A New Email Address: ");
                response = scanner.nextLine().trim();
                if (!Validation.checkEmailAddress(response)) {
                    System.out.println("Invalid Email Address! Details have NOT been saved.");
                } else {
                    boolean confirmation = Validation.confirmChoice("New Email: " + response);
                    if (confirmation) {
                        chosenStaffMember.setEmail(response);
                        System.out.println("The email has been successfully changed!");
                    } else {
                        System.out.println("Changes Cancelled.");
                    }
                }
                break;
            case "3":
                System.out.println("Current Time Commitment: " + chosenStaffMember.getTimeCommitment());
                System.out.println("Please Enter A New Time Commitment: ");
                response = scanner.nextLine().trim();
                if (!Validation.checkTimeCommitment(response)) {
                    System.out.println("Details have NOT been saved.");
                } else {
                    boolean confirmation = Validation.confirmChoice("New Time Commitment: " + response);
                    if (confirmation) {
                        chosenStaffMember.setTimeCommitment(Integer.parseInt(response));
                        System.out.println("The time Commitment has been successfully changed!");
                    } else {
                        System.out.println("Changes Cancelled.");
                    }
                }
                break;
            case "4":
                if (user.equals(chosenStaffMember)) {
                    if (Staff.getAdministratorsCount() == 1) {
                        System.out.println("As you are the only admin, you cannot remove your privileges.");
                    }
                } else {
                    boolean currentPrivileges = chosenStaffMember.getAdminPrivileges();
                    System.out.println("Current Admin Privileges: " + currentPrivileges);
                    boolean confirmation = Validation.confirmChoice("New Admin Privileges: "
                            + !currentPrivileges);
                    if (confirmation) {
                        chosenStaffMember.setAdminPrivileges(!currentPrivileges);
                        System.out.println("The Admin Privilege for " + chosenStaffMember.getName() +
                                " has been successfully changed!");
                    } else {
                        System.out.println("Changes Cancelled.");
                    }
                }
                break;
        }
    }

    /**
     * Allows users with admin privileges to add or remove staff members. The changes are then reflected in
     * the text file where the staff details are stored. Admins are not allowed to remove themselves, but they can
     * remove other users with admin privileges.
     *
     * @param user    The current Delivery Officer logged in to the system.
     * @param scanner this is a scanner that reads the system input previously used in the main menu method.
     * @throws IOException if there is an error with the Staff text file the information is written to.
     */
    public static void addRemoveStaff(Staff user, Scanner scanner) throws IOException {
        System.out.println("\n*******************************************************************\n" + "Would you " +
                "like to\n" +
                "1) Add New Staff Member \n" +
                "2) Remove Staff Member \n" +
                "*******************************************************************\n");
        String response = scanner.nextLine().trim();
        switch (response) {
            case "1":
                String name = null;
                String email = null;
                int timeCommitment = 0;
                boolean adminPrivileges = false;
                boolean isDeliveryOfficer = false;
                boolean error = false;

                System.out.println("Enter Staff Member Name: ");
                response = scanner.nextLine().trim();
                if (response.equals("")) {
                    error = true;
                } else {
                    name = response;
                }
                if (!error) {
                    System.out.println("Enter Staff Technocamps Email Address: ");
                    response = scanner.nextLine().trim();
                    if (!Validation.checkEmailAddress(response)) {
                        error = true;
                    } else {
                        email = response;
                    }
                    if (!error) {
                        System.out.println("Time Commitment: ");
                        response = scanner.nextLine().trim();
                        if (!Validation.checkTimeCommitment(response)) {
                            error = true;
                        } else {
                            timeCommitment = Integer.parseInt(response);
                        }
                    }
                    if (!error) {
                        System.out.println("Admin Privileges (Y/N): ");
                        response = scanner.nextLine().trim().toUpperCase();
                        adminPrivileges = response.equals("Y");
                            System.out.println("Delivery Officer (Y/N): ");
                            response = scanner.nextLine().trim().toUpperCase();
                            isDeliveryOfficer = response.equals("Y");

                    }
                }
                System.out.println("\nStaff Member Details:\nName:\t\t\t\t" + name + "\nEmail:\t\t\t\t" + email +
                        "\nTime Commitment:\t" + timeCommitment + "\nAdmin:\t\t\t\t" + adminPrivileges);
                System.out.println("Are the details all correct? (Y/N): ");
                response = scanner.nextLine().trim().toUpperCase();
                if (response.equals("Y")) {
                    Staff.addNewStaffMember(name, email, timeCommitment, adminPrivileges, isDeliveryOfficer);
                    System.out.println("Staff Member Successfully Added");
                } else {
                    System.out.println("Details were not saved");
                }
                break;
            case "2":
                Staff.printAllStaff();
                System.out.println("Please select Delivery Officer to Remove:");
                String chosenName = scanner.nextLine().trim();
                if (Staff.staffMemberExists(chosenName)) {
                    Staff officerToDelete = Staff.findStaffMember(chosenName);

                    if (officerToDelete.equals(user)) {
                        System.out.println("You cannot delete yourself!");
                    } else {
                        Validation.confirmChoice("Delete " + officerToDelete.getName());
                        System.out.println(officerToDelete.getName() + " successfully removed.");
                        Staff.removeStaffMember(officerToDelete);
                    }
                    break;
                }
        }
    }

}
