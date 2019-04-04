package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient;

import org.json.JSONException;
import org.json.JSONObject;

public interface APICallback {
    void onSuccess(JSONObject jsonResponse) throws JSONException;
    void onError(String message);
}
