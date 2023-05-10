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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.DatabaseInfo;
import utilities.Helper;
import utilities.SQL_Queries;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *  Main Menu Controller - provides functionality to
 *  the "MainMenu.fxml" screen
 */
public class MainMenuController implements Initializable {
    public Button CustomerInfoButton;
    public Button AddCustomerButton;
    public Button RemoveCustomerButton;
    public Button UpdateCustomerButton;

    public TableView<Customer> CustomerTableView;
    public TableColumn<Customer, Integer> CustomerID;
    public TableColumn<Customer, String> CustomerName;
    public TableColumn<Customer, String> CustomerAddress;
    public TableColumn<Customer, String> CustomerPostalCode;
    public TableColumn<Customer, String> CustomerPhone;
    public TableColumn<Customer, String> CustomerNationalDivision;


    public Button AddAppointmentButton;
    public Button UpdateAppointmentButton;
    public Button RemoveAppointmentButton;

    public TableView<Appointment> AppointmentTableView;
    public TableColumn<Appointment, Integer> AppointmentID;
    public TableColumn<Appointment, String> AppointmentTitle;
    public TableColumn<Appointment, String> AppointmentDescription;
    public TableColumn<Appointment, String> AppointmentLocation;
    public TableColumn<Appointment, String> AppointmentContact;
    public TableColumn<Appointment, String> AppointmentType;
    public TableColumn<Appointment, Date>AppointmentStart;
    public TableColumn<Appointment, Date> AppointmentEnd;
    public TableColumn<Appointment, Integer>AppointmentCustomerID;
    public TableColumn<Appointment, Integer> AppointmentUserID;
    public TableColumn<Appointment, Integer >AppointmentContactID;

    private static String userLoggedIn;
    public LocalDate today = LocalDate.now();

    public ComboBox<Month> MonthCB;
    public ComboBox<String> TypeCB;
    public Label TypesByMonthLabel;

    public ComboBox<String> ContactCB;

    public Label CustomerAppCountLabel;
    public ComboBox<String> CustomerCB;

    boolean filter = false;

    /**
     *  userLoggedIn is updated to usernameInput
     *  sent from LoginScreenController.
     *
     *  @param usernameInput
     */
    public static void getCurrentUser(String usernameInput) { userLoggedIn = usernameInput; }

    // Observable lists used for reports section
    ObservableList<Month> months = FXCollections.observableArrayList();
    ObservableList<String> typesList = FXCollections.observableArrayList();
    ObservableList<String> contactList = FXCollections.observableArrayList();
    ObservableList<String> CustomerList = FXCollections.observableArrayList();
    ObservableList<Appointment> filteredApp = FXCollections.observableArrayList();

