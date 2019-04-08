package edu.sjsu.silversnugmobileapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
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
    private static final String AWS_BUCKET = "silversnugphotos";
    private static final String Key = "photos";

    Context ctx;
    private RestClient restClient;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_image);
        imageGridview =(GridView) findViewById(R.id.my_grid_view);
        imageDetails = new ArrayList<>();

        //username=getIntent().getExtras().getString("username");
        ctx = this;
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
                System.out.println("jsonResponse.toString()" + jsonResponse.toString());
                PhotoGalleryResponse response = gson.fromJson(jsonResponse.toString(), PhotoGalleryResponse.class);
                Log.i("PhotoGalleryActivity", response.toString());

                List<PhotoGallery> responseList = response.getPhotos();
                imageDetails = new ArrayList<>();
                System.out.println("Response: " + responseList);
                for (PhotoGallery record : responseList) {
                    String name = record.getPhotoName();
                    String contactNumber = record.getContactNumber();
                    String relationship = record.getRelationship();
                    String imagePath = record.getPhoto();

                    imageDetails.add(new ImageDetails(name,contactNumber,relationship,imagePath));
                }
                //adapter.notifyDataSetChanged();
                adapter = new ImageGridViewAdapter(ctx,R.layout.image_items, imageDetails);
                imageGridview.setAdapter(adapter);
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
