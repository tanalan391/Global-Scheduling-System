package controller;

import DAO.ApptDAO;
import DAO.UserDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.appointment;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;
import java.time.ZoneId;

/**Login Class. Controller class related to the Login screen interface. Responsible for obtaining and calling methods to verify username and password, as well as translate the page into the system default language.*/
public class Login implements Initializable {
    public TextField userName;
    public PasswordField password;
    public Label userZoneID;
    public Label userNameLabel;
    public Label passwordLabel;
    public Label welcomeLabel;
    public Button loginButton;
    ResourceBundle rb = ResourceBundle.getBundle("resources", Locale.getDefault());


    /**initialize Method. Sets Labels to the System Default language.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            loginButton.setText(rb.getString("Login"));
            passwordLabel.setText(rb.getString("Password"));
            userNameLabel.setText(rb.getString("Username"));
            welcomeLabel.setText(rb.getString("Welcome"));

        userZoneID.setText(ZoneId.systemDefault().getId());
    }

    /**onLogin Method. Upon user click of the Login button, this method obtains the input username and password, and calls methods to verify if it matches Database credentials, Methods to alert the user about upcoming appointments, and throws errors if the login info does not match the database.*/
    public void onLogin(ActionEvent actionEvent) throws IOException, SQLException {
        String uName = userName.getText();
        String pWord = password.getText();
        if (UserDAO.checkPassword(uName, pWord) == true){

                        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root, 1000, 500);
                        stage.setTitle("Global Scheduling System");
                        stage.setScene(scene);
                        stage.show();

            LocalDateTime now = LocalDateTime.now();
            ObservableList<appointment> upcomingAppts = ApptDAO.getUserAppts(UserDAO.getCurrentUser().getUserID());


                 for (int i = 0; i< upcomingAppts.size(); i++){
                     LocalDateTime apptTime = LocalDateTime.from(upcomingAppts.get(i).getStart().atZone(ZoneId.systemDefault()));
                     long apptTimeNum = ChronoUnit.MINUTES.between(now,apptTime);
                     
                     if (apptTimeNum <= 15 && apptTimeNum >= 0){
                         Alert alert = new Alert(Alert.AlertType.WARNING,"You have an appointment about to start!\nAppointment ID: "+upcomingAppts.get(i).getApptID()+
                                 "\nDate: "+apptTime.toLocalDate()+"\nTime: "+apptTime.toLocalTime());
                         alert.showAndWait();
                     }
                     else if((apptTimeNum > 15 || apptTimeNum <0) && i <(upcomingAppts.size()-1)){
                         continue;
                     }
                     else {
                         Alert alert = new Alert(Alert.AlertType.INFORMATION, "No appointments starting in the next 15 minutes.");
                         alert.showAndWait();
                     }

                 }

            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, rb.getString("Error"));
                alert.showAndWait();
            }
    }
}
