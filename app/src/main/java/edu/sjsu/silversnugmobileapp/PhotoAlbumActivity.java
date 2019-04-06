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

public class PhotoAlbumActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView insert, get;
    private static final int PICK_IMAGE = 100;
    private static final int RESULT_OK = -1;
    private GridView imageGridview;
    private int columnWidth = 500;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        insert = (CardView) findViewById(R.id.add_picture);
        get = (CardView) findViewById(R.id.display_pictures);
        insert.setOnClickListener(this);
        get.setOnClickListener((View.OnClickListener) this);

        /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getImageIntent = new Intent(PhotoAlbumActivity.this, GetImageActivity.class);
                getImageIntent.putExtra("username", username);
                startActivity(getImageIntent);

            }
        });

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_picture:
                Intent insertIntent = new Intent(PhotoAlbumActivity.this, InsertImageActivity.class);
                insertIntent.putExtra("username", username);
                startActivity(insertIntent);
                break;
        }
    }
}
