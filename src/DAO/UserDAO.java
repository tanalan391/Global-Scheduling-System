package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class UserDAO {
    public static ObservableList<user> userList = FXCollections.observableArrayList();
    public static void newUser(user user){userList.add(user);}
    public static void editUser(user user, int index){}
    public static void delUser(int index){userList.remove(index);}


    public static ObservableList<user> getUserList(){
        try {
            String sql = "SELECT * client_schedule.users";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                user u = new user(userID, userName, password,createDate,createdBy, lastUpdate, lastUpdatedBy);
                userList.add(u);
            }

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return userList;
    }
}
