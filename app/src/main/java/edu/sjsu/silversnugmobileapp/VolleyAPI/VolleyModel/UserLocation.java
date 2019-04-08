package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel;

public class UserLocation {
    private String id;
    private String anomalyScore;
    private String dateTime;
    private String latitude;
    private String longitude;
    private String patientUsername;

    public UserLocation(){}

    public UserLocation(String id, String anomalyScore, String dateTime, String latitude, String longitude, String patientUsername) {
        this.id = id;
        this.anomalyScore = anomalyScore;
        this.dateTime = dateTime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.patientUsername = patientUsername;
    }

    public String getID(){
        return id;
    }

    public void setID(String id){
        this.id=id;
    }

    public String getAnomalyScore(){
        return anomalyScore;
    }

    public void setAnomalyScore(String anomalyScore){
        this.anomalyScore=anomalyScore;
    }

    public String getDateTime(){
        return dateTime;
    }

    public void setDateTime(String dateTime){
        this.dateTime=dateTime;
    }

    public String getLatitude(){
        return latitude;
    }

    public void setLatitude(String latitude){
        this.latitude=latitude;
    }

    public String getLongitude(){
        return longitude;
    }

    public void setLongitude(String longitude){
        this.longitude=longitude;
    }

    public String getPatientUsername(){
        return patientUsername;
    }

    public void setPatientUsername(String patientUsername){
        this.patientUsername=patientUsername;
    }

    @Override
    public String toString() {
        return "UserLocation{" +
                "id='" + id + '\'' +
                ", anomalyScore='" + anomalyScore + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", patientUsername='" + patientUsername + '\'' +
                '}';
    }
}
