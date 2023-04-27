package model;

/** Country class. Data for instances of this class is obtained from the database and stored during runtime.*/
public class country {
    private int countryID;
    private String countryName;

    /**Country constructor method. Takes variables as values and creates an instance of country.*/
    public country (int countryID, String countryName){
        this.countryID = countryID;
        this.countryName = countryName;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString(){
        return (countryName);
    }
}
