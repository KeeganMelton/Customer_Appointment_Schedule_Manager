/**
 *  @auther Keegan Melton
 *  DatabaseInfo Class
 *  Holds Observable lists of information from the database
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseInfo {

    // Creates observable list of Customers
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    // Creates observable list of Appointments
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    // Creates observable list of Users
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();

    // Creates observable list of Countries
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();

    // Creates observable list of First-Level Divisions
    private static ObservableList<NationalDivision> allDivision = FXCollections.observableArrayList();

    // Creates observable list of Contacts
    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList();

    /**
     * @param customer
     */
    public static void addCustomer(Customer customer){allCustomers.add(customer);}

    /**
     * @param appointment
     */
    public static void addAppointment(Appointment appointment){allAppointments.add(appointment);}

    /**
     * @param user
     */
    public static void addUser(User user){allUsers.add(user);}

    /**
     * @param country
     */
    public static void addCountry(Country country){allCountries.add(country);}

    /**
     * @param division
     */
    public static void addDivision(NationalDivision division){allDivision.add(division);}
    /**
     * @param contact
     */
    public static void addContact(Contact contact){allContacts.add(contact);}



    /**
     * @return allCustomers
     */
    public static ObservableList<Customer> getAllCustomers() {return allCustomers;}

    /**
     * @return allAppointments
     */
    public static ObservableList<Appointment> getAllAppointment(){return allAppointments;}

    /**
     * @return allUsers
     */
    public static ObservableList<User> getAllUsers() {return allUsers;}

    /**
     * @return allCountries
     */
    public static ObservableList<Country> getAllCountries() {return allCountries;}

    /**
     * @return allDivision
     */
    public static ObservableList<NationalDivision> getAllDivision() {return allDivision;}

    /**
     * @return allContacts
     */
    public static ObservableList<Contact> getAllContacts() {return allContacts;}


    /**
     * @param indexNum
     * @param customerToUpdate
     */
    public static void updateCustomer(int indexNum, Customer customerToUpdate) { allCustomers.set(indexNum,customerToUpdate);}

    /**
     * @param indexNum
     * @param appointmentToUpdate
     */
    public static void updateAppointment(int indexNum, Appointment appointmentToUpdate) {allAppointments.set(indexNum, appointmentToUpdate);}



    /**
     * @param deleteAppointment
     */
    public static void deleteAppointment(Appointment deleteAppointment) {allAppointments.remove(deleteAppointment);}

    /**
     * @param deleteCustomer
     */
    public static void deleteCustomer(Customer deleteCustomer) {allCustomers.remove(deleteCustomer);}
}
