package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.EditPhotoRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PhotoGalleryResponse;

public class EditPhoto extends AppCompatActivity {

    private EditText contactNumberEditText;
    Button Cancel, save;

    private Gson gson;
    private RestClient restClient;

    String userId, photoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        contactNumberEditText= (EditText) findViewById(R.id.contactNumberEdit);
        save= (Button) findViewById(R.id.button_save);
        Cancel=(Button) findViewById(R.id.button_cancel);

        restClient = new RestClient();
        gson = new Gson();

        userId= getIntent().getStringExtra("userId");
        photoName=getIntent().getStringExtra("photoName");

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                saveContactNumber();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent getImageIntent = new Intent(EditPhoto.this, GetImageActivity.class);
                getImageIntent.putExtra("userId", userId);
                startActivity(getImageIntent);
            }
        });


    }


    public void saveContactNumber()
    {
        String url = "/SilverSnug/PhotoGallery/editPhoto";

        final EditPhotoRequest request = new EditPhotoRequest();

        request.setPhotoName(photoName);
        request.setUserId(userId);

        if (!(contactNumberEditText.getText().equals(null))) {
            request.setContactNumber(contactNumberEditText.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Contact Number cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("EditPhotoActivity", "Contact Number cannot be empty");
            return;
        }

        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(request));
            restClient.executePostAPI(getApplicationContext(), url, jsonObject, new APICallback() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    PhotoGalleryResponse response = gson.fromJson(jsonResponse.toString(), PhotoGalleryResponse.class);
                    Log.i("EditPhotoActivity", response.toString());
                    Toast.makeText(getApplicationContext(), "Contact Number saved successfully!", Toast.LENGTH_LONG).show();
                    Intent getImageIntent = new Intent(EditPhoto.this, GetImageActivity.class);
                    getImageIntent.putExtra("userId", userId);
                    startActivity(getImageIntent);

                }

                @Override
                public void onError(String message) {
                    Log.i("EditPhotoActivity", message);
                }
            });

        } catch (JSONException e) {
            Log.e("EditPhotoActivity", e.getMessage());
        }

    }


}
