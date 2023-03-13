package model;

public class firstLevel {
    private int divID;
    private String divName;
    private int countryID;

    public firstLevel(int divID, String divName, int countryID){
        this.divID = divID;
        this.divName = divName;
        this.countryID = countryID;
    }

    public int getDivID() {
        return divID;
    }

    public void setDivID(int divID) {
        this.divID = divID;
    }

    public String getDivName() {
        return divName;
    }

    public void setDivName(String divName) {
        this.divName = divName;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
}
