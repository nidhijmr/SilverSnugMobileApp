package edu.sjsu.silversnugmobileapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.PillBoxRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.UserRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PillBoxResponse;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;

public class AddPillBox extends AppCompatActivity {

    private static ObjectMapper mapper = new ObjectMapper();
    private Gson gson;
    private RestClient restApiClient;
    PillBoxResponse pillBoxResponse;
    PillBoxRequest pillBoxRequest;
    Button Add, Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pill_box);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }



}
