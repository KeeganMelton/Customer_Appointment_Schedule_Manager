/**
 *  @auther Keegan Melton
 *  User Class
 *  Holds information from the "Users" table in the Database
 */
package model;

public class User {
    private int userID;
    private String userName;
    private String password;

    public User(int userID, String userName, String password){
        this.userID = userID;
        this.userName = userName;
        this.password = password;
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
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
