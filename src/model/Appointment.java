/**
 *  @auther Keegan Melton
 *  Appointment Class
 *  Holds information from the "Appointments" table in the Database
 */
package model;

import java.sql.Timestamp;

public class Appointment {
    private int id;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int customerID;
    private int userID;
    private int contactID;

    public Appointment(int id,
                       String title,
                       String description,
                       String location,
                       String contact,
                       String type,
                       Timestamp start,
                       Timestamp end,
                       Timestamp createDate,
                       String createdBy,
                       Timestamp lastUpdate,
                       String lastUpdatedBy,
                       int customerID,
                       int userID,
                       int contactID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) { this.id = id; }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return contact
     */
    public String getContact() {return contact;}

    /**
     * @param contact
     */
    public void setContact(String contact) {this.contact = contact;}

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return start
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * @param start
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * @return end
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * @param end
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * @return createDate
     */
    public Timestamp getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    /**
     * @return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return lastUpdate
     */
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    /**
     * @param lastUpdate
     */
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     * @return lastUpdatedBy
     */
    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy
     */
    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    /**
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return contactID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }
}
