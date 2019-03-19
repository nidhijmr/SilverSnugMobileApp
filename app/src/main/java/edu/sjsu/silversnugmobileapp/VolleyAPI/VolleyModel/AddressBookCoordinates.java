package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel;

public class AddressBookCoordinates {
    private String addressName;
    private double latitude;
    private double longitude;

    public AddressBookCoordinates(){

    }

    public AddressBookCoordinates(String addressName, double latitude, double longitude) {
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
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

    @Override
    public String toString() {
        return "AddressBookCoordinates{" +
                "addressName='" + addressName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
