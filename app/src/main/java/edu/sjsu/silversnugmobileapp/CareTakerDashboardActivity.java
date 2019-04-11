package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;

public class CareTakerDashboardActivity extends AppCompatActivity {

    UserResponse userResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_taker_dashboard);
       /* Toolbar toolbar = findViewById(R.id.toolbar);*/
        getSupportActionBar().setTitle(" Caretaker DashBoard");
        //getSupportActionBar().setTitle("DashBoard");

        Intent i = getIntent();
        Bundle b = i.getExtras();
        userResponse = (UserResponse) b.get("userResponse");
        Log.i("userResponse: ", userResponse.toString());

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        /* inflater.inflate(R.menu.deleteaccount, menu);*/
        return super.onCreateOptionsMenu(menu);
    }


    public void goToAnomalyDetector(View view) {
        Intent intent = new Intent(CareTakerDashboardActivity.this, UserLocationsMapsActivity.class);
        intent.putExtra("userResponse", userResponse);
        CareTakerDashboardActivity.this.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            finish();
            Intent patientDashboardIntent = new Intent(CareTakerDashboardActivity.this, LoginActivity.class);
            /*patientDashboardIntent.putExtra("username", username);*/
            startActivity(patientDashboardIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goTocareTakerViewProfile(View view) {
        Intent intent = new Intent(CareTakerDashboardActivity.this, ViewProfile.class);
        intent.putExtra("userResponse", userResponse);
        CareTakerDashboardActivity.this.startActivity(intent);
    }

    }
