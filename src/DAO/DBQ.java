package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**Database Queries Class. This contains database access methods for the country, contact, and firstLevel classes, in addition to various helper methods.*/
public class DBQ {

    private static final ObservableList<firstLevel> firstLevelList = FXCollections.observableArrayList();
    private static final ObservableList<country> countryList = FXCollections.observableArrayList();
    private static final ObservableList<firstLevel> flList1 = FXCollections.observableArrayList();
    private static final ObservableList<firstLevel> flList2 = FXCollections.observableArrayList();
    private static final ObservableList<firstLevel> flList3 = FXCollections.observableArrayList();
    private static final ObservableList<contact> contactList = FXCollections.observableArrayList();
    private static final ObservableList<String> hoursList = FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24");
    private static final ObservableList<String> minutesList = FXCollections.observableArrayList("00", "15", "30", "45");
    private static final ObservableList<String> times = FXCollections.observableArrayList("< 15 Minutes","15 Minutes", "30 Minutes", "1 Hour", "> 1 Hour");
    private static final ObservableList<String> months = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

    public static ObservableList<String> getHoursList(){return hoursList;}
    public static ObservableList<String> getMinutesList(){return minutesList;}
    public static ObservableList<String> getTimesList(){return times;}
    public static ObservableList<String> getMonthsList(){return months;}

     /**getFirstLevelList Method. Queries the Database for all items held in the First-Level Divisions and stores them in an observable list for display and interaction by the user.
     @return firstLevelList Returns an Observable list of all First Levels.
     */
     public static ObservableList<firstLevel> getFirstLevelList(){
     try {
         String sql = "SELECT * FROM client_schedule.first_level_divisions";
         PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
         ResultSet rs = ps.executeQuery();

         while (rs.next()){
             int divID = rs.getInt("Division_ID");
             String divName = rs.getString("Division");
             int countryID = rs.getInt("Country_ID");

             firstLevel fl = new firstLevel(divID, divName, countryID);
             firstLevelList.add(fl);
         }
     }catch (SQLException throwables){
         throwables.printStackTrace();
     }
        return firstLevelList;
    }

    /**getFLList1 Method. Filters the First Level list by Division ID 1.
     @return fLList1 Returns an Observable list of all First Levels with Division ID 1.
     */
    public static ObservableList<firstLevel> getFLList1(){
        if (flList1.size() > 1){return flList1;}
        else {
            for (int i = 0; i < firstLevelList.size(); i++) {
                firstLevel fl = firstLevelList.get(i);

                if (fl.getCountryID() == 1) {
                    flList1.add(fl);
                }
            }
            return flList1;
        }
    }

    /**getFLList2 Method. Filters the First Level list by Division ID 2.
     @return fLList2 Returns an Observable list of all First Levels with Division ID 2.
     */
    public static ObservableList<firstLevel> getFlList2(){
        if (flList2.size() > 1){return flList2;}
        else {
            for (int i = 0; i < firstLevelList.size(); i++) {
                firstLevel fl = firstLevelList.get(i);

                if (fl.getCountryID() == 2) {
                    flList2.add(fl);
                }
            }
            return flList2;
        }
    }

    /**getFLList3 Method. Filters the First Level list by Division ID 3.
     @return fLList3 Returns an Observable list of all First Levels with Division ID 3.
     */
    public static ObservableList<firstLevel> getFlList3(){
        if (flList3.size() > 1){return flList3;}
        else {
            for (int i = 0; i < firstLevelList.size(); i++) {
                firstLevel fl = firstLevelList.get(i);

                if (fl.getCountryID() == 3) {
                    flList3.add(fl);
                }
            }
            return flList3;
        }
    }

