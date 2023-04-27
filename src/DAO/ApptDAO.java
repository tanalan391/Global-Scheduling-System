package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**Appointment Data Access Object Class. This class contains the methods that are used to access information from the Database and the Observable lists where information is temporarily stored for interaction during runtime.*/
public class ApptDAO {
    private static ObservableList<appointment> apptList = FXCollections.observableArrayList();
    private static ObservableList<appointment> apptSearch = FXCollections.observableArrayList();
    private static ObservableList<appointment> userAppts = FXCollections.observableArrayList();
    private static ObservableList<appointment> custAppts = FXCollections.observableArrayList();
    private static ObservableList<appointment> monthSched = FXCollections.observableArrayList();
    private static ObservableList<appointment> weekSched = FXCollections.observableArrayList();
    private static int bizStart = 8;
    private static int bizEnd = 22;
    private static ObservableList<String> types = FXCollections.observableArrayList();
    private static ObservableList<appointment> contAppts = FXCollections.observableArrayList();
    private static ObservableList<appointment> apptDur = FXCollections.observableArrayList();

    /**getApptList Method. Queries the Database for all appointments scheduled and stores them in an observable list for display and interaction by the user.
     @return apptList Returns an Observable list of all appointments.
     */
    public static ObservableList<appointment> getApptList(){
         ObservableList<appointment> apptList = FXCollections.observableArrayList();

        try {
            apptList.clear();
            String sql = "SELECT * FROM client_schedule.appointments";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String loc = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = LocalDateTime.from(rs.getTimestamp("Start").toLocalDateTime().atZone(ZoneId.systemDefault() ));
                LocalDateTime end = LocalDateTime.from(rs.getTimestamp("End").toLocalDateTime().atZone(ZoneId.systemDefault()));
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int custID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int conID = rs.getInt("Contact_ID");

                appointment a = new appointment(apptID, title, desc, loc, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, custID, userID, conID);
                apptList.add(a);
            }

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return apptList;
    }

    /**getUserAppts Method. This method is used to obtain the list of appointments for a specific user.
     @param uID The User ID for the selected user, used to filter appointments by user
     @return userAppts Returns an Observable list storing all appointments for the selected user as instances of the appointment class.
     */
    public static ObservableList<appointment> getUserAppts(int uID){
        try {
            userAppts.clear();
            String sql = "SELECT * FROM client_schedule.appointments WHERE User_ID = ?";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ps.setInt(1,uID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String loc = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = LocalDateTime.from(rs.getTimestamp("Start").toLocalDateTime().atZone(ZoneId.systemDefault() ));
                LocalDateTime end = LocalDateTime.from(rs.getTimestamp("End").toLocalDateTime().atZone(ZoneId.systemDefault()));
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int custID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int conID = rs.getInt("Contact_ID");

                appointment a = new appointment(apptID, title, desc, loc, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, custID, userID, conID);
                userAppts.add(a);
            }
    }catch (SQLException e){e.printStackTrace();}
        return userAppts;
    }

