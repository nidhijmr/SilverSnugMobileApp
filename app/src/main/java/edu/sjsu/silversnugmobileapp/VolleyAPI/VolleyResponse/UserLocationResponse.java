package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse;

import java.util.List;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.AddressBook;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.UserLocation;

public class UserLocationResponse extends GenericResponse {

    private static final long serialVersionUID = 3543385351755692064L;


    private List<UserLocation> userLocations;

    public UserLocationResponse(){}

    public UserLocationResponse(List<UserLocation> userLocations) {
        super();
        this.userLocations = userLocations;
    }


    public List<UserLocation> getUserLocations() {
        return userLocations;
    }

    public void setUserLocations(List<UserLocation> userLocations) {
        this.userLocations = userLocations;
    }


    @Override
    public String toString() {
        return "UserLocationResponse{" +
                "userLocations=" + userLocations +
                '}';
    }

}