    /**getCountryList Method. Queries the Database for all items held in the Countries section and stores them in an observable list for display and interaction by the user.
     @return countryList Returns an Observable list of all Countries.
     */
    public static ObservableList<country> getCountryList(){
        if (countryList.size() > 1){return countryList;}
        else {
            try {
                String sql = "SELECT * FROM client_schedule.countries";
                PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int countryID = rs.getInt("Country_ID");
                    String countryName = rs.getString("Country");

                    country country = new country(countryID, countryName);
                    countryList.add(country);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return countryList;
        }
    }

    /**getContactList Method. Queries the Database for all items held in the Contacts section and stores them in an observable list for display and interaction by the user.
     @return contactList Returns an Observable list of all contacts.
     */
    public static ObservableList<contact> getContactList(){
        ObservableList<contact> contactList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM contacts";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int contactID = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                String email = rs.getString("Email");

                contact contact = new contact(contactID, contactName, email);
                contactList.add(contact);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        return contactList;
    }

    /**fLSelect Method. Used to identify the Division Name associated with the Division ID held as a variable of a customer object.
     @param divID passes in the division ID variable from the selected customer.
     @return fl Returns the identified first level object. 
     */
    public static firstLevel fLSelect(int divID) {
        for (int i = 0; i < firstLevelList.size(); i++){
            firstLevel fl = firstLevelList.get(i);
            if (fl.getDivID() == divID){
                return fl;
            }
        }
        return null;
    }

    /**fLType Method. Used to identify the Division Type(ie State, Constituent Country, or Province) associated with the Division ID held as a variable of a customer object.
     @param divID passes in the division ID variable from the selected customer.
     @return String of Division Type
     */
    public static String fltype(int divID){
        for (int i = 0; i<firstLevelList.size(); i++){
            firstLevel fl = firstLevelList.get(i);
            if (fl.getDivID() == divID){
                for (int x = 0; x < countryList.size(); x++){
                    country c = countryList.get(x);
                    if (fl.getCountryID() == 1){
                        return "State"; }
                    if (fl.getCountryID() == 2){
                        return "Constituent Country"; }
                    if (fl.getCountryID() == 3){
                        return "Province"; }
                }
            }
        }
        return null;
    }

    /**cSelect Method. Used to identify the Country Name associated with the Division ID held as a variable of a customer object.
     @param divID passes in the division ID variable from the selected customer.
     @return c Returns the identified country object.
     */
    public static country cSelect(int divID){
        for (int i = 0; i < firstLevelList.size(); i++){
            firstLevel fl = firstLevelList.get(i);
            if (fl.getDivID() == divID){
                for (int x = 0; x<countryList.size(); x++){
                    country c = countryList.get(x);
                    if (fl.getCountryID() == c.getCountryID()){
                        return c;
                    }
                }
            }
        }
        return null;
    }

    /**getFLList Method. Returns the appropriate firstLevel list based on the division ID of the selected customer.
     @param divID Passes in the division ID of the selected customer.
     @return fLList 1, 2, or 3 Returns an Observable list with a div ID matching the parameter.
     */
    public static ObservableList<firstLevel> getFLList(int divID){
        for (int i = 0; i < firstLevelList.size(); i++){
            firstLevel fl = firstLevelList.get(i);
            if (fl.getDivID() == divID){
                for (int x = 0; x<countryList.size(); x++){
                    country c = countryList.get(x);
                    if (fl.getCountryID() == c.getCountryID()){
                        if (c.getCountryID() == 1){
                            return getFLList1();
                        }
                        if (c.getCountryID() == 2){
                            return getFlList2();
                        }
                        if (c.getCountryID() == 3){
                            return getFlList3();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**custSelect Method. Returns the appropriate customer based on the customer ID of the selected appointment.
     @param custID Passes in the customer ID of the selected appointment.
     @return c Returns the customer with matching customer ID.
     */
    public static customer custSelect(int custID){
        for (int i = 0; i < CustDAO.getAllCustomers().size(); i++){
            customer c = CustDAO.getAllCustomers().get(i);
            if (c.getID() == custID){
                return c;
            }
        }
        return null;
    }

    /**userSelect Method. Returns the appropriate user based on the user ID of the selected appointment.
     @param userID Passes in the user ID of the selected appointment.
     @return u Returns the user with matching user ID.
     */
    public static user userSelect(int userID){
        for (int i = 0; i < UserDAO.getUserList().size(); i++){
            user u = UserDAO.getUserList().get(i);
            if (u.getUserID() == userID){
                return u;
            }
        }
        return null;
    }

    /**contSelect Method. Returns the appropriate contact based on the contact ID of the selected appointment.
     @param contID Passes in the contact ID of the selected appointment.
     @return c Returns the contact with matching contact ID.
     */
    public static contact contSelect(int contID){
        for (int i = 0; i < getContactList().size(); i++){
            contact c = getContactList().get(i);
            if (c.getContactID() == contID){
                return c;
            }
        }
        return null;
    }

    /**isNum Method. Used to determine the input type for customer and appointment search functions.
     @param string Passes in the input string the user has entered into the customer or appointment search fields.
     @return boolean Returns True if the String passed in can be parsed to an Integer, and returns false if the field is empty or not able to parse to an integer.
     */
    public static boolean isNum(String string){
        if (string == null){return false;}
        try {
            int i = Integer.parseInt(string);
        }catch (NumberFormatException e){return false;}
        return true;
    }

    /**checkFields Method. Used to verify that the user has entered input for all fields in the Add or Edit customer interface.
     @param cust Passes in the newly created customer object.
     @return Boolean Returns True if all fields have input, returns false if any field is empty.
     */
    public static boolean checkFields(customer cust){
        if (cust.getName().isEmpty()){return false;}
        if (cust.getAddress().isEmpty()){return false;}
        if (cust.getPostalCode().isEmpty()){return false;}
        if (cust.getPhoneNumber().isEmpty()){return false;}
        else {return true;}
    }

    /**checkFields Method. Used to verify that the user has entered input for all fields in the Add or Edit appointment interface.
     @param appt Passes in the newly created appointment object.
     @return Boolean Returns True if all fields have input, returns false if any field is empty.
     */
    public static boolean checkFields(appointment appt){
        if (appt.getTitle().isEmpty()){return false;}
        if (appt.getDesc().isEmpty()){return false;}
        if (appt.getLoc().isEmpty()){return false;}
        if (appt.getType().isEmpty()){return false;}
        if (appt.getStart() == null){return false;}
        if (appt.getEnd() == null){return false;}
        if (appt.getCustID() <= 0 ){return false;}
        if (appt.getUserID() <= 0){return false;}
        if (appt.getConID() <= 0){return false;}
        else {return true;}
    }

}
