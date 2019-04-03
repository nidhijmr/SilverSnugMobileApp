package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse;

import java.util.List;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.PhotoGallery;

public class PhotoGalleryResponse extends GenericResponse{

    private static final long serialVersionUID = 3543385351755692064L;
    private List<PhotoGallery> photogallery;


    public PhotoGalleryResponse()
    {

    }

    public PhotoGalleryResponse(List<PhotoGallery> photogallery)
    {
        super();
        this.photogallery = photogallery;
    }

    public List<PhotoGallery> getPhotogallery() {
        return photogallery;
    }

    public void setPhotogallery(List<PhotoGallery> photogallery) {
        this.photogallery = photogallery;
    }

    @Override
    public String toString() {
        return "PhotoGalleryResponse{" +
                "photogallery=" + photogallery +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

