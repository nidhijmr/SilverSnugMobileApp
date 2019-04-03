package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest;

import java.io.Serializable;

public class PhotoGalleryRequest implements Serializable{

    private static final long serialVersionUID = 1L;
    private String photoId;
    private String userId;
    private String contactNumber;
    private String photoName;
    private String photo;

    public PhotoGalleryRequest()
    {

    }

    public PhotoGalleryRequest(String photoId, String userId, String contactNumber, String photoName, String photo)
    {
        this.photoId = photoId;
        this.userId = userId;
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

    @Override
    public String toString() {
        return "PhotoGalleryRequest{" +
                "photoId='" + photoId + '\'' +
                ", userId='" + userId + '\'' +
                ", photo='" + photo + '\'' +
                ", photoName='" + photoName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }

}
