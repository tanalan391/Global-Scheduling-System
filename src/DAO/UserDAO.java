package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.io.*;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**User Data Access Object Class. This class contains the methods that are used to access information from the Database and the Observable lists where information is temporarily stored for interaction during runtime.*/
public class UserDAO {
    private static ObservableList<user> userList = FXCollections.observableArrayList();
    private static user currentUser;
    public static user getCurrentUser(){return currentUser;}

    /**getUserList Method. Queries the Database for all users and stores them in an observable list for display and interaction by the user.
     @return userList Returns an Observable list of all users.
     */
    public static ObservableList<user> getUserList() {
        ObservableList<user> userList = FXCollections.observableArrayList();
        if (userList.size() < 1) {
            try {
                String sql = "SELECT * FROM client_schedule.users";
                PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    int userID = rs.getInt("User_ID");
                    String userName = rs.getString("User_Name");
                    String password = rs.getString("Password");
                    Timestamp createDate = rs.getTimestamp("Create_Date");
                    String createdBy = rs.getString("Created_By");
                    Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                    String lastUpdatedBy = rs.getString("Last_Updated_By");

                    user u = new user(userID, userName, password, createDate, createdBy, lastUpdate, lastUpdatedBy);
                    userList.add(u);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return userList;
        }
        else {return userList;}
   }

   /**checkPassword Method. This method is called only in the Login interface. When the user clicks the login button, input to the username and password fields are passed into the method to verify if the input matches a username and password set in the Database.
    @return boolean Returns True if the user input matches a username and password set in the Database, returns false if it is incorrect or not a match.
    */
    public static boolean checkPassword(String uName, String pWord) throws SQLException, IOException {
        try {
            String sql = "SELECT * FROM users WHERE User_Name = ?";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ps.setString(1,uName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                int dbUID = rs.getInt("User_ID");
                String dbUName = rs.getString("User_Name");
                String dbPassword = rs.getString("Password");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                if (dbUName.equals(uName) && dbPassword.equals(pWord)){
                    FileWriter file = new FileWriter(".idea/login_activity.txt",true);
                    PrintWriter print = new PrintWriter(file);
                    print.println("Username: "+uName+", Password: "+pWord+", "+Timestamp.from(Instant.now())+", "+"Login: Successful");
                    print.close();

                    currentUser = new user(dbUID,dbUName,dbPassword, createDate, createdBy, lastUpdate, lastUpdatedBy);
                    return true;
                }
                else{return false;}
            }

        }catch (SQLException | IOException e){e.printStackTrace();}
        FileWriter file = new FileWriter(".idea/login_activity.txt",true);
        PrintWriter print = new PrintWriter(file);
        print.println("Username: "+uName+", Password: "+pWord+", "+Timestamp.from(Instant.now())+", "+"Login: Failed");
        print.close();
        return false;
    }
}