    /**apptsByMonth Method. Used to filter appointments from the database by the current month.
     @return monthSched Returns an Observable list of appointments for the current month, and displays the results in the interface.
     */
    public static ObservableList<appointment> apptsByMonth(){
        try {
            monthSched.clear();
            String sql = "SELECT * FROM appointments WHERE MONTH(Start) = MONTH(now()) AND YEAR(Start) = YEAR(now())";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String loc = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = LocalDateTime.from(rs.getTimestamp("Start").toLocalDateTime().atZone(ZoneId.systemDefault() ));
                LocalDateTime end = LocalDateTime.from(rs.getTimestamp("End").toLocalDateTime().atZone(ZoneId.systemDefault()));
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int custID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int conID = rs.getInt("Contact_ID");

                appointment a = new appointment(apptID, title, desc, loc, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, custID, userID, conID);
                monthSched.add(a);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return monthSched;
    }

    /**apptsByWeek Method. Used to filter appointments from the database by the current week.
     @return weekSched Returns an Observable list of appointments for the current week, and displays the results in the interface.
     */
    public static ObservableList<appointment> apptsByWeek(){
        try {
            weekSched.clear();
            String sql = "SELECT * FROM appointments WHERE WEEK(Start) = WEEK(now()) AND YEAR(Start) = YEAR(now())";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String loc = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = LocalDateTime.from(rs.getTimestamp("Start").toLocalDateTime().atZone(ZoneId.systemDefault() ));
                LocalDateTime end = LocalDateTime.from(rs.getTimestamp("End").toLocalDateTime().atZone(ZoneId.systemDefault()));
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int custID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int conID = rs.getInt("Contact_ID");

                appointment a = new appointment(apptID, title, desc, loc, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, custID, userID, conID);
                weekSched.add(a);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return weekSched;
    }

    /**getCustAppts Method. This method is used to obtain the list of appointments for a specific customer.
     @param cID The Customer ID for the selected customer, used to filter appointments by customer
     @return custAppts Returns an Observable list storing all appointments for the selected customer as instances of the appointment class.
     */
    public static ObservableList<appointment> getCustAppts(int cID){
        custAppts.clear();
        try {
            String sql = "SELECT * FROM client_schedule.appointments WHERE Customer_ID = ?";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ps.setInt(1,cID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String loc = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = LocalDateTime.from(rs.getTimestamp("Start").toLocalDateTime().atZone(ZoneId.systemDefault() ));
                LocalDateTime end = LocalDateTime.from(rs.getTimestamp("End").toLocalDateTime().atZone(ZoneId.systemDefault()));
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int custID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int conID = rs.getInt("Contact_ID");

                appointment a = new appointment(apptID, title, desc, loc, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, custID, userID, conID);
                custAppts.add(a);
            }
        }catch (SQLException e){e.printStackTrace();}
        return custAppts;
    }

    /**runCustReport Method. This method is used to obtain the list of appointments for a specific customer, with a specifc Appointment type and in a specific month.
     @param cID The Customer ID for the selected customer, used to filter appointments by customer
     @param reportType Obtained from a combobox menu in the Report interface, the user selects the type of report they wish to filter customer appointments by.
     @param month Obtained from a combobox menu in the Report interface, the user selects which month they wish to filter customer appointments by.
     @return custAppts Returns an Observable list storing all appointments for the selected user, filtered by type and month, as instances of the appointment class.
     */
    public static ObservableList<appointment> runCustReport (int cID, String reportType, String month) throws NullPointerException{
        custAppts.clear();
        try {
            String sql = "SELECT * FROM client_schedule.appointments WHERE Customer_ID = ?";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ps.setInt(1,cID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String loc = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = LocalDateTime.from(rs.getTimestamp("Start").toLocalDateTime().atZone(ZoneId.systemDefault() ));
                LocalDateTime end = LocalDateTime.from(rs.getTimestamp("End").toLocalDateTime().atZone(ZoneId.systemDefault()));
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int custID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int conID = rs.getInt("Contact_ID");

                appointment a = new appointment(apptID, title, desc, loc, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, custID, userID, conID);

                if (reportType == "Any Type" || month == "All Month"){
                    custAppts.add(a);
                    continue;
                }
                else {
                        if (a.getType().equals(reportType) == true) {
                            String dbMonth = a.getStart().getMonth().toString().toLowerCase();
                            if (month.equals(dbMonth) == true) {
                                custAppts.add(a);
                            }
                        }
                    }
            }
        }catch (SQLException | NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Make a Customer selection.");
            alert.showAndWait();
        }
        return custAppts;
    }

    /**getApptDur Method. This method is used to obtain a list of appointments based on the time duration of the appointment.
     @param apptLen The user selects the duration of appointment they wish to filter appointments by, and this selection is passed into the method.
     @return apptDur Returns an Observable list storing all appointments filtered by duration of appointment as instances of the appointment class.
     */
    public static ObservableList<appointment> getApptDur(int apptLen){
        try {
            apptDur.clear();
            String sql = "SELECT * FROM client_schedule.appointments";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int apptID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String desc = rs.getString("Description");
                String loc = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = LocalDateTime.from(rs.getTimestamp("Start").toLocalDateTime().atZone(ZoneId.systemDefault() ));
                LocalDateTime end = LocalDateTime.from(rs.getTimestamp("End").toLocalDateTime().atZone(ZoneId.systemDefault()));
                Timestamp createDate = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Timestamp lastUpdate = rs.getTimestamp("Last_Update");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int custID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int conID = rs.getInt("Contact_ID");

                appointment a = new appointment(apptID, title, desc, loc, type, start, end, createDate, createdBy, lastUpdate, lastUpdatedBy, custID, userID, conID);

                if (apptLen >= 15 && apptLen <= 60 ) {
                    if ((ChronoUnit.MINUTES.between(a.getStart(), a.getEnd())) == apptLen) {
                        apptDur.add(a);
                    }
                }
                else if (apptLen > 60){
                    if ((ChronoUnit.MINUTES.between(a.getStart(), a.getEnd())) >= apptLen) {
                        apptDur.add(a);
                    }
                }
                else {
                    if ((ChronoUnit.MINUTES.between(a.getStart(), a.getEnd())) < 15) {
                        apptDur.add(a);
                    }
                }

            }

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return apptDur;
    }

    /**apptOverlap Method. Used to determine if a new or edited appointment will overlap an existing appointment for the selected customer.
     @param list Passes in a list of all appointments for the selected customer. Obtained using the getCustAppts method.
     @param appt The new or edited appointment to compare to existing customer appointments.
     @return boolean Returns a boolean value. True if there is no conflict between the existing customer appointments and the new or edited appointment, False if there is a conflict.
     */
    public static boolean apptOverlap(ObservableList<appointment> list, appointment appt){
        int i = 0;
        while(i < list.size()){
           appointment oldAppt = list.get(i);
            if (oldAppt.getApptID() != appt.getApptID()){
                if (oldAppt.getStart().isBefore(appt.getEnd()) && oldAppt.getEnd().isAfter(appt.getStart())){
                    return false;
                }
                else if(appt.getStart().isBefore(oldAppt.getEnd()) && appt.getEnd().isAfter(oldAppt.getStart())){
                    return false;
                }
                else if (oldAppt.getStart().isEqual(appt.getStart()) && oldAppt.getEnd().isEqual(appt.getEnd())){
                    return false;
                }
                else {i++; }
            }else {i++;}
        }
        return true;
    }

    /**checkBizHours Method. This method checks if a new or edited appointment falls within the set hours of operation of the business.
     @param appt The new or edited appointment is passed in for verification.
     @return boolean Returns a boolean value. True if the new or edited appointment time falls within business hours, false if it is outside of business hours.
     */
    public static boolean checkBizHours(appointment appt){
        ZonedDateTime sTime = appt.getStart().atZone(ZoneId.of("America/New_York"));
        ZonedDateTime eTime = appt.getEnd().atZone(ZoneId.of("America/New_York"));
        if (sTime.getHour() < bizStart || sTime.getHour() > bizEnd || eTime.getHour() < bizStart || eTime.getHour() > bizEnd){
            return false;
        }
        return true;
    }

    /**searchAppts Method. This method allows the user to search for a specific appointment by appointment title.
     @param partialTitle Obtained from the search textfield in the Schedule tab of the interface, passes in the string of user input.
     @return apptSearch Returns an observable list of all appointments with Titles that contain part or all of the users input.
     */
    public static ObservableList<appointment> searchAppts(String partialTitle){
        apptSearch.clear();
        if (DBQ.isNum(partialTitle) == true){
            for (appointment a : getApptList()){
                if (a.getApptID() == Integer.valueOf(partialTitle)){
                    apptSearch.add(a);
                }
            }
        }
        else {
            for (appointment a : getApptList()){
                if (a.getTitle().toLowerCase().contains(partialTitle.toLowerCase())){
                apptSearch.add(a);
                }
            }
        }
        return apptSearch;
    }

    /**newAppt Method. This method is called when a new appointment instance is saved and added to the Database.
     @param appointment Instance of appointment created on the AddAppointment interface. Passes in the object which allows access to it's variables for addition to the database.
     */
    public static void newAppt(appointment appointment) throws SQLException{

        String title = appointment.getTitle();
        String desc = appointment.getDesc();
        String loc = appointment.getLoc();
        String type = appointment.getType();
        Timestamp start = Timestamp.valueOf(appointment.getStart());
        Timestamp end = Timestamp.valueOf(appointment.getEnd());
        Timestamp createDate = appointment.getCreateDate();
        String createdBy = UserDAO.getCurrentUser().getUserName();
        int custID = appointment.getCustID();
        int userID = appointment.getUserID();
        int conID = appointment.getConID();

        String sql = "INSERT INTO client_schedule.appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Customer_ID, User_ID, Contact_ID) VALUES(?,?,?,?,?,?,?,?,?,?,? )";
        PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, desc);
        ps.setString(3, loc );
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setTimestamp(7, createDate);
        ps.setString(8,createdBy);
        ps.setInt(9,custID);
        ps.setInt(10,userID);
        ps.setInt(11,conID);
        ps.executeUpdate();

    }

    /**editAppt Method. This method is called when an appointment instance is edited and saved, to be added to the Database. The method also updates the info for the object in the runtime data.
     @param appointment Instance of appointment created on the EditAppointment interface. Passes in the object which allows access to it's variables for update in the database.
     @param Index Passes in the array index of the selected appointment instance.
     */
    public static void editAppt(appointment appointment, int Index) throws SQLException{
        int apptID = appointment.getApptID();
        String title = appointment.getTitle();
        String desc = appointment.getDesc();
        String loc = appointment.getLoc();
        String type = appointment.getType();
        Timestamp start = Timestamp.valueOf(appointment.getStart());
        Timestamp end = Timestamp.valueOf(appointment.getEnd());
        Timestamp lastUpdate = appointment.getLastUpdate();
        String lastUpdatedBy = UserDAO.getCurrentUser().getUserName();
        int custID = appointment.getCustID();
        int userID = appointment.getUserID();
        int conID = appointment.getConID();

        String sql = "UPDATE client_schedule.appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, desc);
        ps.setString(3, loc );
        ps.setString(4, type);
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setTimestamp(7, lastUpdate);
        ps.setString(8,lastUpdatedBy);
        ps.setInt(9,custID);
        ps.setInt(10,userID);
        ps.setInt(11,conID);
        ps.setInt(12,apptID);
        ps.executeUpdate();
    }

    /**getTypes Method. This method is used to create the options for the combobox menu in the Reports interface.
     @return types Returns an Observable array list storing the type value of all appointments in the database.
     */
    public static ObservableList<String> getTypes(){
        types.clear();

        try {
            String sql = "SELECT * FROM client_schedule.appointments";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                String type = rs.getString("Type");
                types.add(type);
            }

        }catch (SQLException throwables){
            throwables.printStackTrace();
        }
        return types;
    }

    /**deleteAppt Method. This method is called when a user selects the option to delete an appointment, or when a customer is deleted, requiring their associated appointments to also be deleted. The appointment specified is deleted from the database.
     @param apptID Passes in the appointment ID of the appointment to be deleted.
     */
    public static void deleteAppt(int apptID) throws SQLException {
        try {
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement ps = DBC.getConnection().prepareStatement(sql);
            ps.setInt(1, apptID);
            ps.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

}
