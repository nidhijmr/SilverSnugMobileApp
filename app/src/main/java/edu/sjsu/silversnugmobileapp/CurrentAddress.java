package edu.sjsu.silversnugmobileapp;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class CurrentAddress extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    TextView txtLoc;
    Button ShowLocations;
    public GoogleApiClient googleApiClient;



    private static String[] PERMS_STORAGE = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_address);
        txtLoc = findViewById(R.id.textAddress);
        if(LocationTracker.finalAddress == "")
            txtLoc.setText(LocationTracker.address);
        else
            txtLoc.setText(LocationTracker.finalAddress);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMS_STORAGE, 1);
        }
        else  checkkGglePlyService();
        IntentFilter intentToReceive = new IntentFilter();
        intentToReceive.addAction(LocationTracker.LOCATION_INTENT);
        this.registerReceiver(intentReceiver, intentToReceive, null, handler);
    }

//    private void stopLocUpdates() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
//    }

    private void showLocation() {

    }

    private boolean checkkGglePlyService() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(result)) {
                GooglePlayServicesUtil.getErrorDialog(result, this, 7172).show();
            } else {
                Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        showLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i("MainActivity", "MainActivity");
    }

    private final BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(LocationTracker.LOCATION_INTENT)) {
                if(intent.getExtras().getString("location").equals(null) || intent.getExtras().getString("location").equals("")) {
                     txtLoc.setText("325 E Tasman Dr, \n San Jose, CA 95134");
                }
                else
                    txtLoc.setText(intent.getExtras().getString("location"));
                Log.i("changed" , "changed");
            }
        }
    };
    private boolean rcvrsRgstrd = false;
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentToReceive = new IntentFilter();
        intentToReceive.addAction(LocationTracker.LOCATION_INTENT);
        this.registerReceiver(intentReceiver, intentToReceive, null, handler);
        rcvrsRgstrd = true;
    }
    @Override
    public void onPause() {
        super.onPause();
        if(rcvrsRgstrd) {
            unregisterReceiver(intentReceiver);
            rcvrsRgstrd = false;
        }
    }
}

