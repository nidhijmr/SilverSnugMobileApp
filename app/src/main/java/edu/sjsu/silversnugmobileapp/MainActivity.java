package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;

public class MainActivity extends AppCompatActivity {

    String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("DashBoard");

        Intent i = getIntent();
        Bundle b =  i.getExtras();
        user_name =  b.get("userName").toString();
        Log.i("username: ", user_name);

        //user_name = i.getStringExtra("userName");
    }

    public void goToAddressBook(View view) {
        Intent intent = new Intent(MainActivity.this, AddressBookActivity.class);
        intent.putExtra("userName", user_name);
        MainActivity.this.startActivity(intent);
    }

    public void goToPillBox(View view) {
       Intent intent = new Intent(MainActivity.this, PillBoxActivity.class);
       intent.putExtra("userName", user_name);
       MainActivity.this.startActivity(intent);
    }

    public void goToPhotoAlbum(View view) {
      Intent intent = new Intent(MainActivity.this, PhotoAlbumActivity.class);
      intent.putExtra("userName", user_name);
      MainActivity.this.startActivity(intent);
    }

    public void goToViewProfile(View view) {
        Intent intent = new Intent(MainActivity.this, ViewProfile.class);
        intent.putExtra("userName", user_name);
        MainActivity.this.startActivity(intent);
    }

    public void goToTaskSchedule (View view) {

        Intent intent = getPackageManager().getLaunchIntentForPackage("me.neelmehta.hack4health");
        startActivity(intent);
    }
}

