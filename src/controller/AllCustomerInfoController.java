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
import utilities.SQL_Queries;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *  AllCustomerInfoController - provides functionality to
 *  the "AllCustomerInfo.fxml" screen
 */
public class AllCustomerInfoController implements Initializable {
    public Button MainMenuButton;
    public TableView<Customer> CustomerTableView;
    public TableColumn<Customer, Integer> ID;
    public TableColumn<Customer, String> Name;
    public TableColumn<Customer, String> Address;
    public TableColumn<Customer, String> Postal;
    public TableColumn<Customer, String> Phone;
    public TableColumn<Customer, Date> CreatedOn;
    public TableColumn<Customer, String> CreatedBy;
    public TableColumn<Customer, Date> LastUpdatedOn;
    public TableColumn<Customer, String> LastUpdatedBy;
    public TableColumn<Customer, String> NationalDivision;

    public Button AddCustomerButton;
    public Button UpdateCustomerButton;
    public Button RemoveCustomerButton;

    private static String userLoggedIn;

    /**
     *  userLoggedIn is updated to usernameInput
     *  sent from MainMenuController.
     *
     *  @param usernameInput
     */
    public static void getCurrentUser(String usernameInput) { userLoggedIn = usernameInput; }

    /**
     *  Initializes Controller,
     *  Populates "CustomerTableView" with customers
     *
     *  @param url
     *  @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        Postal.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        Phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        CreatedOn.setCellValueFactory(new PropertyValueFactory<>("createdDate"));
        CreatedBy.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        LastUpdatedOn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        LastUpdatedBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        NationalDivision.setCellValueFactory(new PropertyValueFactory<>("nationalDivision"));

        CustomerTableView.setItems(DatabaseInfo.getAllCustomers());

    }

    /**
     *  Provides "Back to Main Menu" button functionality
     *  Returns user to "Main Menu" screen
     *
     *  @param actionEvent
     *  @throws IOException
     */
    public void ToMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1100,700);
        stage.setTitle("Main Menu");
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
    public void ToAddCustomer(ActionEvent actionEvent) throws IOException {
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
                    CustomerTableView.refresh();

                    return;
                }
            }
        }
        catch(Exception e){}
    }


}
