/**
 *  @auther Keegan Melton
 *  NationalDivision Class
 *  Holds information from the "First_Level_Divisions" table in the Database
 */
package model;

public class NationalDivision {
    private int divisionID;
    private String divisionName;
    private int countryID;

    public NationalDivision(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    /**
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @param divisionID
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * @return divisionName
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @param divisionName
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * @return countryID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * @param countryID
     */
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
}
