package controller;

import DAO.ApptDAO;
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
import java.time.*;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

import DAO.*;

/**EditAppointment Class. Controller class for the Edit Appointment user interface.*/
public class EditAppointment implements Initializable {
    public TextField apptIDField;
    public TextField titleField;
    public TextField descField;
    public TextField locField;
    public TextField typeField;
    public ComboBox<String> startHour;
    public ComboBox<String> endHour;
    public DatePicker startDate;
    public DatePicker endDate;
    public ComboBox<customer> custCombo;
    public ComboBox<user> userCombo;
    public ComboBox<contact> contactCombo;
    public ComboBox<String> startMinute;
    public ComboBox<String> endMinute;
    private static appointment targetAppt = null;
    public static void editAppt(appointment appointment){targetAppt = appointment;}

    /**initialize Method. Sets values for the fields and combo boxes based on the appointment selected.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startHour.setItems(DBQ.getHoursList());
        startMinute.setItems(DBQ.getMinutesList());
        endHour.setItems(DBQ.getHoursList());
        endMinute.setItems(DBQ.getMinutesList());
        custCombo.setItems(CustDAO.getAllCustomers());
        userCombo.setItems(UserDAO.getUserList());
        contactCombo.setItems(DBQ.getContactList());
        targetApptData();
    }

    /**onSave Method. Upon user clicking the Save Button, this method obtains input from the fields and combo boxes and calls methods to check that all fields are filled out, and then calls methods to save the newly edited appointment info to the Database.*/
    public void onSave(ActionEvent actionEvent) throws IOException, SQLException, DateTimeParseException {
        try {
            int apptID = targetAppt.getApptID();
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
            Timestamp createDate = targetAppt.getCreateDate();
            String createdBy = targetAppt.getCreatedBy();
            Timestamp lastUpdate = Timestamp.from(Instant.now());
            String lastUpdatedBy = UserDAO.getCurrentUser().getUserName();
            int custID = custCombo.getSelectionModel().getSelectedItem().getID();
            int userID = userCombo.getSelectionModel().getSelectedItem().getUserID();
            int contID = contactCombo.getSelectionModel().getSelectedItem().getContactID();

            appointment editedAppt = new appointment(apptID, title, desc, loc, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, custID, userID, contID);
            int Index = ApptDAO.getApptList().indexOf(editedAppt);
            if (DBQ.checkFields(editedAppt) == true) {
                if (ApptDAO.checkBizHours(editedAppt) == true) {
                    if (end.isAfter(start)) {
                        if (ApptDAO.apptOverlap(ApptDAO.getCustAppts(editedAppt.getCustID()), editedAppt) == true) {
                            System.out.println(ApptDAO.getCustAppts(editedAppt.getCustID()));
                            ApptDAO.editAppt(editedAppt, Index);

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
        catch (DateTimeParseException e){
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

    /**targetAppData Method. This method obtains all the object variables for the selected appointment for use in the initialize method above to set fields to the appropriate data.*/
    public void targetApptData(){
        apptIDField.setText(String.valueOf(targetAppt.getApptID()));
        titleField.setText(targetAppt.getTitle());
        descField.setText(targetAppt.getDesc());
        locField.setText(targetAppt.getLoc());
        typeField.setText(targetAppt.getType());
        startDate.setValue(targetAppt.getStart().toLocalDate());
        endDate.setValue(targetAppt.getEnd().toLocalDate());
        custCombo.setValue(DBQ.custSelect(targetAppt.getCustID()));
        userCombo.setValue(DBQ.userSelect(targetAppt.getUserID()));
        contactCombo.setValue(DBQ.contSelect(targetAppt.getConID()));
        if (targetAppt.getStart().getHour() < 10){startHour.setValue("0"+targetAppt.getStart().getHour());}
        else {startHour.setValue(String.valueOf(targetAppt.getStart().getHour()));}
        if (targetAppt.getStart().getMinute() < 15){startMinute.setValue("0"+targetAppt.getStart().getMinute());}
        else{startMinute.setValue(String.valueOf(targetAppt.getStart().getMinute()));}
        if (targetAppt.getEnd().getHour() < 10){endHour.setValue("0"+targetAppt.getEnd().getHour());}
        else{endHour.setValue(String.valueOf(targetAppt.getEnd().getHour()));}
        if (targetAppt.getEnd().getMinute() < 15){endMinute.setValue("0"+targetAppt.getEnd().getMinute());}
        else{endMinute.setValue(String.valueOf(targetAppt.getEnd().getMinute()));}
    }
}
