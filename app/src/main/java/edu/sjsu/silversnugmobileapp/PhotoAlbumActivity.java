package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import com.google.gson.Gson;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;

public class PhotoAlbumActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView insert, get;
    private static final int PICK_IMAGE = 100;
    private static final int RESULT_OK = -1;
    private GridView imageGridview;
    private int columnWidth = 500;
    String userId;
    private UserResponse userResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        getSupportActionBar().setTitle(" Photo Directory");
        insert = (CardView) findViewById(R.id.add_picture);
        get = (CardView) findViewById(R.id.display_pictures);
        insert.setOnClickListener(this);
        get.setOnClickListener((View.OnClickListener) this);
        userId= getIntent().getStringExtra("userId");
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getImageIntent = new Intent(PhotoAlbumActivity.this, GetImageActivity.class);
                getImageIntent.putExtra("userId", userId);
                startActivity(getImageIntent);

            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_picture:
                Intent insertIntent = new Intent(PhotoAlbumActivity.this, InsertImageActivity.class);
                insertIntent.putExtra("userId", userId);
                startActivity(insertIntent);
                break;
        }
    }

    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(PhotoAlbumActivity.this, MainActivity.class);
        //intent.putExtra("userResponse", userResponse);
        // intent.putExtra("userId", userResponse.getUserId());
        PhotoAlbumActivity.this.startActivity(intent);
    }*/
}
