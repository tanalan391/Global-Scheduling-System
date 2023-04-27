package model;

/** First Level class. Data for instances of this class is obtained from the database and stored during runtime.*/
public class firstLevel {
    private int divID;
    private String divName;
    private int countryID;

    /**First Level constructor method. Takes variables as values and creates an instance of firstLevel.*/
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

    @Override
    public String toString(){
        return (divName);
    }
}
