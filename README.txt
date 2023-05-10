Application Title: Customer Appointment Schedule Manager

Purpose: This application retrieves information from a database
         and allows the user to manage customer information, as
         well as schedule customer appointment.


Author: Keegan Melton
Contact Information: kmelt54@wgu.edu


Application Version: 1.0 
Date: May 4, 2023
IDE: IntelliJ 2021.1.3 (Community Edition)
JDK Version: JDK Version 17.0.1
JavaFX Version: javafx-sdk-17.0.1
Mysql Connector: Mysql-connector-java-8.0.25




Table of Contents

1. Login
2. Main Menu
	a. Appointments
	b. Customers
	c. Reports
3. Customer Information
4. Add Customer
5. Update Customer
6. Add Appointment
7. Update Appointment



Instructions:

1.Login:
___________________________________________________________________________________________________________________________________________________
  
Launch the program and enter login credentials (Username / Password) and click "Login".
Upon a successful login, a pop-up notification will display notifying if there is an appointment beginning in the next 15 minutes or not.
Once the notification has been acknowledged, the "Main Menu" will be brought to the screen. 

Clicking "Exit" will close the application.



2.Main Menu:
___________________________________________________________________________________________________________________________________________________

Clicking "Log Out" (located in the far lower right of the screen) will return the "Login" screen.
To close the application, click "Close Application". (Also located in the far lower right of the screen.)


--2a.

The top half of the screen contains a table displaying appointment information. 

Above the table are three radio buttons labeled "All Appointments", "Appointments in the next 30 days", and "Appointments in the next 7 days".
Selecting either of the latter two buttons will filter the contents of the table to only show appointments that fall under that date range. 
(Example: If today is Monday and "Appointments in the next 7 days" is selected, all appointment on Monday until the following Sunday will be shown.)

Below the table are three buttons labeled "Add Appointment", "Update Appointment", and "Remove Appointment".

"Add Appointment" will bring up the "Add Appointment" screen (More information in section 6)

"Update Appointment" will bring up the "Update Appointment" screen to update a selected appointment. 
To select an appointment, click on an appointment in the table. (More information section 7)

"Remove Appointment" will remove a selected appointment. To select an appointment, click on an appointment in the table.
A confirmation pop-up will appear and remove the appointment if confirmed. 

--2b.

The lower left of the screen contains table displaying customer information.

Below that are four buttons labeled "All Customer Information", "Add New Customer", "Update Customer", and "Remove Customer".

"All Customer Information" will bring up the "Customer Information" screen. (More information in section 3)

"Add New Customer" will bring up the "Add New Customer" screen. (More information in section 4)

"Update Customer" will bring up the "Update Customer" screen to update a selected customer. 
To select a customer, click on a customer in the table. (More information in section 5)

"Remove Customer" will remove a selected customer. To select a customer, click on a customer in the table. 
A confirmation pop-up will appear and notify the number of appointments with that customer will also be removed.
Once confirmed, the customer and all associated appointments will be removed. 

--2c.

The lower right of the screen contains three reports: "Appointment Types by Month", "Appointments per Contact", and "Appointments Total for Customer."

"Appointment Types by Month":
Select a month and appointment type from the drop-down boxes and the total number of appointments that match that criteria will be displayed 
to the right of the appointment type drop down box. 

"Appointments per Contact":
Select a contact from the drop-down box selection. Once a selection is made, click "Filter Table" to filter to table of appointments above to 
only show appointments that contain a matching contact. Additionally, the radio buttons will still function the same under this filter. (More 
information on radio buttons in section 2a). 
To clear this filter and return the appointments table back to its original state, click "Clear Filter".

"Appointment Total for Customer":
Select a customer from the drop-down box. The total number of appointments for the selected customer will be displayed to the right of the drop-down box.



3 Customer Information:
___________________________________________________________________________________________________________________________________________________

This screen contains a table displaying all the customer information.
Below are four buttons labeled "Add New Customer", "Update Customer", "Remove Customer", and "Back to Main Menu".

"Add New Customer" will bring up the "Add New Customer" screen. (More information in section 4)

"Update Customer" will bring up the "Update Customer" screen to update a selected customer. 
To select a customer, click on a customer in the table. (More information in section 5)

"Remove Customer" will remove a selected customer. To select a customer, click on a customer in the table. 
A confirmation pop-up will appear and notify the number of appointments with that customer will also be removed.
Once confirmed, the customer and all associated appointments will be removed.

"Back to Main Menu" will return the "Main Menu" screen.



4. Add Customer:
___________________________________________________________________________________________________________________________________________________

This screen allows new customers to be added into the system.
Adding a customer requires the following information: name, address, phone number, postal code, country, first-level national division. 

To add a customer, enter the customer's name, address, phone number, and postal code into the designated text field. 

The customer's country and first-level national division (labeled "National Division") are also required. The "National Division" drop down will not 
populate any options to choose unless a country is selected from the "Country" drop down first.

Once the text fields have been filled and selections from the drop downs have been made, click "Add New Customer" to add the customer.
A customer ID will be auto generated upon completion.

Clicking "Cancel" will return the "Main Menu" screen. 



5. Update Customer:
___________________________________________________________________________________________________________________________________________________

This screen allows selected customer information to be updated.
The selected customer's information will be populated in the designated areas.
 
Similar to the "Add Customer" screen, the following information is required: name, address, phone number, postal code, country, first-level national division.

The customer's name, address, phone number, and postal code can be updated using the designated text field.

The customer's country and first-level division (labeled "National Division") can also be updated using the drop-down boxes. If the country is 
changed, the "National Division" will need to be re-selected.

Once the desired changes are made, click "Update Customer" to save the changes.

Clicking "Cancel" will return the "Main Menu" screen. 



6. Add Appointment
___________________________________________________________________________________________________________________________________________________

This screen allows new appointments to be scheduled.
Adding an appointment requires the following information: title, description, location, type of appointment, contact, customer, start/end dates, 
and start/end time of day.

Title, description, and location can be entered using the designated text fields.

Type of appointment can be selected using the drop-down box labeled "Type". The "Type (Other)" text field is only required if "Other (Please Specify)" 
is selected from the drop-down. 

Contact and customer can be selected using the drop-down boxes.

To select start/end dates, click the calendar icon on the right side of the field, and a date from the displayed calendar.

Time options can be selected from the drop-down boxes and are displayed in 15 minutes intervals and 24hr time format.

Once the required information has been entered, click "Add Appointment" to add the appointment. If the timeframe of the appointment does not conflict
with an existing appointment, and the selected time is within business hours, it will be added. 

Clicking "Cancel" will return the "Main Menu" screen.



7. Update Appointment
___________________________________________________________________________________________________________________________________________________

This screen allows selected appointment information to be updated.
The selected appointments information will be populated in the designated areas.

Similar to the "Add Appointment" screen, the following information is required: title, description, location, type of appointment, contact, customer, start/end dates, 
and start/end time of day.

The appointment title, description, and location can be updated using the designated text fields.

Type of appointment can be updated using the drop-down box labeled "Type". The "Type (Other)" text field is only required if "Other (Please Specify)" 
is selected from the drop down. 

Contact and customer can be updated using the drop-down boxes.

Time options can be updated from the dropdown boxes and are displayed in 15 minutes intervals and 24hr time format.

Once the desired changes are made, click "Update Appointment" to save the changes. If the timeframe of the appointment does not conflict
with an existing appointment, and the selected time is within business hours, it will be added.

Clicking "Cancel" will return the "Main Menu" screen.




