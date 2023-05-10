/**
 * @auther Keegan Melton
 */
package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Country;
import model.Customer;
import model.DatabaseInfo;
import model.NationalDivision;
import utilities.Helper;
import utilities.SQL_Queries;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *  AddCustomerController - provides functionality to
 *  the "AddCustomers.fxml" screen
 */
public class UpdateCustomerController implements Initializable {
    public TextField CustomerIDTF;
    public TextField NameTF;
    public TextField AddressTF;
    public TextField PhoneNumberTF;
    public TextField PostalCodeTF;
    public ComboBox<Country> CountryCB;
    public ComboBox<NationalDivision> NationalDivisionCB;
    public Label CurrentUser;
    public Button CancelButton;
    public Button UpdateCustomerButton;

    private static String userLoggedIn;
    private static Customer customerToUpdate;
    public Country countrySelection;
    public NationalDivision divisionSelection;

    /**
     *  userLoggedIn is updated to usernameInput
     *  sent from MainMenuController or AllCustomerInfoController.
     *
     *  @param usernameInput
     */
    public static void getCurrentUser(String usernameInput) { userLoggedIn = usernameInput; }

    /**
     *  customerToUpdate is updated to selectedCustomer
     *  send from MainMenuController or AllCustomerInfoController.
     *
     *  @param selectedCustomer
     */
    public static void customerToUpdate(Customer selectedCustomer) { customerToUpdate = selectedCustomer;}

    /**
     *  Initializes Controller,
     *  Sets names of countries to show as selectable options in "CountryCB" combo box,
     *  Sets prompt for "NationalDivisionCB" combo box
     *  Sets values of text fields and combo boxes to reflect
     *  current values of customerToUpdate.
     *
     *  *LAMBDA USE*
     *   Lambdas are used to help display country names and first-level division names
     *   in the combo boxes
     *
     *  @param url
     *  @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NationalDivisionCB.setItems(DatabaseInfo.getAllDivision());

        CurrentUser.setText(userLoggedIn.toLowerCase(Locale.ROOT));

        CountryCB.setItems(DatabaseInfo.getAllCountries());
        CountryCB.setCellFactory(countryChoices);
        CountryCB.setButtonCell(countryChoicesUsed.call(null));

        CustomerIDTF.setPromptText(Integer.toString(customerToUpdate.getId()));
        NameTF.setText(customerToUpdate.getName());
        AddressTF.setText(customerToUpdate.getAddress());
        PhoneNumberTF.setText(customerToUpdate.getPhone());
        PostalCodeTF.setText(customerToUpdate.getPostalCode());

        for(int i = 0; i < DatabaseInfo.getAllDivision().size(); i++){
            NationalDivision nd = DatabaseInfo.getAllDivision().get(i);
            Country country = CountryCB.getItems().get(nd.getCountryID() - 1);
            if(nd.getDivisionName().equals(customerToUpdate.getNationalDivision())){
                CountryCB.setValue(country);
                NationalDivisionCB.setValue(nd);
                break;
            }
        }
        countrySelection = CountryCB.getValue();
        ObservableList<NationalDivision> selectedCountryDivision = Helper.divideTheNation(countrySelection);
        NationalDivisionCB.setItems(selectedCountryDivision);
        NationalDivisionCB.setCellFactory(divisionChoices);
        NationalDivisionCB.setVisibleRowCount(7);
        NationalDivisionCB.setButtonCell(divisionChoicesUsed.call(null));

    }

    /**
     *  Provides "Update Customer" button functionality,
     *  Gathers information from text fields and combo boxes,
     *  Alerts user if required information is blank,
     *  Updates customer in the database,
     *  Updates customer in the "allCustomers" observable list for TableViews,
     *  Notifies user if upon successful update,
     *  Returns back to the "Main Menu" screen once complete.
     *
     *  @param actionEvent
     *  @throws SQLException
     *  @throws IOException
     */
    public void UpdateCustomer(ActionEvent actionEvent) throws SQLException, IOException {

        // gets index number
        int indexNum = DatabaseInfo.getAllCustomers().indexOf(customerToUpdate);

        // place holder to be updated if a required field is found blank
        String blankTF = "";

        // alerts user of blank value
        divisionSelection = NationalDivisionCB.getValue();
        if(divisionSelection == null) {
            blankTF = "National Division";

            Alert alert = new Alert(Alert.AlertType.INFORMATION, blankTF + " must not be blank.");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Blank Field Detected!");
            alert.showAndWait();
            return;
        }

        // collects user input
        int id = customerToUpdate.getId();
        String nameInput = NameTF.getText();
        String addressInput = AddressTF.getText();
        String postalCodeInput = PostalCodeTF.getText();
        String phoneInput = PhoneNumberTF.getText();
        int divisionID = divisionSelection.getDivisionID();

        // checks for blank values
        if(nameInput.isBlank()){
            blankTF = "Name";
        }
        if(addressInput.isBlank()){
            blankTF = "Address";
        }
        if(postalCodeInput.isBlank()){
            blankTF = "Postal Code";
        }
        if(phoneInput.isBlank()){
            blankTF = "Phone Number";
        }
        if(divisionSelection == null){
            blankTF = "National Division";
        }

        // alerts user of blank values
        if (nameInput.isBlank() ||
                addressInput.isBlank() ||
                postalCodeInput.isBlank() ||
                phoneInput.isBlank()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,blankTF + " must not be blank.");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Blank Field Detected!");
            alert.showAndWait();
            return;
        }

