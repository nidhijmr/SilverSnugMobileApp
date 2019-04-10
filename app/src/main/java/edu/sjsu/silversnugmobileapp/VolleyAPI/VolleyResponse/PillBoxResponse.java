package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse;

import java.util.List;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.PillBox;

public class PillBoxResponse extends GenericResponse {

    private static final long serialVersionUID = 1L;

    private List<PillBox> pillBoxes;

    public PillBoxResponse(){

    }

    public PillBoxResponse(List<PillBox> pillBoxes) {
        super();
        this.pillBoxes = pillBoxes;
    }

    public List<PillBox> getPillBoxes() {
        return pillBoxes;
    }

    public void setPillBoxes(List<PillBox> pillBoxes) {
        this.pillBoxes = pillBoxes;
    }

    @Override
    public String toString() {
        return "PillBoxResponse{" +
                "pillBoxes=" + pillBoxes +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}