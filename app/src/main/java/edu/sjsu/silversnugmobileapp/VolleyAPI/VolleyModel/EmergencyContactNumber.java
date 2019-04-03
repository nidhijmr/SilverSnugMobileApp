package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel;

public class EmergencyContactNumber {

    private String userName;
    private String contactNumber;


    public EmergencyContactNumber()
    {

    }

    public EmergencyContactNumber(String userName, String contactNumber)
    {
        this.userName = userName;
        this.contactNumber = contactNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
