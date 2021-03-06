package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest;

import java.io.Serializable;

public class AddressBookRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String addressId;
    private String addressName;
    private double latitude;
    private double longitude;
    private String address;

    public AddressBookRequest()
    {

    }

    public AddressBookRequest(String userId, String addressId, String addressName, double latitude, double longitude, String address) {
        this.userId = userId;
        this.addressId = addressId;
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "AddressBookRequest{" +
                "userId='" + userId + '\'' +
                ", addressId='" + addressId + '\'' +
                ", addressName='" + addressName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                '}';
    }
}