    /**
     *  Initalizes Controller,
     *  Adds months to "months" list,
     *  Sets "MonthCB" to display "months" list,
     *
     *  Adds appointment types to "typesList",
     *  Sets "TypeCB" to display "typeList",
     *
     *  Adds contact names to "contactList",
     *  Sets "ContactCB" to display "contactList",
     *
     *  Adds customerNames to "CustomerList",
     *  Sets "CustomerCB" to display "CustomerList"
     *
     *  Populates "CustomerTableView" with customers
     *  Populates "AppointmentTableView" with appointments
     *
     *  @param url
     *  @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // adds months to list
        months.add(Month.JANUARY);
        months.add(Month.FEBRUARY);
        months.add(Month.MARCH);
        months.add(Month.APRIL);
        months.add(Month.MAY);
        months.add(Month.JUNE);
        months.add(Month.JULY);
        months.add(Month.AUGUST);
        months.add(Month.SEPTEMBER);
        months.add(Month.OCTOBER);
        months.add(Month.NOVEMBER);
        months.add(Month.DECEMBER);

        // sets combo box
        MonthCB.setItems(months);

        // loops through appointments to add appointment types to list
        for(int i = 0; i < DatabaseInfo.getAllAppointment().size(); i++){
            String type = DatabaseInfo.getAllAppointment().get(i).getType();
            if(typesList.contains(type)){
                i++;
            }
            else {
                typesList.add(type);
            }
        }

        // sets combo box
        TypeCB.setItems(typesList);

        // loops through contacts to add contact names to list
        for(int i = 0; i < DatabaseInfo.getAllContacts().size(); i++){
            String name = DatabaseInfo.getAllContacts().get(i).getName();
            contactList.add(name);
        }

        // sets combo box
        ContactCB.setItems(contactList);

        // loops through customers to add customer names to list
        for(int i = 0; i < DatabaseInfo.getAllCustomers().size(); i++){
            String name = DatabaseInfo.getAllCustomers().get(i).getName();
            CustomerList.add(name);
        }

        // sets combo box
        CustomerCB.setItems(CustomerList);


        // assigns values to table
        CustomerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        CustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        CustomerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        CustomerPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        CustomerPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        CustomerNationalDivision.setCellValueFactory(new PropertyValueFactory<>("nationalDivision"));

        CustomerTableView.setItems(DatabaseInfo.getAllCustomers());

        // assigns values to table
        AppointmentID.setCellValueFactory(new PropertyValueFactory<>("id"));
        AppointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        AppointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        AppointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        AppointmentContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        AppointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        AppointmentStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        AppointmentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        AppointmentCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        AppointmentUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        AppointmentContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));

        AppointmentTableView.setItems(DatabaseInfo.getAllAppointment());

    }

    /**
     *  Provides "All Customer Information" button functionality
     *  Sends user to the "Customer Information" screen
     *  Sends username of user currently logged in to AllCustomerInfoController
     *
     *  @param actionEvent
     *  @throws IOException
     */
    public void ToAllCustomerInfo(ActionEvent actionEvent) throws IOException {
        AllCustomerInfoController.getCurrentUser(userLoggedIn);

        Parent root = FXMLLoader.load(getClass().getResource("/view/AllCustomerInfo.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000,400);
        stage.setTitle("Customer Information");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  Provides "Add New Customer" button functionality
     *  Sends user to the "Add Customer" screen,
     *  Sends username of user currently logged in to AddCustomerController.
     *
     *  @param actionEvent
     *  @throws IOException
     */
    public void ToAddCustomer(ActionEvent actionEvent) throws IOException{
        AddCustomerController.getCurrentUser(userLoggedIn);

        Parent root = FXMLLoader.load(getClass().getResource("/view/AddCustomers.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 750,400);
        stage.setTitle("Add Customer");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  Provides "Update Customer" button functionality
     *  Sends selected customer to UpdateCustomerController,
     *  Sends username of user currently logged in to UpdateCustomerController,
     *  Sends user to the "Update Customer" screen,
     *  Alerts user if no customer is selected
     *
     *  @param actionEvent
     *  @throws IOException
     */
    public void ToUpdateCustomer(ActionEvent actionEvent) throws IOException {
        Customer selectedCustomer = CustomerTableView.getSelectionModel().getSelectedItem();
        UpdateCustomerController.getCurrentUser(userLoggedIn);

        try{
            if(selectedCustomer == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Please select a customer to update.");
                alert.setTitle("Invalid Action!");
                alert.setHeaderText("No customer selected");
                alert.showAndWait();
                return;
            }
            UpdateCustomerController.customerToUpdate(selectedCustomer);

            Parent root = FXMLLoader.load(getClass().getResource("/view/UpdateCustomers.fxml"));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 750,400);
            stage.setTitle("Update Customer");
            stage.setScene(scene);
            stage.show();

        }
        catch (Exception e){}
    }

    /**
     *  Provides "Add Appointment" button functionality
     *  Sends user to the "Add Appointment" screen,
     *  Sends username of user currently logged in to AddAppointmentController
     *
     *  @param actionEvent
     *  @throws IOException
     */
    public void ToAddAppointment(ActionEvent actionEvent) throws IOException {
        AddAppointmentsController.getCurrentUser(userLoggedIn);

        Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppointments.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 750,500);
        stage.setTitle("Add Appointment");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  Provides "Update Appointment" button functionality
     *  Sends selected appointment to UpdateAppointmentController,
     *  Sends username of user currently logged in to UpdateAppointmentController,
     *  Sends user to "Update Appointment" screen,
     *  Alerts user if no appointment is selected
     *
     *  @param actionEvent
     *  @throws IOException
     */
    public void ToUpdateAppointment(ActionEvent actionEvent) throws IOException {
        Appointment selectedAppointment = AppointmentTableView.getSelectionModel().getSelectedItem();
        UpdateAppointmentController.getCurrentUser(userLoggedIn);

        try{
            if(selectedAppointment == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select an appointment to update.");
                alert.setTitle("Invalid Action!");
                alert.setHeaderText("No appointment selected");
                alert.showAndWait();
                return;
            }

        UpdateAppointmentController.appointmentToUpdate(selectedAppointment);

        Parent root = FXMLLoader.load(getClass().getResource("/view/UpdateAppointments.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 750,500);
        stage.setTitle("Update Appointment");
        stage.setScene(scene);
        stage.show();

        }
        catch (Exception e){}
    }

    /**
     *  Provides "Remove Appointment" button functionality
     *  Alerts user if no appointment is selected,
     *  Prompts for confirmation to remove appointment,
     *  Notifies after appointment is removed.
     *  Refreshes table to reflect changes
     *
     *  @param actionEvent
     */
    public void RemoveAppointment(ActionEvent actionEvent) {
        Appointment deleteAppointment = AppointmentTableView.getSelectionModel().getSelectedItem();
        try{
            if(deleteAppointment == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select an appointment to remove.");
                alert.setTitle("Invalid Action!");
                alert.setHeaderText("No appointment selected");
                alert.showAndWait();
                return;
            }
            String customerName = Helper.findCustomerName(deleteAppointment.getCustomerID());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete appointment number "+ deleteAppointment.getId() + ", a(n) "
                            + deleteAppointment.getType() + " with " + customerName +" from the schedule?\n");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                int rowsAffected = SQL_Queries.deleteAppointmentByID(deleteAppointment.getId());
                if (rowsAffected > 0 ){

                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION,  "Appointment " + deleteAppointment.getId() + ", a(n) "
                            + deleteAppointment.getType() + " with " + customerName +" has been removed");
                    infoAlert.setTitle("Success!");
                    infoAlert.setHeaderText("Appointment Removed");
                    infoAlert.showAndWait();

                    DatabaseInfo.deleteAppointment(deleteAppointment);
                    AppointmentTableView.refresh();

                    return;
                }
            }
        }
        catch(Exception e){

        }
    }

    /**
     *  Provides "Remove Customer" button functionality
     *  Alerts user if no customer is selected,
     *  Alerts user appointments with this customer will also be removed,
     *  Removed appointments and customer after user confirmation,
     *  Notifies user of successful removal,
     *  Refreshes table to reflect changes.
     *
     *  @param actionEvent
     */
    public void RemoveCustomer(ActionEvent actionEvent) {
        Customer deleteCustomer = CustomerTableView.getSelectionModel().getSelectedItem();
        ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
        try {
            if (deleteCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select a customer to remove.");
                alert.setTitle("Invalid Action!");
                alert.setHeaderText("No customer selected");
                alert.showAndWait();
                return;
            }

            int appointmentCount = 0;
            int customerID = deleteCustomer.getId();
            for(int i = 0; i < DatabaseInfo.getAllAppointment().size(); i++){
                if(DatabaseInfo.getAllAppointment().get(i).getCustomerID() == customerID){
                    customerAppointments.add(DatabaseInfo.getAllAppointment().get(i));
                    appointmentCount++;
                }
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,appointmentCount + " appointments with this customer will be removed as well");
            alert.setHeaderText("Are you sure you want to delete \"" + deleteCustomer.getName() + "\" ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                int deletedAppointments = SQL_Queries.deleteAllAppointmentsWithCustomer(customerID);
                int deletedCustomer = SQL_Queries.deleteCustomer(customerID);
                if(deletedCustomer > 0) {

                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "\"" + deleteCustomer.getName() + "\" and " +deletedAppointments + " appointments have been removed");
                    infoAlert.setTitle("Success!");
                    infoAlert.setHeaderText("Customer Removed");
                    infoAlert.showAndWait();

                    for(int i = 0; i < customerAppointments.size(); i++){
                        DatabaseInfo.deleteAppointment(customerAppointments.get(i));
                    }

                    DatabaseInfo.deleteCustomer(deleteCustomer);
                    AppointmentTableView.refresh();
                    CustomerTableView.refresh();

                    return;
                }
            }
        }
        catch(Exception e){}
    }

    /**
     *  Sets "TypesByMonthLabel" to display the total number of appointments
     *  that match the selected month and type from "MonthCB" and "TypeCB"
     */
    private void appointmentsTypesByMonth() {
        Month selectedMonth = MonthCB.getSelectionModel().getSelectedItem();
        String selectedType = TypeCB.getValue();
        int count = 0;
        for(int i = 0; i < DatabaseInfo.getAllAppointment().size(); i++){
            if(DatabaseInfo.getAllAppointment().get(i).getStart().toLocalDateTime().toLocalDate().getMonth().equals(selectedMonth)
            && DatabaseInfo.getAllAppointment().get(i).getType().equals(selectedType)){
                count = count + 1;
            }
        }
        TypesByMonthLabel.setText(Integer.toString(count));
    }

    /**
     *  Refreshes "AppointmentTableView" to show all appointments
     *  when "All Appointments" radio button is selected if not filtered
     *  by "Appointments per Contact" report,
     *
     *  Refreshes "AppointmentTableView" to show all appointment for the
     *  selected contact when filtered by "Appointments per Contact" report.
     *
     *  @param actionEvent
     */
    public void AllAppointmentsSelected(ActionEvent actionEvent) {
        if(filter == false){
            AppointmentTableView.setItems(DatabaseInfo.getAllAppointment());
            AppointmentTableView.refresh();
        }

        if(filter == true){
            AppointmentTableView.setItems(filteredApp);
            AppointmentTableView.refresh();
        }
    }

    /**
     *  Refreshes "AppointmentTableView" to show all appointments in the
     *  next 30 days when "Appointments in the next 30 days" radio button
     *  is selected if not filtered by "Appointments per Contact" report,
     *
     *  Refreshes "AppointmentTableView" to show all appointment in the
     *  next 30 days for the selected contact when filtered by
     *  "Appointments per Contact" report.
     *
     *  @param actionEvent
     */
    public void Appointments30Selected(ActionEvent actionEvent) {
        if (filter == false) {
            ObservableList<Appointment> appointmentsNext30 = FXCollections.observableArrayList();
            for (int i = 0; i < DatabaseInfo.getAllAppointment().size(); i++) {
                if (DatabaseInfo.getAllAppointment().get(i).getStart().toLocalDateTime().toLocalDate().isBefore(today.plusDays(30))
                        && DatabaseInfo.getAllAppointment().get(i).getStart().toLocalDateTime().toLocalDate().isAfter(today.minusDays(1))) {
                    appointmentsNext30.add(DatabaseInfo.getAllAppointment().get(i));
                }
            }
            AppointmentTableView.setItems(appointmentsNext30);
            AppointmentTableView.refresh();
        }
        if(filter == true){
            ObservableList<Appointment> appointmentsNext30 = FXCollections.observableArrayList();
            for (int i = 0; i < filteredApp.size(); i++) {
                if (filteredApp.get(i).getStart().toLocalDateTime().toLocalDate().isBefore(today.plusDays(30))
                        && filteredApp.get(i).getStart().toLocalDateTime().toLocalDate().isAfter(today.minusDays(1))) {
                    appointmentsNext30.add(filteredApp.get(i));
                }
            }
            AppointmentTableView.setItems(appointmentsNext30);
            AppointmentTableView.refresh();
        }
    }

    /**
     *  Refreshes "AppointmentTableView" to show all appointments in the
     *  next 7 days when "Appointments in the next 7 days" radio button
     *  is selected if not filtered by "Appointments per Contact" report,
     *
     *  Refreshes "AppointmentTableView" to show all appointment in the
     *  next 7 days for the selected contact when filtered by
     *  "Appointments per Contact" report.
     *
     *  @param actionEvent
     */
    public void Appointments7Selected(ActionEvent actionEvent) {
        if (filter == false) {
            ObservableList<Appointment> appointmentsNext7 = FXCollections.observableArrayList();
            for (int i = 0; i < DatabaseInfo.getAllAppointment().size(); i++) {
                if (DatabaseInfo.getAllAppointment().get(i).getStart().toLocalDateTime().toLocalDate().isBefore(today.plusDays(7))
                        && DatabaseInfo.getAllAppointment().get(i).getStart().toLocalDateTime().toLocalDate().isAfter(today.minusDays(1))) {
                    appointmentsNext7.add(DatabaseInfo.getAllAppointment().get(i));
                }
            }
            AppointmentTableView.setItems(appointmentsNext7);
            AppointmentTableView.refresh();
        }
        if(filter == true){
            ObservableList<Appointment> appointmentsNext7 = FXCollections.observableArrayList();
            for (int i = 0; i < filteredApp.size(); i++) {
                if (filteredApp.get(i).getStart().toLocalDateTime().toLocalDate().isBefore(today.plusDays(7))
                        && filteredApp.get(i).getStart().toLocalDateTime().toLocalDate().isAfter(today.minusDays(1))) {
                    appointmentsNext7.add(filteredApp.get(i));
                }
            }
            AppointmentTableView.setItems(appointmentsNext7);
            AppointmentTableView.refresh();
        }
    }

    /**
     *  Provides "Close Application" button functionality
     *  exits application
     *
     *  @param actionEvent
     */
    public void CloseApplication(ActionEvent actionEvent) { System.exit(0); }

    /**
     *  Provides "Log Out" button functionality
     *  returns user to the "Login" screen
     *
     *  @param actionEvent
     *  @throws IOException
     */
    public void LogOut(ActionEvent actionEvent) throws IOException {

        ResourceBundle rb = ResourceBundle.getBundle("resource/Language", Locale.getDefault());

        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.setTitle(rb.getString("Login"));
        Scene scene = new Scene(root, 600,400);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  Provides "Filter Table" button functionality
     *  Filters "AppointmentTableView" based on users selected contact in "ContactCB",
     *  Alerts user if a selection has not been made.
     *
     *  @param actionEvent
     */
    public void FilterTable(ActionEvent actionEvent) {
        if(ContactCB.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Please select a contact.");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Contact not selected");
            alert.showAndWait();
            return;
        }

        if(ContactCB.getValue() != null){
            for(int i = 0; i < DatabaseInfo.getAllAppointment().size(); i++){
                if(DatabaseInfo.getAllAppointment().get(i).getContact().equals(ContactCB.getValue())){
                    filteredApp.add(DatabaseInfo.getAllAppointment().get(i));
                }
            }
        }
        AppointmentTableView.setItems(filteredApp);
        AppointmentTableView.refresh();
        filter = true;

    }

    /**
     *  Provides "Clear Filter" button functionality
     *  Refreshes "AppointmentTableView" to not be affected by
     *  "Appointments per Contact" report.
     *
     *  @param actionEvent
     */
    public void ClearFilter(ActionEvent actionEvent) {
        filter = false;
        AppointmentTableView.setItems(DatabaseInfo.getAllAppointment());
        AppointmentTableView.refresh();
    }

    /**
     *  On selection, if "TypeCB" is not null, sets "TypesByMonthLabel" to
     *  display the total number of appointments that match the selected
     *  month and type from "MonthCB" and "TypeCB".
     *
     *  @param actionEvent
     */
    public void MonthChoice(ActionEvent actionEvent) {
        if(TypeCB.getValue() != null){
            appointmentsTypesByMonth();
        }
    }

    /**
     *  On selection, if "MonthCB" is not null, sets "TypesByMonthLabel" to
     *  display the total number of appointments that match the selected
     *  month and type from "MonthCB" and "TypeCB".
     *
     *  @param actionEvent
     */
    public void TypeChoice(ActionEvent actionEvent) {
        if(MonthCB.getValue() != null){
            appointmentsTypesByMonth();
        }
    }

    /**
     *  Sets "CustomerAppCountLabel" to display the total number of appointments
     *  for the selected customer.
     *
     *  @param actionEvent
     */
    public void CustomerChoice(ActionEvent actionEvent) {
        int count = 0;
        int id = Helper.findCustomerID(CustomerCB.getValue());
        for (int i = 0; i< DatabaseInfo.getAllAppointment().size(); i++){
            if(DatabaseInfo.getAllAppointment().get(i).getCustomerID() == id){
                count = count + 1;
            }
        }
        CustomerAppCountLabel.setText(Integer.toString(count));
    }
}
