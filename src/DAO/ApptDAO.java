package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.ResourceBundle;

public class ApptDAO {
    public static ObservableList<appointment> apptList = FXCollections.observableArrayList();
    public static void addAppt(appointment appointment){apptList.add(appointment);}
    public static void updateAppt(appointment appointment, int index){}
    public static void delAppt(int index){apptList.remove(index);}


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

    public static int newAppt(appointment appointment) throws SQLException{
        apptList.add(appointment);
        String title = appointment.getTitle();
        String desc = appointment.getDesc();
        String loc = appointment.getLoc();
        String type = appointment.getType();
        Timestamp start = Timestamp.from(appointment.getStart().toInstant((ZoneOffset) ZoneId.systemDefault()));
        Timestamp end = Timestamp.from(appointment.getEnd().toInstant((ZoneOffset) ZoneId.systemDefault()));
        Timestamp createDate = appointment.getCreateDate();
        int custID = appointment.getCustID();
        int userID = appointment.getUserID();
        int conID = appointment.getConID();

        String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Create_Date, Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?,? )";
        PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, desc);
        ps.setString(3, loc );
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setTimestamp(7, createDate);
        ps.setInt(8,custID);
        ps.setInt(9,userID);
        ps.setInt(10,conID);
        int rowsAffected = ps.executeUpdate();

        return rowsAffected;
    }

    public static int editAppt(appointment appointment, int Index) throws SQLException{

        int apptID = appointment.getApptID();
        String title = appointment.getTitle();
        String desc = appointment.getDesc();
        String loc = appointment.getLoc();
        String type = appointment.getType();
        Timestamp start = Timestamp.from(appointment.getStart().toInstant((ZoneOffset) ZoneId.systemDefault()));
        Timestamp end = Timestamp.from(appointment.getEnd().toInstant((ZoneOffset) ZoneId.systemDefault()));
        Timestamp lastUpdate = appointment.getLastUpdate();
        int custID = appointment.getCustID();
        int userID = appointment.getUserID();
        int conID = appointment.getConID();

        String sql = "UPDATE client_schedule.customers SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, desc);
        ps.setString(3, loc );
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setTimestamp(7, lastUpdate);
        ps.setInt(8,custID);
        ps.setInt(9,userID);
        ps.setInt(10,conID);
        ps.setInt(11,apptID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
