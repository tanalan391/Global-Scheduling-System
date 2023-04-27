package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/** Appointment class. Data for instances of this class is obtained from the database and stored during runtime.*/
public class appointment {
    private  int apptID;
    private String title;
    private String desc;
    private String loc;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private int custID;
    private int userID;
    private int conID;

    /**Appointment constructor method. Takes variables as values and creates an instance of appointment.*/
    public appointment(int apptID, String title, String desc, String loc, String type, LocalDateTime start, LocalDateTime end, Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int custID, int userID, int conID){
        this.apptID = apptID;
        this.title = title;
        this.desc = desc;
        this.loc = loc;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.custID = custID;
        this.userID = userID;
        this.conID = conID;
    }

    public int getApptID() {
        return apptID;
    }

    public void setApptID(int apptID) {
        this.apptID = apptID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getConID() {
        return conID;
    }

    public void setConID(int conID) {
        this.conID = conID;
    }

    public String getCreatedBy() {return createdBy;}

    public void setCreatedBy(String createdBy) {this.createdBy = createdBy;}

    public String getLastUpdatedBy() {return lastUpdatedBy;}

    public void setLastUpdatedBy(String lastUpdatedBy) {this.lastUpdatedBy = lastUpdatedBy;}

}
