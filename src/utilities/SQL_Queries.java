/**
 *  @auther Keegan Melton
 *  SQL_Queries Class
 *  Runs SQL queries
 */
package utilities;

import model.*;

import java.sql.*;

public abstract class SQL_Queries {

    /**
     *  Select Query
     *  Gets informaiotn from "Customers" and "First_Level_Divisions "table
     *  Adds it to allCustomers observable list
     *
     *  @throws SQLException
     */
    public static void selectAllCustomer() throws SQLException {
        String sql = "select customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, \n" +
                "customers.Phone, customers.Create_Date, customers.Created_by, customers.Last_Update, customers.Last_Updated_By, \n" +
                "first_level_divisions.Division \n" +
                "FROM client_schedule.customers\n" +
                "join first_level_divisions\n" +
                "ON client_schedule.customers.Division_ID = client_schedule.first_level_divisions.Division_ID;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next() == true){
            int id = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            Timestamp createdDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            String nationalDivision = rs.getString("Division");

            DatabaseInfo.addCustomer(new Customer(
                    id,
                    name,
                    address,
                    postalCode,
                    phone,
                    createdDate,
                    createdBy,
                    lastUpdate,
                    lastUpdatedBy,
                    nationalDivision));
        }
    }

    /**
     *  Select Query
     *  Gets information from "Appointments" and "Contacts" table
     *  Adds it to allAppointments observable list
     *
     *  @throws SQLException
     */
    public static void selectAllAppointments() throws SQLException{
        String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By,\n" +
                "\t   Last_Update, Last_Updated_By, Customer_ID, User_ID, appointments.Contact_ID, contacts.Contact_Name \n" +
                "       FROM client_schedule.appointments\n" +
                "       join contacts\n" +
                "       on appointments.Contact_ID = contacts.Contact_ID;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next() == true){
            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            String createdBy = rs.getString("Created_By");
            Timestamp lastUpdate = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            String contact = rs.getString("Contact_Name");

            DatabaseInfo.addAppointment(new Appointment(
                    id,
                    title,
                    description,
                    location,
                    contact,
                    type,
                    start,
                    end,
                    createDate,
                    createdBy,
                    lastUpdate,
                    lastUpdatedBy,
                    customerID,
                    userID,
                    contactID));
        }
    }

    /**
     *  Select Query
     *  Gets information from "Users" table
     *  Adds it to allUsers observable list
     *
     *  @throws SQLException
     */
   public static void selectAllUsers() throws SQLException {
        String sql = "SELECT * FROM USERS;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next() == true){
            int id = rs.getInt("User_ID");
            String name = rs.getString("User_Name");
            String password = rs.getString("Password");

           DatabaseInfo.addUser(new User(id, name, password));
       }
   }

    /**
     *  Select Query
     *  Gets information from "Countries" table
     *  Adds it to allCountries observable list
     *
     *  @throws SQLException
     */
   public static void selectAllCountries() throws SQLException {
        String sql = "SELECT * FROM COUNTRIES;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next() == true){
            int id = rs.getInt("Country_ID");
            String name = rs.getString("Country");

            DatabaseInfo.addCountry(new Country(id, name));
        }
   }

    /**
     *  Select Query
     *  Gets information from "first_level_division" table
     *  Adds it to allDivision observable list
     *
     *  @throws SQLException
     */
   public static void selectAllNationalDivision() throws SQLException {
        String sql = "SELECT * FROM FIRST_LEVEL_DIVISIONS;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next() == true){
            int divisionID = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryID = rs.getInt("Country_ID");

            DatabaseInfo.addDivision(new NationalDivision(divisionID,division,countryID));
        }
   }

    /**
     *  Select Query
     *  Gets information from "Contacts" table
     *  Adds it to allContacts observable list
     *  @throws SQLException
     */
   public static void selectAllContacts() throws SQLException {
        String sql = "SELECT * FROM CONTACTS;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next() == true) {
            int contactID = rs.getInt("Contact_ID");
            String name = rs.getString("Contact_Name");
            String email = rs.getString("Email");

            DatabaseInfo.addContact(new Contact(contactID,name,email));
        }
    }


    /**
     *  Insert Query
     *  Inserts New Customer Information into "Customers" table
     *
     *  @param name
     *  @param address
     *  @param postal
     *  @param phone
     *  @param user
     *  @param nationalDivision
     *  @return rowsAffected
     *  @throws SQLException
     */
    public static int insertNewCustomer(String name, String address, String postal, String phone, String user, int nationalDivision) throws SQLException {
        String sql = "insert into client_schedule.customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, \n" +
                "Created_by, Last_Update, Last_Updated_By, Division_ID)\n" +
                "values (?, \n" +
                "?, \n" +
                "?, \n" +
                "?, \n" +
                "current_timestamp(), \n" +
                "?, \n" +
                "current_timestamp(), \n" +
                "?, \n" +
                "?);";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postal);
        ps.setString(4, phone);
        ps.setString(5, user);
        ps.setString(6, user);
        ps.setInt(7, nationalDivision);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

   }

    /**
     *  Select Query
     *  Gets new customer information from "Customers" and "First_Level_Divisions "table
     *  Adds new customer to allCustomers observable list
     *
     *  @throws SQLException
     */
   public static void selectAddNewCustomer() throws SQLException {
        String sql = "select client_schedule.customers.Customer_ID, client_schedule.customers.Customer_Name, \n" +
                "       client_schedule.customers.Address, client_schedule.customers.Postal_Code,\n" +
                "       client_schedule.customers.Phone, client_schedule.customers.Create_Date, \n" +
                "       client_schedule.customers.Created_by, client_schedule.customers.Last_Update, \n" +
                "       client_schedule.customers.Last_Updated_By, client_schedule.first_level_divisions.Division\n" +
                "\t   FROM client_schedule.customers\n" +
                "\t   join first_level_divisions\n" +
                "\t   ON client_schedule.customers.Division_ID = client_schedule.first_level_divisions.Division_ID\n" +
                "\t   where client_schedule.customers.customer_id = (select max(client_schedule.customers.Customer_ID) \n" +
                "       from client_schedule.customers);";
       PreparedStatement ps = JDBC.connection.prepareStatement(sql);
       ResultSet rs = ps.executeQuery();
       while (rs.next() == true){
           int id = rs.getInt("Customer_ID");
           String name = rs.getString("Customer_Name");
           String address = rs.getString("Address");
           String postalCode = rs.getString("Postal_Code");
           String phone = rs.getString("Phone");
           Timestamp createdDate = rs.getTimestamp("Create_Date");
           String createdBy = rs.getString("Created_By");
           Timestamp lastUpdate = rs.getTimestamp("Last_Update");
           String lastUpdatedBy = rs.getString("Last_Updated_By");
           String nationalDivision = rs.getString("Division");

           DatabaseInfo.addCustomer(new Customer(
                   id,
                   name,
                   address,
                   postalCode,
                   phone,
                   createdDate,
                   createdBy,
                   lastUpdate,
                   lastUpdatedBy,
                   nationalDivision));
       }
   }

    /**
     *  Update Query
     *  Updates Customer information in "Customers" table
     *
     *  @param id
     *  @param name
     *  @param address
     *  @param postal
     *  @param phone
     *  @param user
     *  @param nationalDivision
     *  @return rowsAffected
     *  @throws SQLException
     */
   public static int updateCustomerInfo(int id,String name, String address, String postal, String phone, String user, int nationalDivision) throws SQLException {
       String sql = "update client_schedule.customers\n" +
                "set client_schedule.customers.customer_name = ?,\n" +
                "client_schedule.customers.Address = ?,\n" +
                "client_schedule.customers.Postal_Code = ?,\n" +
                "client_schedule.customers.Phone = ?,\n" +
                "client_schedule.customers.Last_Update = current_timestamp(),\n" +
                "client_schedule.customers.Last_Updated_By = ?,\n" +
                "client_schedule.customers.Division_ID = ?\n" +
                "where client_schedule.customers.Customer_ID = ?;";
       PreparedStatement ps = JDBC.connection.prepareStatement(sql);
       ps.setString(1, name);
       ps.setString(2, address);
       ps.setString(3, postal);
       ps.setString(4, phone);
       ps.setString(5, user);
       ps.setInt(6, nationalDivision);
       ps.setInt(7, id);
       int rowsAffected = ps.executeUpdate();
       return rowsAffected;
   }

    /**
     *  Insert Query
     *  Inserts New Appointment Information into "Appointments" table
     *
     *  @param title
     *  @param description
     *  @param location
     *  @param type
     *  @param start
     *  @param end
     *  @param user
     *  @param customerID
     *  @param userID
     *  @param contactID
     *  @return rowsAffected
     *  @throws SQLException
     */
   public static int insertNewAppointment(String title, String description, String location, String type, Timestamp start,
                                          Timestamp end, String user, int customerID, int userID, int contactID) throws SQLException {
      String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, \n" +
               "Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)\n" +
               "values (?, ?, ?, ?, ?, ?, current_timestamp(), ?,current_timestamp(), ?, ?, ?, ?);";
      PreparedStatement ps = JDBC.connection.prepareStatement(sql);
      ps.setString(1,title);
      ps.setString(2, description);
      ps.setString(3, location);
      ps.setString(4, type);
      ps.setTimestamp(5,start);
      ps.setTimestamp(6,end);
      ps.setString(7,user);
      ps.setString(8,user);
      ps.setInt(9, customerID);
      ps.setInt(10,userID);
      ps.setInt(11,contactID);
      int rowsAffected = ps.executeUpdate();
      return rowsAffected;

   }

    /**
     *  Select Query
     *  Gets new appointment information from "Appointments" and "Contacts" table
     *  Adds new appointment to allAppointments observable list
     *
     *  @throws SQLException
     */
    public static void selectAddNewAppointment() throws SQLException {
       String sql = "SELECT Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, \n" +
               "Last_Update, Last_Updated_By, Customer_ID, User_ID, appointments.Contact_ID, contacts.Contact_Name\n" +
               "FROM client_schedule.appointments\n" +
               "join contacts\n" +
               "on appointments.Contact_ID = contacts.Contact_ID\n" +
               "WHERE Appointment_ID = (select max(Appointment_ID) from appointments);";
       PreparedStatement ps = JDBC.connection.prepareStatement(sql);
       ResultSet rs = ps.executeQuery();
       while(rs.next() == true) {
           int id = rs.getInt("Appointment_ID");
           String title = rs.getString("Title");
           String description = rs.getString("Description");
           String location = rs.getString("Location");
           String type = rs.getString("Type");
           Timestamp start = rs.getTimestamp("Start");
           Timestamp end = rs.getTimestamp("End");
           Timestamp createDate = rs.getTimestamp("Create_Date");
           String createdBy = rs.getString("Created_By");
           Timestamp lastUpdate = rs.getTimestamp("Last_Update");
           String lastUpdatedBy = rs.getString("Last_Updated_By");
           int customerID = rs.getInt("Customer_ID");
           int userID = rs.getInt("User_ID");
           int contactID = rs.getInt("Contact_ID");
           String contact = rs.getString("Contact_Name");

            DatabaseInfo.addAppointment(new Appointment(
                   id,
                   title,
                   description,
                   location,
                   contact,
                   type,
                   start,
                   end,
                   createDate,
                   createdBy,
                   lastUpdate,
                   lastUpdatedBy,
                   customerID,
                   userID,
                   contactID));
       }
    }

    /**
     *  Update Query
     *  Updates Appointment information in the "Appointments" table
     *
     *  @param appointmentID
     *  @param title
     *  @param description
     *  @param location
     *  @param type
     *  @param start
     *  @param end
     *  @param user
     *  @param customerID
     *  @param userID
     *  @param contactID
     *  @return rowsAffected
     *  @throws SQLException
     */
    public static int updateAppointment(int appointmentID, String title, String description,String location, String type,
                                        Timestamp start, Timestamp end, String user, int customerID, int userID, int contactID) throws SQLException {
        String sql = "UPDATE client_schedule.appointments\n" +
                "set title = ?,\n" +
                "Description = ?,\n" +
                "Location = ?,\n" +
                "Type = ?,\n" +
                "Start = ?,\n" +
                "End = ?,\n" +
                "Last_Update = current_timestamp(),\n" +
                "Last_Updated_By = ?,\n" +
                "Customer_ID = ?,\n" +
                "User_ID = ?,\n" +
                "Contact_ID = ?\n" +
                "WHERE appointment_id = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1,title);
        ps.setString(2,description);
        ps.setString(3,location);
        ps.setString(4,type);
        ps.setTimestamp(5,start);
        ps.setTimestamp(6,end);
        ps.setString(7,user);
        ps.setInt(8,customerID);
        ps.setInt(9,userID);
        ps.setInt(10,contactID);
        ps.setInt(11,appointmentID);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     *  Delete Query
     *  Deletes one appointment from "Appointments" table
     *
     *  @param id
     *  @return rowsAffected
     *  @throws SQLException
     */
    public static int deleteAppointmentByID(int id) throws SQLException {
       String sql = "DELETE FROM APPOINTMENTS\n" +
               "WHERE Appointment_ID = ?;";
       PreparedStatement ps = JDBC.connection.prepareStatement(sql);
       ps.setInt(1,id);
       int rowsAffected = ps.executeUpdate();
       return rowsAffected;
    }

    /**
     *  Delete Query
     *  Deletes all appointments with a specific customer from "Appointments" table
     *
     *  @param customerID
     *  @return rowsAffected
     *  @throws SQLException
     */
    public static int deleteAllAppointmentsWithCustomer(int customerID) throws SQLException {
       String sql = "DELETE FROM APPOINTMENTS\n" +
               "WHERE Customer_ID = ?;";
       PreparedStatement ps = JDBC.connection.prepareStatement(sql);
       ps.setInt(1,customerID);
       int rowsAffected = ps.executeUpdate();
       return rowsAffected;
    }

    /**
     *  Delete Query
     *  Deletes one customer from "Customers" table
     *
     *  @param customerID
     *  @return rowsAffected
     *  @throws SQLException
     */
    public static int deleteCustomer(int customerID) throws SQLException {
       String sql = "DELETE FROM CUSTOMERS \n" +
               "WHERE Customer_ID = ?;";
       PreparedStatement ps = JDBC.connection.prepareStatement(sql);
       ps.setInt(1,customerID);
       int rowsAffected = ps.executeUpdate();
       return rowsAffected;

    }
}


