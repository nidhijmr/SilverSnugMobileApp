package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse;

import java.util.List;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.EmergencyContactNumber;

public class EmergencyContactResponse extends  GenericResponse{

    private static final long serialVersionUID = 3543385351755692064L;
    private String userId;
    private String emergencyContactNumber;





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

    public EmergencyContactResponse()
    {

    }

    public EmergencyContactResponse(String status, String message, String userId, String emergencyContactNumber)
    {

        super(status,message);
        this.userId = userId;
        this.emergencyContactNumber = emergencyContactNumber;

    }




    @Override
    public String toString() {
        return "EmergencyContactResponse{" +
                "userId='" + userId + '\'' +
                ", emergencyContactNumber='" + emergencyContactNumber + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }


}
