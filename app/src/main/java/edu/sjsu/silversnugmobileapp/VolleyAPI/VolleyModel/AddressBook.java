package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel;

public class AddressBook {
    private String userId;
    private String addressId;
    private String addressName;
    private String latitude;
    private String longitude;

    public AddressBook(){

    }

    public AddressBook(String userId, String addressId, String addressName, String latitude, String longitude) {
        this.userId = userId;
        this.addressId = addressId;
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

