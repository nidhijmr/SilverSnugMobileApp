package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel;

public class EmergencyContactNumber {

    private String userId;
    private String contactNumber;


    public EmergencyContactNumber()
    {

    }

    public EmergencyContactNumber(String userId, String contactNumber)
    {
        this.userId = userId;
        this.contactNumber = contactNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
