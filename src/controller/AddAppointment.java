package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import DAO.*;

/**AddAppointment Class. Controller class for the Add Appointment user interface.*/
public class AddAppointment implements Initializable {
    public TextField titleField;
    public TextField descField;
    public TextField locField;
    public TextField typeField;
    public ComboBox<String> startHour;
    public ComboBox<String> endHour;
    public ComboBox<String> startMinute;
    public ComboBox<String> endMinute;
    public DatePicker startDate;
    public DatePicker endDate;
    public ComboBox<customer> custCombo;
    public ComboBox<user> userCombo;
    public ComboBox<contact> contactCombo;

    /**initialize Method. Sets values for the combo box drop down menus.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startHour.setItems(DBQ.getHoursList());
        startMinute.setItems(DBQ.getMinutesList());
        endHour.setItems(DBQ.getHoursList());
        endMinute.setItems(DBQ.getMinutesList());
        custCombo.setItems(CustDAO.getAllCustomers());
        userCombo.setItems(UserDAO.getUserList());
        contactCombo.setItems(DBQ.getContactList());
    }

    /**onSave Method. Upon user clicking the Save Button, this method obtains input from the fields and combo boxes and calls methods to check that all fields are filled out, and then calls methods to save the newly created appointment to the Database.*/
    public void onSave(ActionEvent actionEvent) throws IOException, SQLException, DateTimeParseException, NullPointerException {
        try {
            int apptID = 1;
            String title = titleField.getText();
            String desc = descField.getText();
            String loc = locField.getText();
            String type = typeField.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String startH = startHour.getSelectionModel().getSelectedItem();
            String startM = startMinute.getSelectionModel().getSelectedItem();
            LocalTime startT = LocalTime.parse(startH + ":" + startM);
            String endH = endHour.getSelectionModel().getSelectedItem();
            String endM = endMinute.getSelectionModel().getSelectedItem();
            String startD = startDate.getValue().toString();
            String startStr = startD + " " + startT;
            String endD = endDate.getValue().toString();
            String endStr = endD + " " + endH + ":" + endM;
            LocalDateTime start = LocalDateTime.parse(startStr, formatter);
            LocalDateTime end = LocalDateTime.parse(endStr, formatter);
            Timestamp createDate = Timestamp.from(Instant.now());
            String createdBy = UserDAO.getCurrentUser().getUserName();
            Timestamp lastUpdate = null;
            String lastUpdatedBy = null;
            int custID = custCombo.getSelectionModel().getSelectedItem().getID();
            int userID = userCombo.getSelectionModel().getSelectedItem().getUserID();
            int contID = contactCombo.getSelectionModel().getSelectedItem().getContactID();

            appointment newAppt = new appointment(apptID, title, desc, loc, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, custID, userID, contID);
            if (DBQ.checkFields(newAppt) == true) {
                if (ApptDAO.checkBizHours(newAppt) == true) {
                   if (end.isAfter(start)) {
                       if (ApptDAO.apptOverlap(ApptDAO.getCustAppts(newAppt.getCustID()), newAppt) == true) {
                           ApptDAO.newAppt(newAppt);
                           Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                           Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                           Scene scene = new Scene(root, 1000, 500);
                           stage.setTitle("Global Scheduling System");
                           stage.setScene(scene);
                           stage.show();
                       } else {
                           Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Appointment Time!\n This customer has a preexisting appointment at this time.\n Please select a different time.");
                           alert.showAndWait();
                       }
                   }
                   else {
                       Alert alert = new Alert(Alert.AlertType.ERROR, "Start day and time must be before the End day and time, and duration must be greater than 0 minutes.");
                       alert.showAndWait();
                   }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "The requested time is outside of business hours.\nPlease select a time between 8:00 AM - 10:00 PM EST.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Enter data or make a selection for every field!");
                alert.showAndWait();
            }
        }
        catch (DateTimeParseException|NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Enter data or make a selection for every field!");
            alert.showAndWait();
        }
    }

    /**onCancel Method. When the user clicks the Cancel Button, the input info(if any) is not saved and the user is redirected to the Main Screen.*/
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 500);
        stage.setTitle("Global Scheduling System");
        stage.setScene(scene);
        stage.show();
    }

}
