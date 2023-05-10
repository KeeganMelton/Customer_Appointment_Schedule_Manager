/**
 *  @auther Keegan Melton
 *  Helper Class
 *  Helps convert ids to strings
 *  Helps convert strings to ids
 *  Helps find the National Division of a Country
 *  Helps check if a user has an appointment in the next 15 minutes
 */
package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class Helper {

    /**
     *  Takes the selected country and returns the first-level division
     *  of that country
     *
     *  @param countrySelection
     *  @return nationalDivision
     */
    public static ObservableList<NationalDivision> divideTheNation(Country countrySelection){
        ObservableList<NationalDivision> nationalDivision = FXCollections.observableArrayList();
        int countryID = countrySelection.getId();
        for(NationalDivision firstLevelDivision : DatabaseInfo.getAllDivision()){
            if(firstLevelDivision.getCountryID() == countryID){
                nationalDivision.add(firstLevelDivision);
            }
        }
        return nationalDivision;
    }

    /**
     *  Takes the user name of the user that is logged in and
     *  returns the ID of that user
     *
     *  @param userLoggedIn
     *  @return id
     */
    public static int findUserID(String userLoggedIn){
        int id = 0;
        for(int i = 0; i < DatabaseInfo.getAllUsers().size(); i++){
            if(DatabaseInfo.getAllUsers().get(i).getUserName().equals(userLoggedIn)){
                id = DatabaseInfo.getAllUsers().get(i).getUserID();
            }
        }
        return id;
    }

    /**
     *  Takes the name of a customer and returns the ID of that customer
     *
     *  @param customerName
     *  @return id
     */
    public static int findCustomerID(String customerName){
        int id = 0;
        for(int i = 0; i < DatabaseInfo.getAllCustomers().size(); i++){
            if(DatabaseInfo.getAllCustomers().get(i).getName().contains(customerName)){
                id = DatabaseInfo.getAllCustomers().get(i).getId();
            }
        }

        return id;
    }

    /**
     *  Takes the ID of a customer and returns the name of that customer
     *
     *  @param customerID
     *  @return customer
     */
    public static String findCustomerName(int customerID){
        String customer =  "";
        for(int i = 0; i < DatabaseInfo.getAllCustomers().size(); i++){
            if (DatabaseInfo.getAllCustomers().get(i).getId() == customerID){
                customer = DatabaseInfo.getAllCustomers().get(i).getName();
            }
        }
       return customer;
    }

    /**
     *  Takes the ID of a customer and returns all information for that customer
     *
     *  @param customerID
     *  @return customer
     */

    public static Customer findCustomer(int customerID){
        Customer customer = null;
        for (int i = 0; i < DatabaseInfo.getAllCustomers().size(); i++){
            if (DatabaseInfo.getAllCustomers().get(i).getId() == customerID){
                customer = DatabaseInfo.getAllCustomers().get(i);
            }
        }
        return customer;
    }

    /**
     *  Takes the name of a contact and returns all information for that contact
     *
     *  @param contactName
     *  @return contact
     */
    public static Contact findContact(String contactName) {
        Contact contact = null;
        for(int i = 0; i < DatabaseInfo.getAllContacts().size(); i++){
            if (DatabaseInfo.getAllContacts().get(i).getName().equals(contactName)){
                contact = DatabaseInfo.getAllContacts().get(i);
            }
        }
        return contact;
    }

    /**
     *  Takes the contact name and returns that contact email
     *
     *  @param contactName
     *  @return contactEmail
     */
    public static  String findContactEmail(String  contactName){
        String contactEmail = "";
        for(int i = 0; i < DatabaseInfo.getAllContacts().size(); i++){
            if (DatabaseInfo.getAllContacts().get(i).getName().equals(contactName)){
                contactEmail = DatabaseInfo.getAllContacts().get(i).getEmail();
            }
        }
        return contactEmail;
    }

    /**
     *  Alerts the logged in user of any appointments in the next 15 minutes,
     *  Alerts the logged in user if there are no meeting in the next 15 minutes.
     *
     *  @param loginID
     */
    public static void appointmentSoonCheck(int loginID){
        ResourceBundle rb = ResourceBundle.getBundle("resource/Language", Locale.getDefault());
        LocalTime current = LocalDateTime.now().toLocalTime();
        LocalDate today = LocalDateTime.now().toLocalDate();

        for(int i = 0; i < DatabaseInfo.getAllAppointment().size(); i++){
            String date = "";
            LocalDateTime start = DatabaseInfo.getAllAppointment().get(i).getStart().toLocalDateTime();
            LocalDateTime end = DatabaseInfo.getAllAppointment().get(i).getEnd().toLocalDateTime();
            String title = DatabaseInfo.getAllAppointment().get(i).getTitle();
            int customerID = DatabaseInfo.getAllAppointment().get(i).getCustomerID();
            int appID = DatabaseInfo.getAllAppointment().get(i).getId();
            String customer = findCustomerName(customerID);
            String contact = DatabaseInfo.getAllAppointment().get(i).getContact();
            String contactEmail = findContactEmail(contact);
            if (start.toLocalDate().equals(end.toLocalDate())){
                date = start.toLocalDate().toString();
            }
            else{
                date = start.toLocalDate().toString() + " - " + end.toLocalDate().toString();
            }

            if(DatabaseInfo.getAllAppointment().get(i).getUserID() == loginID &&
                    (current.isAfter(start.toLocalTime().minusMinutes(15)) && current.isBefore(start.toLocalTime()))&&
                    (today.isAfter(start.toLocalDate().minusDays(1)) && today.isBefore(end.toLocalDate().plusDays(1)))){
                ButtonType ok = new ButtonType(rb.getString("Ok"), ButtonBar.ButtonData.OK_DONE);
                ButtonType cancel = new ButtonType(rb.getString("Cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, rb.getString( "TheContactForThisAppointmentIs") + "\n" + contact + ": " + contactEmail,ok,cancel);
                alert.setTitle(rb.getString( "AppointmentNoticeForUserID") + " " + loginID + "!");
                alert.setHeaderText(rb.getString("AppointmentNumber") + appID + " \" " + title + " \" \n" + rb.getString("with") + " " + customer + rb.getString("beginsInTheNext15Minutes") + "\n"
                        + "Date: " + date + "\nTime: " + start.toLocalTime() + "-"+ end.toLocalTime() );
                alert.showAndWait();
                return;
            }

        }
        ButtonType ok = new ButtonType(rb.getString("Ok"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType(rb.getString("Cancel"), ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "",ok,cancel);
        alert.setTitle(rb.getString("AppointmentNoticeForUserID") + " " + loginID + "!");
        alert.setHeaderText(rb.getString("YouHaveNoAppointmentsWithingTheNext15Minutes"));
        alert.showAndWait();
        return;
    }

}
