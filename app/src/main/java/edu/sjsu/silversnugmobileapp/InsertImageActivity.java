package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.PhotoGalleryRequest;

public class InsertImageActivity extends AppCompatActivity implements View.OnClickListener {

    Button choosePic;
    TextView name, relation, contactNumber;
    EditText ename, erelation, econtactNumber;
    private static final int PICK_IMAGE=100;
    private static final int RESULT_OK= -1;
    String sname, srelation, scontactNumber;
    String username;
    private Gson gson;
    private RestClient restApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_image);
        choosePic = (Button) findViewById(R.id.button_choosePic);
        ename = (EditText) findViewById(R.id.editText3);
        erelation = (EditText) findViewById(R.id.editText4);
        econtactNumber = (EditText) findViewById(R.id.editText5);
        choosePic.setOnClickListener(this);
        username=getIntent().getExtras().getString("username");
        restApiClient = new RestClient();
        gson = new Gson();
        addPhoto();

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
        Intent intent= new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,PICK_IMAGE );

    }

    public void addPhoto()
    {
        String url = "/SilverSnug/PhotoGallery/addPhoto";
        final PhotoGalleryRequest request = new PhotoGalleryRequest();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri uri = data.getData();
            String x = getPath(uri);
            sname = ename.getText().toString();
            srelation = erelation.getText().toString();
            scontactNumber= econtactNumber.getText().toString();

        }
    }


    public String getPath(Uri uri)
    {
        if(uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri,projection,null,null,null);
        if(cursor!=null)
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();

    }

}
