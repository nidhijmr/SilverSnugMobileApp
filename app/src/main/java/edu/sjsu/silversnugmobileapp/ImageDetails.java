package edu.sjsu.silversnugmobileapp;

public class ImageDetails {

    private int id;
    private String name;
    //private String relationship;
    private String contactNumber;
    private byte[] image;

    public ImageDetails(String name, String contactNumber ) //, byte[] image)
    {
        this.name= name;
        this.contactNumber = contactNumber;
       // this.image = image;
        // this.id=id;
    }

    public int getId()
    {
        return id;

    }

    public void setId()
    {
        this.id=id;

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

   /* public byte[] getImage()
    {
        return image;

    }

    public void setImage()
    {
        this.image=image;

    }*/

}
