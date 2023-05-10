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
import model.*;
import utilities.Helper;
import utilities.SQL_Queries;
import utilities.TimeZones;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *  AddAppointmentsController - provides functionality to
 *  the "AddAppointments.fxml" screen
 */
public class AddAppointmentsController implements Initializable {
    public TextField TitleTF;
    public TextField DescriptionTF;
    public TextField LocationTF;
    public ComboBox<Contact> ContactCB;
    public ComboBox<String> TypeCB;
    public ComboBox<Customer> CustomerCB;
    public TextField TypeOtherTF;
    public DatePicker StartDatePick;
    public DatePicker EndDatePick;
    public ComboBox<LocalTime> StartCB;
    public ComboBox<LocalTime> EndCB;
    public Button AddAppointmentButton;
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

    /**
     *  userLoggedIn is updated to usernameInput
     *  sent from MainMenuController.
     *
     *  @param usernameInput
     */
    public static void getCurrentUser(String usernameInput) { userLoggedIn = usernameInput.toLowerCase(Locale.ROOT); }

    /**
     *  Initializes Controller,
     *  Adds content to "types" observable list,
     *  Sets "types" to be selectable options in "TypeCB" combo box
     *  Sets contact names to show as selectable options in "ContactCB" combo box
     *  Sets customer names to show as selectable options in "CustomerCB" combo box
     *  Sets 15 minute time increments to be shown as selectable time options
     *  in both "StartCB" and "EndCB".
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

        //Adds list to combo box
        TypeCB.setItems(types);
        TypeCB.setPromptText("Select Type");

        // Sets contact names to show in combo box
        ContactCB.setItems(DatabaseInfo.getAllContacts());
        ContactCB.setPromptText("Select Contact");
        ContactCB.setCellFactory(contactChoices);
        ContactCB.setButtonCell(contactChoicesNames.call(null));
        ContactCB.setVisibleRowCount(5);

        // Sets customer names to show in combo box
        CustomerCB.setItems(DatabaseInfo.getAllCustomers());
        CustomerCB.setPromptText("Select Customer");
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
    }

    /**
     *  Provides "Add Appointment" button functionality,
     *  Gathers information from text fields, combo boxes, and date pickers,
     *  Alerts user if required fields are blank,
     *  Alerts user if Date/Time are not in a valid range, conflict with another
     *  appointment, or are outside of business hours,
     *  Adds new appointment to the database,
     *  Adds new appointment to the "allAppointments" observable list for TableViews,
     *  Notifies user if upon successful addition,
     *  Returns back to the "Main Menu" screen once complete,
     *
     *  *LAMBDA USE*
     *
     *   Lambdas are used to help display the contact and customer names in
     *   the combo boxes
     *
     *  @param actionEvent
     *  @throws IOException
     *  @throws SQLException
     */
    public void AddAppointment(ActionEvent actionEvent) throws IOException, SQLException {
        // "id" is used to skip the selected appointment while checking current
        //  appointments for conflicts when updating appointments.
        //  Here, it is set to "-1" so all appointments are check for conflicts.
        int id = -1;

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

        // checks if appointment time is before business hours
        boolean tooEarly = TimeZones.beforeOfficeHoursCheck(selectedStartDate,selectedStartTime);
        if (tooEarly == true){ return;}

        // checks if appointment time is after business hours
        boolean tooLate = TimeZones.afterOfficeHoursCheck(selectedEndDate,selectedEndTime);
        if (tooLate == true){return;}

        // checks against currently scheduled appointments for any conflicts
        boolean conflict = TimeZones.conflictCheck(id,selectedStartDate, selectedStartTime, selectedEndDate, selectedEndTime);
        if (conflict == true){return;}

        // stores ID information
        int userID = Helper.findUserID(userLoggedIn);
        int customerID = selectedCustomer.getId();
        int contactID = selectedContact.getId();

        // collects user input
        Timestamp selectedStart = TimeZones.localToUTC(selectedStartDate, selectedStartTime);
        Timestamp selectedEnd = TimeZones.localToUTC(selectedEndDate,selectedEndTime);

        // adds appointment to the database
        int rowsAffected = SQL_Queries.insertNewAppointment(titleInput,descriptionInput,locationInput,
                            selectedType,selectedStart,selectedEnd,userLoggedIn,customerID,userID,contactID);

        // notifies success
        if(rowsAffected > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, titleInput + " appointment has been added!");
            alert.setTitle("Success!");
            alert.setHeaderText("New Appointment Added!");
            alert.showAndWait();
        }

        // adds appointment to allAppointments observable list
        SQL_Queries.selectAddNewAppointment();

        // back to "Main Menu"
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1100,700);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();

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



    Callback<ListView<Contact>, ListCell<Contact>> contactChoices = lv -> new ListCell<>() {
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
