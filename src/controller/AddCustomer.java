package controller;

import DAO.CustDAO;
import DAO.DBQ;
import DAO.UserDAO;
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
import java.util.ResourceBundle;

/**AddCustomer Class. Controller class for the Add Customer user interface.*/
public class AddCustomer implements Initializable {
    public Label firstLevelLabel;
    public Label countryLabel;
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneField;
    public ComboBox<firstLevel> flCombo;
    public ComboBox<country> countryCombo;

    /**initialize Method. Sets values for the combo box drop down menus.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBQ.getFirstLevelList();
        flCombo.setItems(DBQ.getFLList1());
        firstLevelLabel.setText("State");
        flCombo.getSelectionModel().selectFirst();
        countryCombo.setItems(DBQ.getCountryList());
        countryCombo.getSelectionModel().selectFirst();
    }

    /**onSave Method. Upon user clicking the Save Button, this method obtains input from the fields and combo boxes and calls methods to check that all fields are filled out, and then calls methods to save the newly created customer to the Database.*/
    public void onSave(ActionEvent actionEvent) throws SQLException, IOException {
        int id = 1;
        String name = nameField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String phone = phoneField.getText();
        Timestamp createDate = Timestamp.from(Instant.now());
        String createdBy = UserDAO.getCurrentUser().getUserName();
        Timestamp lastUpdate = null;
        String lastUpdatedBy = null;
        int divID = flCombo.getSelectionModel().getSelectedItem().getDivID();

        customer newCust = new customer(id, name, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divID);
        if (DBQ.checkFields(newCust) == true) {
            CustDAO.newCust(newCust);

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 500);
            stage.setTitle("Global Scheduling System");
            stage.setScene(scene);
            stage.show();
        }
        else {
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

    /**onCSelect Method. When the user makes a new country selection in the related combobox menu, this method changes the label and available combo box menu for the First Level selection appropriately.*/
    public void onCSelect(ActionEvent actionEvent) {
        if (countryCombo.getSelectionModel().getSelectedItem().getCountryID() == 1){
            firstLevelLabel.setText("State");
            flCombo.setItems(DBQ.getFLList1());
        }
        if (countryCombo.getSelectionModel().getSelectedItem().getCountryID() == 2){
            firstLevelLabel.setText("Constituent Country");
            flCombo.setItems(DBQ.getFlList2());
        }
        if (countryCombo.getSelectionModel().getSelectedItem().getCountryID() == 3){
            firstLevelLabel.setText("Province");
            flCombo.setItems(DBQ.getFlList3());
        }
    }
}
