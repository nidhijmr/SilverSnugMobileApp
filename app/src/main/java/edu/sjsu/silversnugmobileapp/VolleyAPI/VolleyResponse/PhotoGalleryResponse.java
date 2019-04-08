package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse;

import java.util.List;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.PhotoGallery;

public class PhotoGalleryResponse extends GenericResponse{

    private static final long serialVersionUID = 3543385351755692064L;
    private List<PhotoGallery> photos;


    public PhotoGalleryResponse()
    {

    }

    public PhotoGalleryResponse(List<PhotoGallery> photos)
    {
        super();
        this.photos = photos;
    }

    public List<PhotoGallery> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoGallery> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "PhotoGalleryResponse{" +
                "photos=" + photos  +
                '}';
    }
}

