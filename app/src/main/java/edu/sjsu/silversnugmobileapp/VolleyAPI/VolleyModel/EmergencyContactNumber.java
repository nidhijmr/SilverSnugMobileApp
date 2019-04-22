package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel;

public class EmergencyContactNumber {

    private String userId;
    private String emergencyContactNumber;


    public EmergencyContactNumber()
    {

    }

    public EmergencyContactNumber(String userId, String emergencyContactNumber)
    {
        this.userId = userId;
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }
}
