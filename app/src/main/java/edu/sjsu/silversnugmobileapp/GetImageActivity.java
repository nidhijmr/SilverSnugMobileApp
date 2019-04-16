package edu.sjsu.silversnugmobileapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.PhotoGallery;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.PhotoGalleryRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PhotoGalleryResponse;

public class GetImageActivity extends AppCompatActivity {

    private ArrayList<Bitmap> images;
    private ArrayList<ImageDetails> imageDetails;
    private int columnWidth=500;
    Button get;
    ImageGridViewAdapter adapter;
    private GridView imageGridview;
    String userId;
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

        userId=getIntent().getExtras().getString("userId");
        ctx = this;
        restClient = new RestClient();
        gson = new Gson();

        imageGridview.setLongClickable(true);
        imageGridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog =  new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Photo")
                        .setMessage("Are you sure you want to delete this photo?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ImageDetails photoSelectedToDelete = imageDetails.get(position);
                                String photoName= photoSelectedToDelete.getName();
                                deletePhoto(photoName);
                                loadPhotoGallery();
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
           // }
       // }));
                return false;
            }
        });


        loadPhotoGallery();
    }

    public void loadPhotoGallery()
    {
        System.out.println("userId "+ userId);
        String url = "/SilverSnug/PhotoGallery/getPhotoGallery?userId=" + userId;
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
                    System.out.println("imagePath "+ imagePath);

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

    public void deletePhoto(String photoName) {

        String url = "/SilverSnug/PhotoGallery/deletePhoto?userId=" + "680cdb82-c044-4dd1-ae84-1a15e54ab502" +"&photoName="+photoName;
        PhotoGalleryRequest request = new PhotoGalleryRequest();
        try{
            JSONObject jsonObject = new JSONObject(gson.toJson(request));
            restClient.executePostAPI(getApplicationContext(), url, jsonObject, new APICallback() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    PhotoGalleryResponse response = gson.fromJson(jsonResponse.toString(), PhotoGalleryResponse.class);
                    Log.i("PhotoGalleryActivity", response.toString());
                    Toast.makeText(getApplicationContext(), "Photo deleted successfully!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String message) {
                    Log.i("PhotoGalleryActivity", message);
                }
            });

        } catch (JSONException e) {
            String err = (e.getMessage()==null)?"Pill deletion failed":e.getMessage();
            Log.e("PhotoGalleryActivity", err);
        }
    }
}
