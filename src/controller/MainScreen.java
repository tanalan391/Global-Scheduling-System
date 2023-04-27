package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.appointment;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import  DAO.*;

/**MainScreen Class. Controller class for the Main Screen of the application. All application functionality is accessible from this screen. Funtions are divided into three main tabs(Customers, Appointments, and Reports).*/
public class MainScreen implements Initializable {
    public TableColumn custIDCol;
    public TableColumn nameCol;
    public TableColumn addressCol;
    public TableColumn postalCodeCol;
    public TableColumn phoneCol;
    public TableColumn divIDCol;
    public TextField custSearch;
    public TableColumn apptIDCol;
    public TableColumn titleCol;
    public TableColumn descCol;
    public TableColumn locCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn apptCustIDCol;
    public TableColumn userIDCol;
    public TableColumn conIDCol;
    public TextField apptSearch;
    public TableView custTable;
    public TableView apptTable;
    public ComboBox<customer> reportCust;
    public ComboBox reportType;
    public ComboBox<String> reportMonth;
    public ComboBox<contact> reportContact;
    public ComboBox<String> reportTime;
    public TableView reportTable;
    public TableColumn rApptID;
    public TableColumn rTitle;
    public TableColumn rType;
    public TableColumn rDesc;
    public TableColumn rStart;
    public TableColumn rEnd;
    public TableColumn rCustID;
    public Label reportOutput;

