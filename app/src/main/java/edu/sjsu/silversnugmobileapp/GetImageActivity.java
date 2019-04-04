package edu.sjsu.silversnugmobileapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;

import com.google.gson.Gson;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.PhotoGallery;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PhotoGalleryResponse;

public class GetImageActivity extends AppCompatActivity {

    private ArrayList<Bitmap> images;
    private ArrayList<ImageDetails> imageDetails;
    private int columnWidth=500;
    Button get;
   // ImageGridViewAdapter adapter;
    private GridView imageGridview;
    String username;

    private RestClient restClient;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);
        imageGridview =(GridView) findViewById(R.id.my_grid_view);
        username=getIntent().getExtras().getString("username");

        restClient = new RestClient();
        gson = new Gson();

        loadPhotoGallery();
    }

    public void loadPhotoGallery()
    {
        String url = "/SilverSnug/PhotoGallery/getPhotoGallery?userName=" + "Ashwini";
        restClient.executeGetAPI(getApplicationContext(), url, new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                PhotoGalleryResponse response = gson.fromJson(jsonResponse.toString(), PhotoGalleryResponse.class);
                Log.i("AddressBookActivity", response.toString());

                List<PhotoGallery> responseList = response.getPhotogallery();
                for (PhotoGallery record : responseList) {
                    String name = record.getPhotoName();
                    String contactNumber = record.getContactNumber();
                    imageDetails.add(new ImageDetails(name,contactNumber));

                }
            }

                @Override public void onError(String message) {
                    Log.i("GetImageActivity", message);
                }
            });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadPhotoGallery();
    }
}
