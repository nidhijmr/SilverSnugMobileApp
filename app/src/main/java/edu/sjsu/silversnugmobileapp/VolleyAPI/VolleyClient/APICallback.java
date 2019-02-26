package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient;

import org.json.JSONObject;

public interface APICallback {
    void onSuccess(JSONObject jsonResponse);
    void onError(String message);
}
