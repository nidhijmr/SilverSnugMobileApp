package edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class APIRequestQueue {
    private static APIRequestQueue apiRequestQueue;
    private Context mContext;
    private RequestQueue mRequestQueue;

    private APIRequestQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized APIRequestQueue getInstance(Context context) {
        if (apiRequestQueue == null) {
            apiRequestQueue = new APIRequestQueue(context);
        }
        return apiRequestQueue;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
}
