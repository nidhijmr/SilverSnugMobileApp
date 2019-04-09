package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RestClient {

    private final String BASE_URL = "http://ec2-18-234-100-52.compute-1.amazonaws.com:8080"; //Local host URL

    public void executePostAPI(Context context, String uri, JSONObject jsonObject, final APICallback callback) {
        APIRequestQueue queue =  APIRequestQueue.getInstance(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + uri, jsonObject, new Response.Listener<JSONObject> () {

            @Override
            public void onResponse(JSONObject jsonResponse) {
                try {
                    callback.onSuccess(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("RestApiClient", "Error: " + error.getMessage());
                callback.onError(error.getMessage());
            }
        });
        queue.getRequestQueue().add(jsonObjectRequest);
    }

    public void executeGetAPI(Context context, String uri, final APICallback callback) {
        APIRequestQueue queue =  APIRequestQueue.getInstance(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  BASE_URL + uri, null, new Response.Listener<JSONObject> () {

            @Override
            public void onResponse(JSONObject jsonResponse) {
                try {
                    callback.onSuccess(jsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("RestApiClient", "Error: " + error.getMessage());
                callback.onError(error.getMessage());
            }
        });
        queue.getRequestQueue().add(jsonObjectRequest);
    }
}
