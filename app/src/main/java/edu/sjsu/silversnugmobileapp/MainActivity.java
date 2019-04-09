package edu.sjsu.silversnugmobileapp;


import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

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
import edu.sjsu.silversnugmobileapp.backgroundTasks.panicVoiceDetection;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    String phoneNumber;
    private String username,address;
    private Sensor accelerometer;
    private SensorManager accelerometerManager;
    Context context;
    long t1 = 0;
    long t2 = 0;
    boolean lessThan = false;
    boolean greaterThan = false;
    boolean callFlag=true;
    GPSLocation gps;
    String user_name;



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
        Intent panicVcDetect  = new Intent(MainActivity.this, panicVoiceDetection.class);
        panicVcDetect.putExtra("userName", userResponse.getUserName());
        getApplicationContext().startService(panicVcDetect);
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



    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double x = sensorEvent.values[0];
            double y = sensorEvent.values[1];
            double z = sensorEvent.values[2];

            double acVector = Math.sqrt(x * x + y * y + z * z);
            //System.out.println("acVector= " + acVector + " at " + System.currentTimeMillis());

            if (acVector > 20) {
                greaterThan = true;
                t1 = System.currentTimeMillis();
            } else if (acVector < 3) {
                lessThan = true;
                t2 = System.currentTimeMillis();
            }

            if (greaterThan && lessThan && callFlag) {
                if (t2 - t1 <= 1000 && t2 - t1 >100) {

                    callFlag=false;
                    greaterThan = false;
                    lessThan = false;
                    t1 = 0;
                    t2 = 0;
                    acVector=0;

                    gps = new GPSLocation(MainActivity.this);
                    Intent intent = new Intent();
                    if (gps.GetLocation()) {
                        address = gps.getAddress();
                        intent.putExtra("address", address);
                        intent.putExtra("username",username );
                    }
                    System.out.println("callFlag= "+ callFlag);
                    intent.setClass(this, FallDetection.class);
                    startActivity(intent);
                    System.out.println("returned to dashboard on sensor change from fall detection");
                }
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public void goToVewProfile(View view) {

        Intent intent = new Intent(MainActivity.this, ViewProfile.class);
        intent.putExtra("userResponse", userResponse);
        MainActivity.this.startActivity(intent);
    }

    public void goToCurrentAddress(View view) {
        Intent intent = new Intent(MainActivity.this, CurrentAddress.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToTaskSchedule(View view) {
        Intent intent = new Intent(MainActivity.this, MainActivity4.class);
        intent.putExtra("userResponse", userResponse);
        MainActivity.this.startActivity(intent);
    }
    
}

