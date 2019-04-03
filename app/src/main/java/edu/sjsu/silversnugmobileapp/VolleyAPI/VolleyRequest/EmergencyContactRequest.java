package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest;

public class EmergencyContactRequest {


    private String userId;

    public EmergencyContactRequest()
    {

    }

    public EmergencyContactRequest(String userId)
    {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userName) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "EmergencyContactRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }
}


