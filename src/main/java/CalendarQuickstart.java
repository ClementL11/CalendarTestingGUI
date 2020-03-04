import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class CalendarQuickstart {


    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    /**
     * This method loads events from the Google Calendar API using a starting
     * when accessed from the Google Calendar API.
     *
     * @throws IOException if error with Google Calendar API.
     * @throws GeneralSecurityException if error with Google Calendar credentials.
     */
    public static void getEventsFromCalendar(DateTime startingDate, String calendarEmail) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

        Events events = service.events().list(calendarEmail)
                .setMaxResults(2500)
                .setTimeMin(startingDate)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        for (Event event : items) {
            String eventSummary = event.getSummary();
            String uniqueId = event.getId();
            DateTime dateOfEvent = event.getStart().getDateTime();
            if (dateOfEvent != null) {     //This ignores all-day events
                DateTime startTime = event.getStart().getDateTime();
                DateTime endTime = event.getEnd().getDateTime();
                try {
                    ArrayList<String> eventSummaryList = new ArrayList<>(Arrays.asList(event.getSummary().split(" - ")));
                    if (eventSummaryList.size() == 4) {
                        eventSummary = eventSummary + " - ?";
                    } else if (eventSummaryList.size() == 3) {
                        eventSummary = eventSummary + " - ? - ?";

                    } else if (eventSummaryList.size() == 2) {
                        eventSummary = eventSummary + " - ? - ? - ?";
                    }
                    ArrayList<String> eventName = new ArrayList<>(Arrays.asList(eventSummary.split(" - ")));
                    String universityCode = eventName.get(0);
                    if (universityCode.equals("SU")) {
                        String eventType = eventName.get(1);
                        String schoolName = eventName.get(2);
                        String keyStage = eventName.get(3);
                        String workshop = eventName.get(4);
                        ArrayList<EventAttendee> deliveryNames = (ArrayList<EventAttendee>) event.getAttendees();
                        ArrayList<Staff> eventStaffs = new ArrayList<>();
                        if (deliveryNames.size() > 0) {
                            for (EventAttendee attendee : deliveryNames) {
                                for (Staff staffMember : Staff.getAllStaffList())
                                    if (attendee.getEmail().trim().equals(staffMember.getEmail())) {
                                        eventStaffs.add(staffMember);
                                    }
                            }
                        }
                        TechnocampsEvent.getAllEvents().add(new TechnocampsEvent(uniqueId, dateOfEvent, startTime,
                                endTime, universityCode, eventType, schoolName, keyStage, workshop,
                                eventStaffs));
                    }
                } catch (Exception exception) {
                    System.out.println("Poorly formatted event in Calendar ignored: " + eventSummary);
                }
            }
        }
    }

    /**
     * This method is included as I'm not sure how to run gradle beginning with the Main.main() method.
     *
     * @param args string arguments for use through console.
     * @throws IOException if issues reading and writing from text files.
     * @throws GeneralSecurityException if issues with Google API credentials.
     */
    public static void main(String[] args) throws IOException, GeneralSecurityException, ParseException {
        Main.main();
    }

}
