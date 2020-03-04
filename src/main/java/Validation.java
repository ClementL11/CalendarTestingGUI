import com.google.api.client.util.DateTime;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class Validation {


    public static String convertLocalDateToUKFormat(LocalDate date) {
        return date.toString().substring(8) + "/"  + date.toString().substring(5, 7) + "/" +
                date.toString().substring(0, 4);
    }
    /**
     * Converts a Google DateTime formatted date into a UK style date in format dd/MM/yyyy.
     *
     * @param dateTime a Google API DateTime date.
     * @return a String of a date in format dd/MM/yyyy
     */
    public static String convertDateTimeToUKFormat(DateTime dateTime) {
        String eventDate = dateTime.toStringRfc3339();
        return eventDate.substring(8, 10) + "/" + eventDate.substring(5, 7) + "/" + eventDate.substring(0, 4);
    }

    /**
     * Converts a String version of a Google DateTime into a UK style date in format dd/MM/yyyy.
     *
     * @param dateTime a String version of a Google API DateTime date.
     * @return a String of a date in format dd/MM/yyyy
     * @throws ParseException if date incorrectly formatted.
     */
    public static Date convertDateTimeToUKFormat(String dateTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.parse(dateTime.substring(8, 10) + "/" + dateTime.substring(5, 7) + "/" + dateTime.substring(0, 4));
    }

    /**
     * Converts a String version of a UK date into a date object.
     *
     * @param stringDate String version of a UK date in format dd/MM/yyyy
     * @return a date object in format dd/MM/yyyy
     * @throws ParseException if stringDate incorrectly formatted.
     */
    public static Date convertStringToDate(String stringDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.parse(stringDate);

    }

    /**
     * Checks if an input date is valid. If not it throws an exception.
     *
     * @param date a String of a date which should be as dd/MM/yyyy
     * @throws ParseException if date incorrectly formatted.
     */
    public static void checkValidDate(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        format.parse(date);
    }

    /**
     * Checks if an input date is valid. If not it throws an exception.
     *
     * @param date a String of a date which should be as dd/MM/yyyy
     * @throws ParseException if date incorrectly formatted.
     */
    public static boolean isValidDate(String date) throws ParseException {
        try {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            format.setLenient(false);
            format.parse(date);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    /**
     * Checks an email address is valid by checking it isn't too long, that it contains an "@" and a ".".
     *
     * @param email input email address.
     * @return boolean value, true if valid, false if invalid email.
     */
    public static boolean checkEmailAddress(String email) {
        boolean validEmail = true;
        if (email.equals("")) {
            validEmail = false;
        } else if (email.split("@").length < 2) {
            System.out.println("1");
            validEmail = false;
        } else if (email.split("@")[0].length() > 64) {
            System.out.println("2");
            validEmail = false;
        }
        else if (email.split("\\.").length < 2) {
            System.out.println("3");
            validEmail = false;
        }
        return validEmail;
    }

    /**
     * Checks whether a time commitment is valid or not. It must be and integer between 0 and a 100.
     *
     * @param timeCommitment an input String which should be an integer between 0 and 100.
     * @return boolean, true if the time commitment is valid, false if not.
     */
    public static boolean checkTimeCommitment(String timeCommitment) {
        boolean validCommitment = true;
        try {
            int commitment = Integer.parseInt(timeCommitment);
            if (commitment < 0 || commitment > 100) {
                System.out.println("Commitment cannot be less than 0 or more than 100!");
                validCommitment = false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Not a valid integer between 0 and 100");
            validCommitment = false;
        }
        return validCommitment;
    }

    /**
     * Prompts user to confirm changes requested by showing relevant information and allowing final confirmation.
     *
     * @param dataToCheck the relevant information to check for confirmation.
     * @return boolean, true if confirmed, false if not.
     */
    public static boolean confirmChoice(String dataToCheck) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you sure you wish to keep these changes: " + dataToCheck + " (Y/N)");
        return scanner.nextLine().trim().toUpperCase().equals("Y");
    }

    /**
     * Log in menu for users of the user interface. Some users have admin privileges when logged in to the system.
     *
     * @return delivery officer who has logged in successfully or if not, returns null.
     */
    public static Staff logIn(){
        Scanner scanner = new Scanner(System.in);
        Staff.printAllStaff();
        System.out.println("X)\tQuit");
        System.out.println("Please select your name:");
        String chosenName = scanner.nextLine().trim();
        if (chosenName.toUpperCase().equals("X")) {
            System.out.println("Thanks for using our System. Shutting Down.");
            System.exit(0);
        } else if (Staff.staffMemberExists(chosenName)) {
            Staff staffMember = Staff.findStaffMember(chosenName);

            System.out.println("\nWelcome " + staffMember.getName() + "!");
            return staffMember;
        }
        return null;
    }
}
