package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel;

public class PhotoGallery {

    private String photoId;
    private String userName;
    private String contactNumber;
    private String photoName;
    private String photo;

    public PhotoGallery()
    {

    }

    public PhotoGallery(String photoId, String userName, String contactNumber, String photoName, String photo)
    {
        this.photoId = photoId;
        this.userName = userName;
        this.contactNumber = contactNumber;
        this.photoName = photoName;
        this.photo = photo;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
