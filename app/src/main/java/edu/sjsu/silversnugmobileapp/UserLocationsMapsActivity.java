package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.location.Address;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import org.json.JSONArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.UserLocation;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserLocationResponse;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;

public class UserLocationsMapsActivity extends FragmentActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    UserResponse userResponse;
    private Gson gson;
    private RestClient restApiClient;
    List<UserLocation> responseList;
    private Geocoder geocoder;
    private List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        restApiClient = new RestClient();
        responseList = new ArrayList<>();
        Intent i = getIntent();
        Bundle b =  i.getExtras();
        userResponse =  (UserResponse)b.get("userResponse");
        Log.i("userResponse: ", userResponse.toString());
        gson = new Gson();
        //getUserLocations(userResponse.getUserName());
        FetchLocations();

        setContentView(R.layout.activity_user_locations_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void getUserLocations(String user_name) {
        Log.i("UserResponse", user_name);
        String url = "/SilverSnug/User/GetUserLocations?userName=" + "nidhi";
        restApiClient.executeGetAPI(getApplicationContext(), url, new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) throws JSONException {

                UserLocationResponse userLocationResponse = gson.fromJson(jsonResponse.toString(), UserLocationResponse.class);
                Log.i("userLocationResponse", userLocationResponse.toString());

                responseList = userLocationResponse.getUserLocations();
            }

            @Override
            public void onError(String message) {

                Log.i("UserResponse", message);
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);
        googleMap.moveCamera(zoom);
        googleMap.animateCamera(zoom);
        try {
            googleMap.setMyLocationEnabled(true);

        } catch (SecurityException se) {

        }
        googleMap.setTrafficEnabled(true);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        Log.i("mao got location: ", responseList.toString());
        for(int i = 0; i< responseList.size(); i++) {
            LatLng tempLoc = new LatLng(Double.parseDouble(responseList.get(i).getLatitude()), Double.parseDouble(responseList.get(i).getLongitude()));
            geocoder = new Geocoder(this, Locale.getDefault());
            String finalAddress = "";
            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(responseList.get(i).getLatitude()), Double.parseDouble(responseList.get(i).getLongitude()), 1);
                if(addresses.size() > 0) {
                    for (int x = 0; x < addresses.get(0).getMaxAddressLineIndex(); x++) {
                        finalAddress += addresses.get(0).getAddressLine(x) + " ";
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();

            }
            if(Double.parseDouble(responseList.get(i).getAnomalyScore())<1.0)
                googleMap.addMarker(new MarkerOptions().position(tempLoc));
            else
                googleMap.addMarker(new MarkerOptions().position(tempLoc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(tempLoc));
        }
        googleMap.setOnMarkerClickListener(this);

    }

    public void FetchLocations() {

        Thread thrFetchLoc =  new Thread(new Runnable() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public void run() {
                getUserLocations(userResponse.getUserName());
            }
        });
        thrFetchLoc.start();

        try {
            thrFetchLoc.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println("Inside onMarkerClick");
        geocoder = new Geocoder(this, Locale.getDefault());
        String finalAddress = "test address";
        try {
            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 2);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            finalAddress=address + city + state +country + postalCode;

        } catch (IOException e) {
            e.printStackTrace();

        }
        System.out.println("Final address= "+finalAddress);

        marker.setTitle(finalAddress);
        marker.showInfoWindow();
        return true;
    }
}
