package model;

/** Contact class. Data for instances of this class is obtained from the database and stored during runtime.*/
public class contact {
    private int contactID;
    private String contactName;
    private String email;

    /**Contact constructor method. Takes variables as values and creates an instance of contact.*/
    public contact(int contactID, String contactName, String email){
        this.contactID = contactID;
        this.contactName = contactName;
        this.email = email;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString(){
        return (contactName);
    }
}
