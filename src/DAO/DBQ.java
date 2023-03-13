package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBQ {

    public static void getAll(){
        CustDAO.getAllCustomers();
        ApptDAO.getApptList();
        getFirstLevelList();
        getCountryList();
    }

    public static ObservableList<firstLevel> firstLevelList = FXCollections.observableArrayList();
    public static ObservableList<country> countryList = FXCollections.observableArrayList();
    public static ObservableList<country> cList1 = FXCollections.observableArrayList();
    public static ObservableList<country> cList2 = FXCollections.observableArrayList();
    public static ObservableList<country> cList3 = FXCollections.observableArrayList();

    public static ObservableList<firstLevel> getFirstLevelList(){
     try {
         String sql = "SELECT * FROM client.schedule.first_level_divisions";
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

    public static ObservableList<country> getCountryList(){
        try {
            String sql = "SELECT * FROM client_schedule.countries";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");

                country country = new country(countryID, countryName);
                countryList.add(country);
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return countryList;
    }
}
