package edu.sjsu.silversnugmobileapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("DashBoard");
    }

    public void goToAddressBook(View view) {
        Intent intent = new Intent(MainActivity.this, AddressBookActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToPillBox(View view) {
       Intent intent = new Intent(MainActivity.this, PillBoxActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToPhotoAlbum(View view) {
      Intent intent = new Intent(MainActivity.this, PhotoAlbumActivity.class);
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

}
