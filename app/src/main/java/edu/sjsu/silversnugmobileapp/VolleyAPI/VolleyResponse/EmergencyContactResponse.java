package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse;

import java.util.List;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.EmergencyContactNumber;

public class EmergencyContactResponse {

    private static final long serialVersionUID = 3543385351755692064L;
    private EmergencyContactNumber emergencyContactNumber;

    public EmergencyContactResponse()
    {

    }

    public EmergencyContactResponse(EmergencyContactNumber emergencyContactNumber)
    {

        super();
        this.emergencyContactNumber = emergencyContactNumber;
    }

    public EmergencyContactNumber getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public void setEmergencyContactNumber(EmergencyContactNumber emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    @Override
    public String toString() {
        return "EmergencyContactResponse{" +
                "emergencyContactNumber=" + emergencyContactNumber +
                '}';
    }
}
