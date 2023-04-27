package main;

import DAO.DBC;
import DAO.DBQ;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

/** Main/Starting class for the Global Scheduling Application.*/
public class main extends Application {

    /** Start method. Sets up the starting display of the Login page.*/
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        primaryStage.setTitle("Global Scheduling System");
        primaryStage.setScene(new Scene(root, 550, 450));
        primaryStage.show();
    }

    /** Main method. Initiates the Database connection utilizing a method from the DBC(Database Connection) class. Follows by launching aruguments and returns to close the Database connection when the application is closed. */
    public static void main(String[] args) throws SQLException {
        DBC.startConnection();
        launch(args);
        DBC.closeConnection();

    }
}
