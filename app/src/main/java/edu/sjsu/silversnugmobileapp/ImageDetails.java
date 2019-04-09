package edu.sjsu.silversnugmobileapp;

public class ImageDetails {

    private int id;
    private String name;
    private String relationship;
    private String contactNumber;
    private String imagePath;

    public ImageDetails(String name, String contactNumber, String relationship, String imagePath)
    {
        this.name= name;
        this.contactNumber = contactNumber;
        this.relationship = relationship;
        this.imagePath = imagePath;

    }

    public String getName()
    {
        return name;

    }

    public void setName()
    {
        this.name=name;

    }

    public String getContactNumber()
    {
        return contactNumber;

    }

    public void setContactNumber()
    {
        this.contactNumber=contactNumber;

    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
