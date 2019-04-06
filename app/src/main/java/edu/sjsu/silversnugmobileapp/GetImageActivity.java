package edu.sjsu.silversnugmobileapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.PhotoGallery;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PhotoGalleryResponse;

public class GetImageActivity extends AppCompatActivity {

    private ArrayList<Bitmap> images;
    private ArrayList<ImageDetails> imageDetails;
    private int columnWidth=500;
    Button get;
    ImageGridViewAdapter adapter;
    private GridView imageGridview;
    String username;
    ImageView imageView;

    private RestClient restClient;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);
        imageGridview =(GridView) findViewById(R.id.my_grid_view);
        //username=getIntent().getExtras().getString("username");

        restClient = new RestClient();
        gson = new Gson();

        loadPhotoGallery();
    }

    public void loadPhotoGallery()
    {
        String url = "/SilverSnug/PhotoGallery/getPhotoGallery?userId=" + "680cdb82-c044-4dd1-ae84-1a15e54ab502";
        restClient.executeGetAPI(getApplicationContext(), url, new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                PhotoGalleryResponse response = gson.fromJson(jsonResponse.toString(), PhotoGalleryResponse.class);
                Log.i("PhotoGalleryActivity", response.toString());

                List<PhotoGallery> responseList = response.getPhotogallery();
                for (PhotoGallery record : responseList) {
                    String name = record.getPhotoName();
                    String contactNumber = record.getContactNumber();
                    String relationship = record.getRelationship();
                    String imagePath = record.getPhoto();
                    imageDetails.add(new ImageDetails(name,contactNumber,relationship,imagePath));
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
