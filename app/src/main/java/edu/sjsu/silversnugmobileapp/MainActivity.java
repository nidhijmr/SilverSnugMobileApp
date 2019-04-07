package edu.sjsu.silversnugmobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;

public class MainActivity extends AppCompatActivity {

    UserResponse userResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("DashBoard");

        Intent i = getIntent();
        Bundle b =  i.getExtras();
        userResponse =  (UserResponse)b.get("userResponse");
        Log.i("userResponse: ", userResponse.toString());

        //user_name = i.getStringExtra("userName");

        Intent intent=new Intent(MainActivity.this,LocationTracker.class);
        intent.putExtra("userName", userResponse.getUserName());
        getApplicationContext().startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.myprofile, menu);
        inflater.inflate(R.menu.logout, menu);
        inflater.inflate(R.menu.deleteaccount, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();

        if(id==R.id.logout)
        {
            finish();
            Intent patientDashboardIntent = new Intent(MainActivity.this, LoginActivity.class);
            /*patientDashboardIntent.putExtra("username", username);*/
            startActivity(patientDashboardIntent);
            return true;
        }

        if(id==R.id.myprofile)
        {
            Intent intent = new Intent(MainActivity.this, ViewProfile.class);
            intent.putExtra("userResponse", userResponse);
            MainActivity.this.startActivity(intent);
        }

        if(id== R.id.deleteaccount)
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Delete Account");
            alertDialogBuilder
                    .setMessage("Are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            //db.deleteAccount(username);
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    public void goToAddressBook(View view) {
        Intent intent = new Intent(MainActivity.this, AddressBookActivity.class);
        intent.putExtra("userResponse", userResponse);
        MainActivity.this.startActivity(intent);
    }

    public void goToPillBox(View view) {
       Intent intent = new Intent(MainActivity.this, PillBoxActivity.class);
       intent.putExtra("userResponse", userResponse);
       MainActivity.this.startActivity(intent);
    }

    public void goToPhotoAlbum(View view) {
      Intent intent = new Intent(MainActivity.this, PhotoAlbumActivity.class);
      intent.putExtra("userResponse", userResponse);
      MainActivity.this.startActivity(intent);
    }

    public void goToViewProfile(View view) {
        Intent intent = new Intent(MainActivity.this, ViewProfile.class);
        intent.putExtra("userResponse", userResponse);
        MainActivity.this.startActivity(intent);
    }

    public void goToCurrentAddress(View view) {
        Intent intent = new Intent(MainActivity.this, CurrentAddress.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToTaskSchedule (View view) {

        Intent intent = getPackageManager().getLaunchIntentForPackage("me.neelmehta.hack4health");
        startActivity(intent);
    }
}

