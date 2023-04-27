package controller;

import DAO.CustDAO;
import DAO.DBQ;
import javafx.collections.ObservableList;
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

import DAO.*;

/**EditCustomer Class. Controller class for the Edit Customer user interface.*/
public class EditCustomer implements Initializable {
    public Label firstLevelLabel;
    public Label countryLabel;
    public TextField nameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneField;
    public ComboBox<firstLevel> flCombo;
    public ComboBox<country> countryCombo;
    public TextField custIDField;
    private static customer targetCustomer = null;
    public void custToEdit(customer customer){targetCustomer = customer;}

    /**initialize Method. Sets values for the fields and combo boxes based on the customer selected.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBQ.getFirstLevelList();
        countryCombo.setItems(DBQ.getCountryList());
        flCombo.setItems(DBQ.getFLList(targetCustomer.getDivID()));
        targetCustomerData();
    }

    /**onSave Method. Upon user clicking the Save Button, this method obtains input from the fields and combo boxes and calls methods to check that all fields are filled out, and then calls methods to save the newly edited customer info to the Database.*/
    public void onSave(ActionEvent actionEvent) throws SQLException, IOException {
        int id = targetCustomer.getID();
        String name = nameField.getText();
        String address = addressField.getText();
        String postalCode = postalCodeField.getText();
        String phone = phoneField.getText();
        Timestamp createDate = targetCustomer.getCreateDate();
        String createdBy = targetCustomer.getCreatedBy();
        Timestamp lastUpdate = Timestamp.from(Instant.now());
        String lastUpdatedBy = UserDAO.getCurrentUser().getUserName();
        int divID = flCombo.getSelectionModel().getSelectedItem().getDivID();

        customer editCust = new customer(id, name, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divID);
        int Index = CustDAO.getAllCustomers().indexOf(targetCustomer);
        if (DBQ.checkFields(editCust) == true) {
            CustDAO.editCust(editCust, Index);

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

    /**targetCustomerData Method. This method obtains all the object variables for the selected customer for use in the initialize method above to set fields to the appropriate data.*/
    public void targetCustomerData() {
        custIDField.setText(String.valueOf(targetCustomer.getID()));
        nameField.setText(targetCustomer.getName());
        addressField.setText(targetCustomer.getAddress());
        postalCodeField.setText(targetCustomer.getPostalCode());
        phoneField.setText(targetCustomer.getPhoneNumber());
        flCombo.setValue(DBQ.fLSelect(targetCustomer.getDivID()));
        firstLevelLabel.setText(DBQ.fltype(targetCustomer.getDivID()));
        countryCombo.setValue(DBQ.cSelect(targetCustomer.getDivID()));
    }

    /**onCSelect Method. When the user makes a new country selection in the related combobox menu, this method changes the label and available combo box menu for the First Level selection appropriately.*/
    public void onCSelect(ActionEvent actionEvent) {
        if (countryCombo.getSelectionModel().getSelectedItem().getCountryID() == 1){
            firstLevelLabel.setText("State");
            flCombo.valueProperty().set(null);
            flCombo.setItems(DBQ.getFLList1());
        }
        if (countryCombo.getSelectionModel().getSelectedItem().getCountryID() == 2){
            firstLevelLabel.setText("Constituent Country");
            flCombo.valueProperty().set(null);
            flCombo.setItems(DBQ.getFlList2());
        }
        if (countryCombo.getSelectionModel().getSelectedItem().getCountryID() == 3){
            firstLevelLabel.setText("Province");
            flCombo.valueProperty().set(null);
            flCombo.setItems(DBQ.getFlList3());
        }
    }

}
