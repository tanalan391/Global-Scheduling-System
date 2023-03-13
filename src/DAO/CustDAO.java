package DAO;

import javafx.collections.ObservableList;
import model.customer;
import java.sql.*;
import javafx.collections.FXCollections;


public abstract class CustDAO {

    public static ObservableList<customer> customerList = FXCollections.observableArrayList();


    public static ObservableList<customer> getAllCustomers(){

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
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                int divID = rs.getInt("Division_ID");

                customer c = new customer(customerID, name, address, postalCode, phoneNumber,createDate, lastUpdate,divID);
                customerList.add(c);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return customerList;
    }


    public static int insert(String name, int divisionID) throws SQLException{

        String sql = "INSERT INTO client_schedule.customers (Customer_Name, Division_ID) VALUES(?,? )";
        PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        ps.setInt(2,divisionID);
        int rowsAffected = ps.executeUpdate();


        return rowsAffected;
    }

    public static int update(int customerID, String name) throws SQLException{

        String sql = "UPDATE client_schedule.customers SET Customer_Name = ? WHERE Customer_ID = ?";
        PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
        ps.setInt(2, customerID);
        ps.setString(1, name);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }











}
