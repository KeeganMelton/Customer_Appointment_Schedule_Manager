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
import model.DatabaseInfo;
import model.NationalDivision;
import utilities.Helper;
import utilities.SQL_Queries;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *  AddCustomerController - provides functionality to
 *  the "AddCustomers.fxml" screen
 */
public class AddCustomerController implements Initializable {
    public Button AddCustomerButton;
    public Button CancelButton;
    public TextField NameTF;
    public TextField AddressTF;
    public TextField PostalCodeTF;
    public TextField PhoneNumberTF;
    public ComboBox<Country> CountryCB;
    public ComboBox<NationalDivision> NationalDivisionCB;

    public Country countrySelection;
    public NationalDivision divisionSelection;

    private static String userLoggedIn;

    /**
     *  userLoggedIn is updated to usernameInput
     *  sent from MainMenuController or AllCustomerInfoController.
     *
     *  @param usernameInput
     */
    public static void getCurrentUser(String usernameInput) { userLoggedIn = usernameInput; }

    /**
     *  Initializes Controller,
     *  Sets names of countries to show as selectable options in "CountryCB" combo box,
     *  Sets prompt for "NationalDivisionCB" combo box.
     *
     *  *LAMBDA USE*
     *  Lambdas are used to help display country names and first-level division names
     *  in the combo boxes
     *
     *
     *  @param url
     *  @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CountryCB.setItems(DatabaseInfo.getAllCountries());
        CountryCB.setPromptText("Select a Country");
        CountryCB.setCellFactory(countryChoices);
        CountryCB.setButtonCell(countryChoicesNames.call(null));

        NationalDivisionCB.setPromptText("No Country Selected");
    }

    /**
     *  Provides "Add Customer" button functionality,
     *  Gathers information from text fields and combo boxes,
     *  Alerts user if required information is blank,
     *  Adds new customer to the database,
     *  Adds new customer to the "allCustomers" observable list for TableViews,
     *  Notifies user if upon successful addition,
     *  Returns back to the "Main Menu" screen once complete.
     *
     *  @param actionEvent
     *  @throws SQLException
     *  @throws IOException
     */
    public void SaveCustomer(ActionEvent actionEvent) throws SQLException, IOException {
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

        // adds customer to the database
        int rowsAffected = SQL_Queries.insertNewCustomer(nameInput,addressInput,postalCodeInput,phoneInput,userLoggedIn,divisionID);

        if(rowsAffected > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, nameInput + " has been added as a customer!");
            alert.setTitle("Success!");
            alert.setHeaderText("New Customer Added!");
            alert.showAndWait();
        }

        // adds customer to allCustomers observable list
        SQL_Queries.selectAddNewCustomer();

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


    public void OnCountrySelection(ActionEvent actionEvent) {
        countrySelection = CountryCB.getValue();
        ObservableList<NationalDivision> selectedCountryDivision = Helper.divideTheNation(countrySelection);

        NationalDivisionCB.setPromptText("Select a Division");
        NationalDivisionCB.setItems(selectedCountryDivision);
        NationalDivisionCB.setCellFactory(divisionChoices);
        NationalDivisionCB.setButtonCell(divisionChoicesNames.call(null));
        NationalDivisionCB.setVisibleRowCount(7);
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

    Callback<ListView<Country>, ListCell<Country>> countryChoicesNames = lv -> new ListCell<>() {
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

    Callback<ListView<NationalDivision>, ListCell<NationalDivision>> divisionChoicesNames = lv -> new ListCell<>() {
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
