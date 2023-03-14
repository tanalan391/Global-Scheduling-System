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


    public static int newCust(customer customer) throws SQLException{
        customerList.add(customer);
        String name = customer.getName();
        String address = customer.getAddress();
        String postalCode = customer.getPostalCode();
        String phone = customer.getPhoneNumber();
        Timestamp createDate = customer.getCreateDate();
        int divID = customer.getDivID();

        String sql = "INSERT INTO client_schedule.customers (Customer_Name,Address, Postal_Code, Phone, Create_Date, Division_ID) VALUES(?,?,?,?,?,? )";
        PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setTimestamp(5, createDate);
        ps.setInt(6,divID);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    public static int editCust(customer customer, int Index) throws SQLException{

        int custID = customer.getID();
        String name = customer.getName();
        String address = customer.getAddress();
        String postalCode = customer.getPostalCode();
        String phone = customer.getPhoneNumber();
        Timestamp lastUpdate = customer.getLastUpdate();
        int divID = customer.getDivID();

        String sql = "UPDATE client_schedule.customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4,phone);
        ps.setTimestamp(5,lastUpdate);
        ps.setInt(6,divID);
        ps.setInt(7,custID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }











}
