package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ApptDAO {
    public static ObservableList<appointment> apptList = FXCollections.observableArrayList();

    public static ObservableList<appointment> getApptList(){
        try {
            String sql = "SELECT * client_schedule.appointments";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String loc = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                Timestamp createDate = rs.getTimestamp("Create_Date");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                int custID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int conID = rs.getInt("Contact_ID");

                appointment a = new appointment(apptID, title, desc, loc, type, start, end, createDate, lastUpdate, custID, userID, conID);
                apptList.add(a);
            }

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return apptList;
    }
}