    /**initialize Method. Calls methods to obtain lists of all customers and appointments, and displays that information in the correct tableview.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        custTable.setItems(CustDAO.getAllCustomers());
        custIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        divIDCol.setCellValueFactory(new PropertyValueFactory<>("divID"));
        apptTable.setItems(ApptDAO.getApptList());
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        locCol.setCellValueFactory(new PropertyValueFactory<>("loc"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        conIDCol.setCellValueFactory(new PropertyValueFactory<>("conID"));
        rApptID.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        rTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        rType.setCellValueFactory(new PropertyValueFactory<>("type"));
        rDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        rStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        rEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        rCustID.setCellValueFactory(new PropertyValueFactory<>("custID"));

        reportCust.setItems(CustDAO.getAllCustomers());
        reportContact.setItems(DBQ.getContactList());
        reportTime.setItems(DBQ.getTimesList());
        reportMonth.setItems(DBQ.getMonthsList());
        reportType.setItems(ApptDAO.getTypes());
    }

    /**newCust Method. Upon user click of the New Button in the Customers Tab, redirects the user to the appropriate interface.*/
    public void newCust(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddCustomer.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 650, 800);
        stage.setTitle("Global Scheduling System");
        stage.setScene(scene);
        stage.show();
    }

    /**editCust Method. Upon user click of the Edit Button in the Customers Tab, redirects the user to the appropriate interface.*/
    public void editCust(ActionEvent actionEvent) throws IOException {
        EditCustomer ec = new EditCustomer();
        ec.custToEdit((customer) custTable.getSelectionModel().getSelectedItem());
        try{
            if (custTable.getSelectionModel().getSelectedItem() != null){
                Parent root = FXMLLoader.load(getClass().getResource("/view/EditCustomer.fxml"));
                Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 650, 800);
                stage.setTitle("Global Scheduling System");
                stage.setScene(scene);
                stage.show();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Customer to edit.");
                alert.showAndWait();
            }

        }catch (NullPointerException | IOException e){e.printStackTrace();}
    }

    /**delCust Method. Upon user click of the Delete Button in the Customers Tab, produces a warning message, and if confirmed will proceed to call methods to delete the customer and all associated appointments.*/
    public void delCust(ActionEvent actionEvent) throws SQLException {
        customer c = (customer) custTable.getSelectionModel().getSelectedItem();
        if (c != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All appointments associated with " + c.getName() + " will be deleted.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                CustDAO.deleteCust(c);
                custTable.setItems(CustDAO.getAllCustomers());
                apptTable.setItems(ApptDAO.getApptList());
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Select a Customer to delete.");
            alert.showAndWait();
        }
    }

    /**onCustSearch Method. Upon user click of the Search Button, obtains the input from the search field and checks against the existing list of customers using either Customer name or ID.*/
    public void onCustSearch(ActionEvent actionEvent) {
        String q = custSearch.getText();
        if (q == "" || q == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a Customer name or ID.");
            alert.showAndWait();
            custTable.setItems(CustDAO.getAllCustomers());
        }
        else{
            if (CustDAO.searchCustomers(q).size() != 0) {
                custTable.setItems(CustDAO.searchCustomers(q));
                custSearch.setText("");
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No Customers were found!");
                alert.showAndWait();
                custTable.setItems(CustDAO.getAllCustomers());
                custSearch.setText("");
            }
        }
    }

    /**newAppt Method. Upon user click of the New Button in the Appointments Tab, redirects the user to the appropriate interface.*/
    public void newAppt(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddAppointment.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 650, 800);
        stage.setTitle("Global Scheduling System");
        stage.setScene(scene);
        stage.show();
    }

    /**editAppt Method. Upon user click of the Edit Button in the Appointments Tab, redirects the user to the appropriate interface.*/
    public void editAppt(ActionEvent actionEvent) {
    EditAppointment.editAppt((appointment) apptTable.getSelectionModel().getSelectedItem());
        try{
            if (apptTable.getSelectionModel().getSelectedItem() != null){
                Parent root = FXMLLoader.load(getClass().getResource("/view/EditAppointment.fxml"));
                Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 700, 800);
                stage.setTitle("Global Scheduling System");
                stage.setScene(scene);
                stage.show();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an Appointment to edit.");
                alert.showAndWait();
            }

        }catch (NullPointerException | IOException e){e.printStackTrace();}
    }

    /**delAppt Method. Upon user click of the Delete Button in the Appointments Tab, produces a warning message, and if confirmed will proceed to call methods to delete the selected appointment and display a message with the appointment ID.*/
    public void delAppt(ActionEvent actionEvent) throws SQLException {
        appointment a = (appointment) apptTable.getSelectionModel().getSelectedItem();
        if (a != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment ID: " + a.getApptID() + "\nType: " + a.getType() + "\nThis appointment will be permanently deleted. \nClick OK to proceed.");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                ApptDAO.deleteAppt(a.getApptID());
            }
            alert = new Alert(Alert.AlertType.INFORMATION, "Appointment " + a.getApptID() + " has been deleted.");
            alert.showAndWait();
            apptTable.setItems(ApptDAO.getApptList());
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Select an Appointment to delete.");
            alert.showAndWait();
        }
    }

    /**onApptSearch Method. Upon user click of the Search Button, obtains the input from the search field and checks against the existing list of appointments using either Appointment title or ID.*/
    public void onApptSearch(ActionEvent actionEvent) {
        String q = apptSearch.getText();
        if (q == "" || q == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a Appointment Title or ID.");
            alert.showAndWait();
            apptTable.setItems(ApptDAO.getApptList());
        }
        else{
            if (ApptDAO.searchAppts(q).size() != 0) {
                apptTable.setItems(ApptDAO.searchAppts(q));
                apptSearch.setText("");
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "No Appointments were found!");
                alert.showAndWait();
                apptTable.setItems(ApptDAO.getApptList());
                apptSearch.setText("");
            }
        }
    }

    /**allFilter Method. Sets the Appointments tableview to display all appointments*/
    public void allFilter(ActionEvent actionEvent) {
        apptTable.setItems(ApptDAO.getApptList());
    }

    /**monthFilter Method. Sets the Appointments tableview to display appointments that occur within the current month*/
    public void monthFilter(ActionEvent actionEvent) {
        apptTable.setItems(ApptDAO.apptsByMonth());
    }

    /**weekFilter Method. Sets the Appointments tableview to display appointments that occur within the current week*/
    public void weekFilter(ActionEvent actionEvent) {
        apptTable.setItems(ApptDAO.apptsByWeek());
    }

    /**runCustAppts Method. Runs the Customer Appointments report. Collects the user selections for the customer, Type, and Month drop downs. Using this info, it then obtains an Observable List to display in the tableview. This list is obtained using a Lambda expression. The lambda expression was used in this case due to it's simplicity and minimizing bukly coding of nested if statements. With the built in interface for filters, obtaining the correct list is much more straight-forward with a lambda in this scenario.*/
    public void runCustAppts(ActionEvent actionEvent) throws NullPointerException {
        try {
            int cID = reportCust.getSelectionModel().getSelectedItem().getID();
            String cName = reportCust.getSelectionModel().getSelectedItem().getName();
            String type = reportType.getSelectionModel().getSelectedItem().toString();
            String month = reportMonth.getSelectionModel().getSelectedItem().toLowerCase();
            String bMonth = reportMonth.getSelectionModel().getSelectedItem();

            ObservableList<appointment> custAppts = ApptDAO.getApptList().stream().filter(x -> cID == x.getCustID() && type.equals(x.getType()) && month.equals(x.getStart().getMonth().toString().toLowerCase())).collect(Collectors.toCollection(FXCollections::observableArrayList));

            reportTable.setItems(custAppts);

            int numAppts = ApptDAO.runCustReport(cID,type,month).size();
            reportOutput.setText(cName+" has "+numAppts+" appointments of Type: "+type+" in "+bMonth+"." );


        }
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Make a Customer selection.");
            alert.showAndWait();
        }
    }

    /**runContactSched Method. Runs the Contact Schedule report. Collects the user selections for the contact drop down. Using this info, it then obtains an Observable List to display in the tableview. This list is obtained using a Lambda expression. The lambda expression was used in this case due to it's simplicity and minimizing bukly coding of nested if statements. With the built in interface for filters, obtaining the correct list is much more straight-forward with a lambda in this scenario.*/
    public void runContactSched(ActionEvent actionEvent) throws NullPointerException {
        try {
            contact selCont = reportContact.getSelectionModel().getSelectedItem();
            int contID = selCont.getContactID();
            String contName = selCont.getContactName();
            //Use of Lambda Expression
            ObservableList<appointment> contAppts = ApptDAO.getApptList().stream().filter(x -> contID == x.getConID()).collect(Collectors.toCollection(FXCollections::observableArrayList));
            reportTable.setItems(contAppts);
            reportOutput.setText(contName+"'s Schedule");
        }
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Make a Contact selection.");
            alert.showAndWait();
        }
    }

    /**runTime Method. Runs the Run Time Report. The user selects an appointment duration from the dropdown menu and clicks Run. The method then searches all appointments based on the selected duration and returns an Observable list for display in the reports tableview.*/
    public void runTime(ActionEvent actionEvent) {
        try {
            String dur = reportTime.getSelectionModel().getSelectedItem();
            int minutes;
            if (dur == "15 Minutes"){minutes = 15;}
            else if (dur == "30 Minutes"){minutes = 30;}
            else if (dur == "1 Hour"){minutes = 60;}
            else if(dur == "> 1 Hour"){minutes = 61;}
            else{minutes = 0;}

        reportTable.setItems(ApptDAO.getApptDur(minutes));
        int num = ApptDAO.getApptDur(minutes).size();
        reportOutput.setText("There are "+num+" appointments that are "+dur+" long.");

        }
        catch (NullPointerException e){e.printStackTrace();}
    }
}
