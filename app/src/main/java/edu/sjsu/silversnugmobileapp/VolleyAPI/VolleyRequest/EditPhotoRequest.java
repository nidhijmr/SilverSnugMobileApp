package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest;

public class EditPhotoRequest {
    private static final long serialVersionUID = 1L;
    private String photoName;
    private String userId;
    private String contactNumber;

    public EditPhotoRequest()
    {

    }

    public EditPhotoRequest(String photoName, String userId, String contactNumber)
    {
        this.photoName = photoName;
        this.userId = userId;
        this.contactNumber = contactNumber;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
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

    @Override
    public String toString() {
        return "EditPhotoRequest{" +
                "photoName='" + photoName + '\'' +
                ", userId='" + userId + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}
