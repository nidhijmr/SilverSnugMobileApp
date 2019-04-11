package edu.sjsu.silversnugmobileapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.kinesis.kinesisrecorder.KinesisRecorder;
import com.amazonaws.regions.Regions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class LocationTracker extends Service implements ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public LocationTracker() {
    }
    private Geocoder geocoder;
    private List<Address> addresses;
    private Context ctx;
    public static final String LOCATION_INTENT = "LocationIntent";
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    JSONObject userLocationData = null;
    private KinesisRecorder kinesisRecorder;
    private String userName = "";
    public static String address = "";
    public static String finalAddress = "";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            userName = intent.getExtras().getString("userName");
        }
        catch (Exception ex){
            Log.e("Error: ", "No user name in location tracker");
        }
        ctx = getApplicationContext();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return flags;

        getGgleAPIClient();
        getLocRequest();



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener( new OnSuccessListener<Location>() {

                    @Override
                    public void onSuccess(Location location) {

                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                });

        if(googleApiClient !=null)
            googleApiClient.connect();
        Log.i("LOCATION TRACKING:","LOCATION TRACKING Starting");
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "", // Identity pool ID
                Regions.US_EAST_1 // Region
        );
        String kinesisDirectory = "Alzm-kinesis";
//        kinesisRecorder = new KinesisRecorder(
//                this.getDir(kinesisDirectory, 0),
//                Regions.US_EAST_1,
//                credentialsProvider
//        );

        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("RestrictedApi")
    private void getLocRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(.1f);
    }

    private void getGgleAPIClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

    }

    private void getLocUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        showLocation();
        getLocUpdates();
    }

    private void showLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        if (lastLocation != null) {
            Log.i("LastLoc", "location fetched");
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();

            userLocationData = new JSONObject();
            try {
                userLocationData.put("Patient_UserName", userName);
                userLocationData.put("Latitude", latitude);
                userLocationData.put("DateTime", Calendar.getInstance().getTime());
                userLocationData.put("ID", UUID.randomUUID().toString());
                userLocationData.put("Longitude", longitude);


            } catch (JSONException e) {
                Log.i("JSONException", "JSONException");
                e.printStackTrace();
            }

            Log.i("Location json is: ", userLocationData.toString());

            //          kinesis
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    kinesisRecorder.saveRecord(userLocationData.toString(),"input-alzheimer-stream");
//                    kinesisRecorder.submitAllRecords();
//                }
//            }).start();

            finalAddress = "";
            try {
                geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    //for (int x = 0; x < addresses.get(0).getMaxAddressLineIndex(); x++) {
                        finalAddress = addresses.get(0).getAddressLine(0) + " ";
                    //}
                    address = "" + latitude + ", " + longitude;
                    Intent i = new Intent();
                    i.setAction(LOCATION_INTENT);
                    if (finalAddress != "")
                        i.putExtra("location", finalAddress);
                    else
                        i.putExtra("location", address);
                    ctx.sendBroadcast(i);

                    Log.i("Address changed to:", address + " , " + finalAddress);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        lastLocation =location;
        showLocation();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if(googleApiClient !=null)
            googleApiClient.connect();
    }

    @Override
    public boolean stopService(Intent name) {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        if(googleApiClient !=null)
            googleApiClient.disconnect();
        return super.stopService(name);

    }


}