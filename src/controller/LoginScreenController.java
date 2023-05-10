/**
 * @auther Keegan Melton
 */
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.DatabaseInfo;
import utilities.Helper;
import utilities.JDBC;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *  LoginScreenController - provides functionality to
 *  the "LoginScreen.fxml" screen
 */

public class LoginScreenController implements Initializable {
    public Button LoginButton;
    public Button ExitButton;
    public TextField UserNameTF;
    public TextField PasswordTF;
    public Label TimezoneLabel;
    public Label WelcomeLabel;
    public Label TZMessageLabel;
    public Label HeaderLabel;

    private String usernameInput;

    // uses resource bundle for localization
    public ResourceBundle rb = ResourceBundle.getBundle("resource/Language", Locale.getDefault());

    ZoneId currentZone = ZonedDateTime.now().getZone();

    /** This checks the users credentials,
     *  sends an alert message if incorrect,
     *  sends user to "Main Menu" if correct,
     *  sends username to MainMenuController,
     *  alerts of upcoming appointments in 15 minutes,
     *  alerts if no appointment in 15 minutes,
     *  logs login attempt to "login_activity.txt".
     *
     *  @param actionEvent
     *  @throws IOException
     */
    public void CheckCred(ActionEvent actionEvent) throws IOException{
        boolean loginStatus = false;
        String statusMessage = "";

        // logs login attempt
        String loginActivity = "login_activity.txt";
        FileWriter fileWriter = new FileWriter(loginActivity,true);
        PrintWriter outputFile = new PrintWriter(fileWriter);
        outputFile.println("Login Attempt");
        outputFile.println("Date: " + LocalDateTime.now().toLocalDate());
        outputFile.println("Time: " + LocalDateTime.now().toLocalTime() + " " + currentZone);

        // sets "usernameInput" to lower case
        usernameInput = UserNameTF.getText().toLowerCase(Locale.ROOT);
        String passwordInput = PasswordTF.getText();

        String blankTF = "";
        if(usernameInput.isBlank()){
            blankTF = rb.getString("Username");
        }
        if(passwordInput.isBlank()){
            blankTF = rb.getString("Password");
        }
        outputFile.println("Username: " + usernameInput);
        outputFile.println("Password: " + passwordInput);

        if(usernameInput.isBlank() || passwordInput.isBlank()){
            ButtonType ok = new ButtonType(rb.getString("Ok"), ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType(rb.getString("Cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.INFORMATION,blankTF + " " + rb.getString( "NotBlank"),ok,cancel);
            alert.setTitle(rb.getString("InvalidAct"));
            alert.setHeaderText(rb.getString("BlankFieldDetected"));
            alert.showAndWait();
            return;
        }

        // loops through user credentials for a match
        for(int i = 0; i < DatabaseInfo.getAllUsers().size(); i++){
            if(usernameInput.equalsIgnoreCase(DatabaseInfo.getAllUsers().get(i).getUserName()) && passwordInput.equals(DatabaseInfo.getAllUsers().get(i).getPassword())){
                loginStatus = true;
                statusMessage = "Successful";
                outputFile.println("Status: " + statusMessage);

                MainMenuController.getCurrentUser(usernameInput);
                int loginID = Helper.findUserID(usernameInput);
                Helper.appointmentSoonCheck(loginID);


                outputFile.println();
                outputFile.close();

                Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1100,700);
                stage.setTitle("Main Menu");
                stage.setScene(scene);
                stage.show();
                return;
            }
            else{
                loginStatus = false;
            }
        }

        if(loginStatus == false){
            statusMessage = "Unsuccessful";
            outputFile.println("Status: " + statusMessage);

            outputFile.println();
            outputFile.close();

            ButtonType ok = new ButtonType(rb.getString("Ok"), ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType(rb.getString("Cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.WARNING, rb.getString("InvalidUser/Pass"), ok, cancel);
            alert.setTitle(rb.getString("InvalidCred"));
            alert.showAndWait();
            return;
        }

    }

    /**
     *  Sets "TimeZoneLabel" to the users current timezone
     */
    public void displayTimezone(){
        TimezoneLabel.setText(String.valueOf(currentZone));
    }

    /**
     *  Provides "Exit" button functionality
     *  Closes database connection and exits application
     *
     *  @param actionEvent
     */
    public void Exit(ActionEvent actionEvent) {

        // Closes Connection
        JDBC.closeConnection();

        // Exits application
        System.exit(0); }

    /**
     *  Initializes Controller,
     *  Sets "TimeZoneLabel" to users current timezone
     *  Sets text on screen to match system language (French / English)
     *
     *  @param url
     *  @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayTimezone();
        HeaderLabel.setText(rb.getString("AppHeader"));
        WelcomeLabel.setText(rb.getString("Welcome") + "!");
        TZMessageLabel.setText(rb.getString("CurrentTimezone"));
        UserNameTF.setPromptText(rb.getString("Username"));
        PasswordTF.setPromptText(rb.getString("Password"));
        LoginButton.setText(rb.getString("Login"));
        ExitButton.setText(rb.getString("Exit"));

    }
}
