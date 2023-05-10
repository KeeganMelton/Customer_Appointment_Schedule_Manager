/**
 * @auther Keegan Melton
 */
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.DatabaseInfo;
import utilities.Helper;
import utilities.SQL_Queries;
import utilities.TimeZones;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 *  UpdateAppointmentController - provides functionality to
 *  the "UpdateAppointments.fxml" screen
 */
public class UpdateAppointmentController implements Initializable {
    public TextField AppointmentIDTF;
    public TextField TitleTF;
    public TextField DescriptionTF;
    public TextField LocationTF;
    public ComboBox<Contact> ContactCB;
    public ComboBox<String> TypeCB;
    public DatePicker StartDatePick;
    public DatePicker EndDatePick;
    public ComboBox<Customer> CustomerCB;
    public TextField TypeOtherTF;
    public ComboBox<LocalTime> StartCB;
    public ComboBox<LocalTime> EndCB;
    public Button UpdateAppointmentButton;
    public Button CancelButton;

    public LocalTime start = LocalTime.of(0,0);
    public LocalTime end = LocalTime.of(23,45);

    public String selectedType;
    public Contact selectedContact;
    public Customer selectedCustomer;
    public LocalDate selectedStartDate;
    public LocalDate selectedEndDate;
    public LocalTime selectedStartTime;
    public LocalTime selectedEndTime;

    private ObservableList<String> types = FXCollections.observableArrayList();

    private static String userLoggedIn;
    private static Appointment appointmentToUpdate;

    /**
     *  userLoggedIn is updated to usernameInput
     *  sent from MainMenuController.
     *
     *  @param usernameInput
     */
    public static void getCurrentUser(String usernameInput) { userLoggedIn = usernameInput; }

    /**
     *  appointmentToUpdate is updated to selectedAppointment
     *  send from MainMenuController.
     *
     *  @param selectedAppointment
     */
    public static void appointmentToUpdate(Appointment selectedAppointment) { appointmentToUpdate = selectedAppointment;}

