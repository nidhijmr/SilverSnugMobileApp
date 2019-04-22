package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest;

public class DeletePhotoRequest {
    private static final long serialVersionUID = 1L;
    private String photoName;
    private String userId;

    public DeletePhotoRequest()
    {

    }

    public DeletePhotoRequest(String photoName, String userId)
    {
        this.photoName = photoName;
        this.userId = userId;
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

    @Override
    public String toString() {
        return "DeletePhotoRequest{" +
                "photoName='" + photoName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