        // updates customer object information
        customerToUpdate.setName(nameInput);
        customerToUpdate.setAddress(addressInput);
        customerToUpdate.setPostalCode(postalCodeInput);
        customerToUpdate.setPhone(phoneInput);
        customerToUpdate.setLastUpdate(Timestamp.from(Instant.now()));
        customerToUpdate.setLastUpdatedBy(userLoggedIn);
        customerToUpdate.setNationalDivision(divisionSelection.getDivisionName());

        // updates database table
        DatabaseInfo.updateCustomer(indexNum,customerToUpdate);

        // notifies success
        int rowsAffected = SQL_Queries.updateCustomerInfo(id,nameInput,addressInput,postalCodeInput,phoneInput,userLoggedIn,divisionID);
        if(rowsAffected > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Information for " + nameInput + " has been updated!");
            alert.setTitle("Success!");
            alert.setHeaderText("Customer Information Updated!");
            alert.showAndWait();
        }

        // back to "Main Menu"
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1100,700);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *  Provides "Cancel" button functionality
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
     *  Sets "NationalDivisionCB" selection options based on the selected country
     *  in "CountryCB" combo box.
     *
     *  @param actionEvent
     */
    public void OnCountrySelection(ActionEvent actionEvent) {
        countrySelection = CountryCB.getValue();
        ObservableList<NationalDivision> selectedCountryDivision = Helper.divideTheNation(countrySelection);

        NationalDivisionCB.setPromptText("Select a Division");
        NationalDivisionCB.setItems(selectedCountryDivision);
        NationalDivisionCB.setCellFactory(divisionChoices);
        NationalDivisionCB.setVisibleRowCount(7);
        NationalDivisionCB.setButtonCell(divisionChoicesUsed.call(null));
    }
    Callback<ListView<Country>, ListCell<Country>> countryChoices = lv -> new ListCell<>() {
        /**
         *  *LAMBDA FUNCTION*
         *
         *  This takes the country names from the Country object and helps displays them in
         *  "CountryCB" combo box as selectable options.
         *
         *  @param country
         *  @param empty
         */
        @Override
        protected void updateItem(Country country, boolean empty) {
            super.updateItem(country, empty);
            setText(empty ? "Please Select a Country" : (country.getName()));
        }
    };

    Callback<ListView<Country>, ListCell<Country>> countryChoicesUsed = lv -> new ListCell<>() {
        /**
         *  *LAMBDA FUNCTION*
         *
         *  This takes the country names from the Country object and helps displays them in
         *  "CountryCB" combo box as selectable options.
         *
         *  @param country
         *  @param empty
         */
        @Override
        protected void updateItem(Country country, boolean empty) {
            super.updateItem(country, empty);
            setText(empty ? "Please Select a Country" : (country.getName()));
        }
    };
    Callback<ListView<NationalDivision>, ListCell<NationalDivision>> divisionChoices = lv -> new ListCell<>() {
        /**
         *  *LAMBDA FUNCTION*
         *
         *  This takes the first-level division names from the NationalDivision object and
         *  helps displays them in "NationalDivisionCB" combo box as selectable options.
         *
         *  @param division
         *  @param empty
         */
        @Override
        protected void updateItem(NationalDivision division, boolean empty) {
            super.updateItem(division, empty);
            setText(empty ? " " : (division.getDivisionName()));
        }
    };

    Callback<ListView<NationalDivision>, ListCell<NationalDivision>> divisionChoicesUsed = lv -> new ListCell<>() {
        /**
         *  *LAMBDA FUNCTION*
         *
         *  This takes the first-level division names from the NationalDivision object and
         *  helps displays them in "NationalDivisionCB" combo box as selectable options.
         *
         *  @param division
         *  @param empty
         */
        @Override
        protected void updateItem(NationalDivision division, boolean empty) {
            super.updateItem(division, empty);
            setText(empty ? " " : (division.getDivisionName()));
        }
    };

}