    /**
     *  Initializes Controller,
     *  Adds content to "types" observable list,
     *  Sets "types" to be selectable options in "TypeCB" combo box
     *  Sets contact names to show as selectable options in "ContactCB" combo box
     *  Sets customer names to show as selectable options in "CustomerCB" combo box
     *  Sets 15 minute time increments to be shown as selectable time options
     *  in both "StartCB" and "EndCB".
     *  Sets values of text fields, date pickers, and combo boxes to reflect
     *  current values of appointmentToUpdate,
     *
     *  *LAMBDA USE*
     *
     *   Lambdas are used to help display the contact and customer names in
     *   the combo boxes
     *
     *  @param url
     *  @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Hard codes an observable list of appointment types
        types.add("Planning Session");
        types.add("De-Briefing");
        types.add("Coffee Break");
        types.add("1-on-1 Meeting");
        types.add("Lunch");
        types.add("Interview");
        types.add("Stand-up Meeting");
        types.add("Team Building");
        types.add("Management Meeting");
        types.add("Other (Please Specify)");

        // Adds list to combo box
        TypeCB.setItems(types);

        // Sets contact names to show in combo box
        ContactCB.setItems(DatabaseInfo.getAllContacts());
        ContactCB.setCellFactory(contactChoices);
        ContactCB.setButtonCell(contactChoicesNames.call(null));
        ContactCB.setVisibleRowCount(5);

        // Sets customer names to show in combo box
        CustomerCB.setItems(DatabaseInfo.getAllCustomers());
        CustomerCB.setCellFactory(customerChoices);
        CustomerCB.setButtonCell(customerChoicesNames.call(null));
        CustomerCB.setVisibleRowCount(5);

        // loops to add time options in combo boxes
        while (start.isBefore(end)){
            StartCB.getItems().add(start);
            EndCB.getItems().add(start);
            start = start.plusMinutes(15);
        }

        // adds end time to both combo boxes
        StartCB.getItems().add(end);
        EndCB.getItems().add(end);

        // updates text fields to values of appointmentToUpdate
        AppointmentIDTF.setPromptText(Integer.toString(appointmentToUpdate.getId()));
        TitleTF.setText(appointmentToUpdate.getTitle());
        DescriptionTF.setText(appointmentToUpdate.getDescription());
        LocationTF.setText(appointmentToUpdate.getLocation());

        // updates TypeCB and/or TypeOtherTF to value in appointmentToUpdate
        if(types.contains(appointmentToUpdate.getType())){
            TypeCB.setValue(appointmentToUpdate.getType());
        }
        else {
            types.add(appointmentToUpdate.getType());
            TypeCB.setValue("Other (Please Specify)");
            TypeOtherTF.setText(appointmentToUpdate.getType());
        }

        // updates combo boxes to values of appointmentToUpdate
        CustomerCB.setValue(Helper.findCustomer(appointmentToUpdate.getCustomerID()));
        ContactCB.setValue(Helper.findContact(appointmentToUpdate.getContact()));

        LocalDate startDate = appointmentToUpdate.getStart().toLocalDateTime().toLocalDate();
        LocalTime startTime = appointmentToUpdate.getStart().toLocalDateTime().toLocalTime();

        LocalDate endDate = appointmentToUpdate.getEnd().toLocalDateTime().toLocalDate();
        LocalTime endTime = appointmentToUpdate.getEnd().toLocalDateTime().toLocalTime();

        // updates start date/time to values of appointmentToUpdate
        StartDatePick.setValue(startDate);
        StartCB.setValue(startTime);

        // updates end date/time to values of appointmentToUpdate
        EndDatePick.setValue(endDate);
        EndCB.setValue(endTime);
    }

    /**
     *  Provides "Update Appointment" button functionality
     *  Gathers information from text fields, combo boxes, and date pickers,
     *  Alerts user if required fields are blank,
     *  Alerts user if Date/Time are not in a valid range, conflict with another
     *  appointment, or are outside of business hours,
     *  Updates appointment in the database,
     *  Updates appointment in the "allAppointments" observable list for TableViews,
     *  Notifies user if upon successful update,
     *  Notifies user if no changes were made,
     *  Returns back to the "Main Menu" screen once complete.
     *
     *  @param actionEvent
     *  @throws SQLException
     *  @throws IOException
     */
    public void UpdateAppointment(ActionEvent actionEvent) throws SQLException, IOException {

        // gets index number
        int indexNum = DatabaseInfo.getAllAppointment().indexOf(appointmentToUpdate);

        // "id" is used to skip the selected appointment while checking current
        //  appointments for conflicts when updating appointments.
        int id = appointmentToUpdate.getId();

        // gets today's date
        LocalDate today = LocalDate.now();

        // place holder to be updated if a required field is found blank
        String blankTF = "";

        // collects user inputs
        selectedType = TypeCB.getValue();
        selectedContact = ContactCB.getValue();
        selectedCustomer = CustomerCB.getValue();
        selectedStartDate = StartDatePick.getValue();
        selectedEndDate = EndDatePick.getValue();
        selectedStartTime = StartCB.getValue();
        selectedEndTime = EndCB.getValue();

        // checks for blank values
        if(selectedType == null){
            blankTF = "Type";
        }
        if(selectedContact == null){
            blankTF = "Contact";
        }
        if(selectedCustomer == null){
            blankTF = "Customer";
        }
        if(selectedStartDate == null){
            blankTF = "Start Date";
        }
        if(selectedEndDate == null){
            blankTF = "End Date";
        }
        if(selectedStartTime == null){
            blankTF = "Start Time";
        }
        if(selectedEndTime == null){
            blankTF = "End Time";
        }

        // alerts user of blank values
        if(selectedType == null || selectedContact == null || selectedCustomer == null ||
                selectedStartDate == null || selectedEndDate == null || selectedStartTime == null ||
                selectedEndTime == null) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION, blankTF + " must not be blank.");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Blank Field Detected!");
            alert.showAndWait();
            return;
        }

        // collects user input
        String titleInput = TitleTF.getText();
        String descriptionInput = DescriptionTF.getText();
        String locationInput = LocationTF.getText();
        String typeOtherInput = TypeOtherTF.getText();

        // checks for blank values
        if (titleInput.isBlank()){
            blankTF = "Title";
        }
        if (descriptionInput.isBlank()){
            blankTF = "Description";
        }
        if (locationInput.isBlank()){
            blankTF = "Location";
        }

        // alerts user of blank values
        if(titleInput.isBlank() || descriptionInput.isBlank() || locationInput.isBlank()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,blankTF + " must not be blank.");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Blank Field Detected!");
            alert.showAndWait();
            return;
        }

        // checks if "Types (Other)" is required, and if so, if it is blank
        if(selectedType.contains("Other (Please Specify)") && typeOtherInput.isBlank()){
            blankTF = "Type (Other)";
            Alert alert = new Alert(Alert.AlertType.INFORMATION,blankTF + " must not be blank.");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Blank Field Detected!");
            alert.showAndWait();
            return;
        }

        // adds and sets the values to match the user input
        if(selectedType.contains("Other (Please Specify)")){
            types.add(typeOtherInput);
            selectedType = types.get(types.size()-1);
        }

        // checks if start date is after the end date
        if (selectedStartDate.isAfter(selectedEndDate)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION," Start Date must not be after End Date");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Invalid Date Range");
            alert.showAndWait();
            return;
        }

        // checks if the start date is earlier than the current date
        if(selectedStartDate.isBefore(today) || selectedEndDate.isBefore(today)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Start/End Date must not be earlier than today. (" + today + ")");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Invalid Date Selection");
            alert.showAndWait();
            return;
        }

        // checks if the start time is before end time
        if(selectedStartTime.isAfter(selectedEndTime)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Start Time must not be after End Time");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Invalid Time Set");
            alert.showAndWait();
            return;
        }

        Timestamp selectedStart = TimeZones.localToUTC(selectedStartDate, selectedStartTime);
        Timestamp selectedEnd = TimeZones.localToUTC(selectedEndDate,selectedEndTime);

        // checks if appointment time is before business hours
        boolean tooEarly = TimeZones.beforeOfficeHoursCheck(selectedStartDate, selectedStartTime);
        if (tooEarly == true) { return; }

        // checks if appointment time is after business hours
        boolean tooLate = TimeZones.afterOfficeHoursCheck(selectedEndDate, selectedEndTime);
        if (tooLate == true) { return; }

        // checks against currently scheduled appointments for any conflicts
        boolean conflict = TimeZones.conflictCheck(id,selectedStartDate, selectedStartTime, selectedEndDate, selectedEndTime);
        if (conflict == true){ return; }

        // stores ID information
        int userID = Helper.findUserID(userLoggedIn);
        int customerID = selectedCustomer.getId();
        int contactID = selectedContact.getId();

        // Check for no changes
        if(appointmentToUpdate.getTitle().equals(titleInput) &&
                appointmentToUpdate.getDescription().equals(descriptionInput) &&
                appointmentToUpdate.getLocation().equals(locationInput) &&
                appointmentToUpdate.getType().equals(selectedType) &&
                appointmentToUpdate.getStart().equals(selectedStart) &&
                appointmentToUpdate.getEnd().equals(selectedEnd) &&
                appointmentToUpdate.getCustomerID() == customerID &&
                appointmentToUpdate.getContact().equals(selectedContact.getName())){

            // alerts if no changes were made
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No changes were made");
            alert.setTitle("No Updates");
            alert.setHeaderText("No changes made to the appointment");
            alert.showAndWait();

            // back to "Main Menu"
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1100,700);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        }
        else {

            // updates information if changes are found
            appointmentToUpdate.setTitle(titleInput);
            appointmentToUpdate.setDescription(descriptionInput);
            appointmentToUpdate.setLocation(locationInput);
            appointmentToUpdate.setContact(selectedContact.getName());
            appointmentToUpdate.setType(selectedType);
            appointmentToUpdate.setStart(selectedStart);
            appointmentToUpdate.setEnd(selectedEnd);
            appointmentToUpdate.setLastUpdate(Timestamp.from(Instant.now()));
            appointmentToUpdate.setLastUpdatedBy(userLoggedIn);
            appointmentToUpdate.setCustomerID(customerID);
            appointmentToUpdate.setUserID(userID);
            appointmentToUpdate.setContactID(contactID);

            // updates database table
            DatabaseInfo.updateAppointment(indexNum, appointmentToUpdate);

            int rowsAffected = SQL_Queries.updateAppointment(id, titleInput, descriptionInput, locationInput, selectedType, selectedStart, selectedEnd, userLoggedIn, customerID, userID, contactID);

            // notifies success
            if (rowsAffected > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, titleInput + " appointment has been updated!");
                alert.setTitle("Success!");
                alert.setHeaderText("Appointment Information Updated!");
                alert.showAndWait();
            }

            // back to "Main Menu"
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1100, 700);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     *  On "StartCB" selection, if the selected time matches the time selected
     *  in "EndCB", the start time is set back one 15 interval,
     *  This prevents appointments from sharing the exact same start and end time.
     *
     *  @param actionEvent
     */
    public void OnSTSelection(ActionEvent actionEvent) {
        if(StartCB.getValue().equals(EndCB.getValue())){
            EndCB.setValue(EndCB.getValue().plusMinutes(15));
        }
    }

    /**
     *  On "EndCB" selection, if the selected time matches the time selected
     *  in "StartCB", the end time is set forward one 15 interval,
     *  This prevents appointments from sharing the exact same start and end time.
     *
     * @param actionEvent
     */
    public void OnETSelection(ActionEvent actionEvent) {
        if(StartCB.getValue().equals(EndCB.getValue())){
            StartCB.setValue(StartCB.getValue().minusMinutes(15));
        }
    }

    /**
     *  Provides "Cancel" button Functionality
     *  Information entered is not saved and user is returned to "Main Menu".
     *
     *  @param actionEvent
     *  @throws IOException
     */
    public void Cancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1100,700);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }


    Callback<ListView<Contact>, ListCell<Contact>> contactChoices = lv -> new ListCell<>() {
        /**
         *  *LAMBDA FUNCTION*
         *
         *  This takes the contact names from the Contact object and helps displays them in
         *  "ContactCB" combo box as selectable options.
         *
         *  @param contact
         *  @param empty
         */
        @Override
        protected void updateItem(Contact contact, boolean empty) {
            super.updateItem(contact, empty);
            setText(empty ? "Please Select a Contact" : (contact.getName()));
        }
    };

    Callback<ListView<Contact>, ListCell<Contact>> contactChoicesNames = lv -> new ListCell<>() {
        /**
         *  *LAMBDA FUNCTION*
         *
         *  This takes the contact names from the Contact object and helps display them in
         *  "ContactCB" combo box as selectable options.
         *
         *  @param contact
         *  @param empty
         *
         */
        @Override
        protected void updateItem(Contact contact, boolean empty) {
            super.updateItem(contact, empty);
            setText(empty ? "Please Select a Contact" : (contact.getName()));
        }
    };

    Callback<ListView<Customer>, ListCell<Customer>> customerChoices = lv -> new ListCell<>() {
        /**
         *  *LAMBDA FUNCTION*
         *
         *  This takes the customer names from the Customer object and helps display them in
         *  "CustomerCB" combo box as selectable options.
         *
         *  @param customer
         *  @param empty
         *
         */
        @Override
        protected void updateItem(Customer customer, boolean empty) {
            super.updateItem(customer, empty);
            setText(empty ? "Please Select a Customer" : (customer.getName()));
        }
    };

    Callback<ListView<Customer>, ListCell<Customer>> customerChoicesNames = lv -> new ListCell<>() {
        /**
         *  *LAMBDA FUNCTION*
         *
         *  This takes the customer names from the Customer object and helps display them in
         *  "CustomerCB" combo box as selectable options.
         *
         *  @param customer
         *  @param empty
         *
         */
        @Override
        protected void updateItem(Customer customer, boolean empty) {
            super.updateItem(customer, empty);
            setText(empty ? "Please Select a Customer" : (customer.getName()));
        }
    };

}
