package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.PhotoGalleryRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PhotoGalleryResponse;

public class InsertImageActivity extends AppCompatActivity implements View.OnClickListener {

    Button choosePic, save;
    TextView name, relation, contactNumber;
    EditText ename, erelation, econtactNumber;
    private static final int PICK_IMAGE = 100;
    private static final int RESULT_OK = -1;
    String sname, srelation, scontactNumber;
    String userId;
    private Gson gson;
    private RestClient restApiClient;
    private static final String AWS_KEY = "";
    private static final String AWS_SECRET = "";
    private static final String AWS_BUCKET = "silversnugphotos";
    ImageView mImage;
    String imagePath;
    TransferUtility transferUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_image);
        choosePic = (Button) findViewById(R.id.button_choosePic);
        save = (Button) findViewById(R.id.button_save);
        ename = (EditText) findViewById(R.id.editText3);
        erelation = (EditText) findViewById(R.id.editText4);
        econtactNumber = (EditText) findViewById(R.id.editText5);
        choosePic.setOnClickListener(this);
        restApiClient = new RestClient();
        gson = new Gson();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPhoto();
            }
        });
        userId= getIntent().getStringExtra("userId");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    @Override
    public void onClick(View view) {
        System.out.println("Select Picture button clicked");
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    public void addPhoto() {
        String url = "/SilverSnug/PhotoGallery/addPhoto";
        final PhotoGalleryRequest request = new PhotoGalleryRequest();

        if (!(ename.getText().equals(null))) {
            request.setPhotoName(ename.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Name cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("PhotoGalleryActivity", "Name cannot be empty");
            return;
        }

        if (!(erelation.getText().equals(null))) {
            request.setRelationship(erelation.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Relationship cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("PhotoGalleryActivity", "Relationship cannot be empty");
            return;
        }

        if (!(econtactNumber.getText().equals(null))) {
            request.setContactNumber(econtactNumber.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Contact Number cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("PhotoGalleryActivity", "Contact Number cannot be empty");
            return;
        }

        request.setUserId(userId);

        System.out.println("ImagePath=" + imagePath);

        if (!(imagePath.equals(null))) {
            request.setPhoto(imagePath);
        } else {
            Toast.makeText(getApplicationContext(), "Photo cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("PhotoGalleryActivity", "Photo cannot be empty");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(request));
            restApiClient.executePostAPI(getApplicationContext(), url, jsonObject, new APICallback() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    PhotoGalleryResponse response = gson.fromJson(jsonResponse.toString(), PhotoGalleryResponse.class);
                    Log.i("PhotoGalleryActivity", response.toString());
                    Intent getImageIntent = new Intent(InsertImageActivity.this, GetImageActivity.class);
                    getImageIntent.putExtra("userId", userId);
                    startActivity(getImageIntent);
                }

                @Override
                public void onError(String message) {
                    Log.i("PhotoGalleryActivity", message);
                }
            });

        } catch (JSONException e) {
            Log.e("PhotoGalleryActivity", e.getMessage());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri selectedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            final String picturePath = c.getString(columnIndex);
            c.close();
            Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            // mImage.setImageBitmap(thumbnail);
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {

                        uploadImageToAWS(picturePath);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }

    }


    private void uploadImageToAWS(String selectedImagePath) {

        if (selectedImagePath == null) {
            Toast.makeText(this, "Could not find the filepath of the selected file", Toast.LENGTH_LONG).show();
            return;
        }

        File file = new File(selectedImagePath);


        AmazonS3 s3Client = null;
        if (s3Client == null) {
            ClientConfiguration clientConfig = new ClientConfiguration();
            clientConfig.setProtocol(Protocol.HTTP);
            clientConfig.setMaxErrorRetry(0);
            clientConfig.setSocketTimeout(60000);

            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
                    "", // Identity pool ID
                    Regions.US_EAST_1 // Region
            );

            s3Client = new AmazonS3Client(credentialsProvider, clientConfig);
            s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));

        }

        FileInputStream stream = null;

        try {

            stream = new FileInputStream(file);

            ObjectMetadata objectMetadata = new ObjectMetadata();

            Log.d("message", "converting to bytes");

            objectMetadata.setContentLength(file.length());

            String[] s = selectedImagePath.split("\\.");

            String extention = s[s.length - 1];

            Log.d("message", "set content length : " + file.length() + "sss" + extention);

            String fileName = UUID.randomUUID().toString();

            PutObjectRequest putObjectRequest = new PutObjectRequest(AWS_BUCKET, "photos/" + fileName + "." + extention, stream, objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead);

            PutObjectResult result = s3Client.putObject(putObjectRequest);

            imagePath = "https://s3.amazonaws.com/silversnugphotos/photos/" + fileName +"."+ extention;


            runOnUiThread(new Runnable() {

                public void run() {

                }

            });


            if (result == null) {

                Log.e("RESULT", "NULL");

            } else {

                Log.e("RESULT", result.toString());

            }

        } catch (Exception e) {

            Log.d("ERROR", " " + e.getMessage());

            e.printStackTrace();

        }

    }

}
