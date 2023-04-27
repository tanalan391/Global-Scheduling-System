package DAO;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.appointment;
import model.customer;
import java.sql.*;
import java.util.Locale;

import javafx.collections.FXCollections;

/**Customer Data Access Object Class. This class contains the methods that are used to access information from the Database and the Observable lists where information is temporarily stored for interaction during runtime.*/
public abstract class CustDAO {

    private static ObservableList<customer> customerList = FXCollections.observableArrayList();
    private static ObservableList<customer> customerSearch = FXCollections.observableArrayList();

    /**getAllCustomers Method. Queries the Database for all customers and stores them in an observable list for display and interaction by the user.
     @return customerList Returns an Observable list of all customers.
     */
    public static ObservableList<customer> getAllCustomers(){
        ObservableList<customer> customerList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM client_schedule.customers";

            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int customerID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phoneNumber = rs.getString("Phone");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int divID = rs.getInt("Division_ID");

                customer c = new customer(customerID, name, address, postalCode, phoneNumber,createDate, createdBy, lastUpdate, lastUpdatedBy,divID);
                customerList.add(c);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return customerList;
    }

    /**searchCustomers Method. This method allows the user to search for a specific customer by customer name.
     @param partialName Obtained from the search textfield in the Customers tab of the interface, passes in the string of user input.
     @return customerSearch Returns an observable list of all customers with Names that contain part or all of the users input.
     */
    public static ObservableList<customer> searchCustomers(String partialName){
        customerSearch.clear();
        if (DBQ.isNum(partialName) == true){
            for (customer c : getAllCustomers()){
                if (c.getID() == Integer.valueOf(partialName)){
                    customerSearch.add(c);
                }
            }
        }
        else {
            for (customer c : getAllCustomers()){
                if (c.getName().toLowerCase().contains(partialName.toLowerCase())){
                customerSearch.add(c);
                }
            }
        }
        return customerSearch;
    }

    /**newCust Method. This method is called when a new customer instance is saved and added to the Database.
     @param customer Instance of customer created on the AddCustomer interface. Passes in the object which allows access to it's variables for addition to the database.
     */
    public static void newCust(customer customer) throws SQLException{
        customerList.add(customer);
        String name = customer.getName();
        String address = customer.getAddress();
        String postalCode = customer.getPostalCode();
        String phone = customer.getPhoneNumber();
        Timestamp createDate = customer.getCreateDate();
        String createdBy = customer.getCreatedBy();
        int divID = customer.getDivID();

        String sql = "INSERT INTO client_schedule.customers (Customer_Name,Address, Postal_Code, Phone, Create_Date, Created_By, Division_ID) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setTimestamp(5, createDate);
        ps.setString(6,createdBy);
        ps.setInt(7,divID);
        ps.executeUpdate();
    }

    /**editCust Method. This method is called when a customer instance is edited and saved, to be added to the Database. The method also updates the info for the object in the runtime data.
     @param customer Instance of customer created on the EditCustomer interface. Passes in the object which allows access to it's variables for update in the database.
     @param Index Passes in the array index of the selected customer instance.
     */
    public static int editCust(customer customer, int Index) throws SQLException{
        int custID = customer.getID();
        String name = customer.getName();
        String address = customer.getAddress();
        String postalCode = customer.getPostalCode();
        String phone = customer.getPhoneNumber();
        Timestamp lastUpdate = customer.getLastUpdate();
        String lastUpdatedBy = customer.getLastUpdatedBy();
        int divID = customer.getDivID();

        try{
        String sql = "UPDATE customers SET Customer_Name= ?, Address= ?, Postal_Code= ?, Phone= ?, Last_Update= ?, Last_Updated_By= ?, Division_ID= ? WHERE Customer_ID = ?";
        PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4,phone);
        ps.setTimestamp(5,lastUpdate);
        ps.setString(6,lastUpdatedBy);
        ps.setInt(7,divID);
        ps.setInt(8,custID);
        ps.executeUpdate();
        }
        catch (SQLException e){e.printStackTrace();}
        return custID;
    }

    /**deleteCust Method. This method is called when a user selects the option to delete a customer. The customer specified is deleted from the database.
     @param customer Passes in the appointment ID of the appointment to be deleted.
     */
    public static void deleteCust(customer customer) throws SQLException{
        int custID = customer.getID();
            ObservableList<appointment> tempList = ApptDAO.getCustAppts(custID);

            while(!tempList.isEmpty()) {
                int i = 0;
                ApptDAO.deleteAppt(tempList.get(i).getApptID());
                tempList.remove(i);
            }

            String sql = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ps.setInt(1, custID);
            ps.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, customer.getName()+" and all associated appointments have been deleted.");
            alert.showAndWait();

    }
}
