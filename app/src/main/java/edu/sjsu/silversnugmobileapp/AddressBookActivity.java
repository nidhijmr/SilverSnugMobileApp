package edu.sjsu.silversnugmobileapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.AddressBook;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.AddressBookCoordinates;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.AddressBookRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.AddressBookResponse;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;
import edu.sjsu.silversnugmobileapp.utilities.RVAdapter;
import edu.sjsu.silversnugmobileapp.utilities.RecyclerTouchListener;
import edu.sjsu.silversnugmobileapp.utilities.UIListner;

public class AddressBookActivity extends AppCompatActivity implements UIListner {

    private RestClient restClient;
    private List<String> labelsList = new ArrayList<>();
    private List<AddressBookCoordinates> coordinatesList = new ArrayList<>();
    private List<AddressBook> responseList = new ArrayList<>();
    private Gson gson;
    private RVAdapter adapter = null;
    private RecyclerView rv;
    private EditText addressBookNameEditText;
    private EditText addressBookAddressEditText;
    private UserResponse userResponse;
    public boolean subelement = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address_book);

        rv = findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        restClient = new RestClient();

        gson = new Gson();

        adapter = new RVAdapter(labelsList);
        rv.setAdapter(adapter);

        rv.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rv, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                    double latitude = 0;
                    double longitude = 0;
                    String addressSelected = labelsList.get(position);

                    //Call intent to show navigation on Google maps
                    for (AddressBookCoordinates entry : coordinatesList) {
                        if (entry.getAddressName() == addressSelected) {
                            latitude = entry.getLatitude();
                            longitude = entry.getLongitude();
                        }
                    }

                    String location = latitude + "," + longitude;
                    Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=" + location);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

            }

            @Override
            public void onLongClick(View view, final int position) {
                AlertDialog dialog =  new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Address")
                        .setMessage("Are you sure you want to delete this address?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String addressSelectedToRemove = labelsList.get(position);
                                removeAddress(addressSelectedToRemove);
                                loadAddressBookList();
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            @Override
            public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
                return false;
            }

            }));
        loadAddressBookList();
        getSupportActionBar().setTitle("Address Book");
        Intent i = getIntent();
        Bundle b =  i.getExtras();
        userResponse =  (UserResponse)b.get("userResponse");
        Log.i("userResponse: ", userResponse.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void loadAddressBookList() {

        if(userResponse==null){
            Intent i = getIntent();
            Bundle b =  i.getExtras();
            userResponse =  (UserResponse)b.get("userResponse");
            Log.i("userResponse: ", userResponse.toString());
        }

        //Call to GET REST API to get all saved Addresses
        String url = "/SilverSnug/Address/getAddress?userId=" + userResponse.getUserId();
        restClient.executeGetAPI(getApplicationContext(), url, new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                AddressBookResponse response = gson.fromJson(jsonResponse.toString(), AddressBookResponse.class);
                Log.i("AddressBookActivity", response.toString());

                responseList = response.getAddressBooks();
                labelsList.clear();
                if(responseList!=null) {
                    for (AddressBook record : responseList) {
                        labelsList.add(record.getAddressName());
                        coordinatesList.add(new AddressBookCoordinates(record.getAddressName(), record.getLatitude(), record.getLongitude()));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Log.i("AddressBookActivity", message);
            }
        });
    }

    public void addAddress(View view) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        addressBookNameEditText = new EditText(this);
        addressBookAddressEditText = new EditText(this);

        // Add a TextView here for the "Title" label, as noted in the comments
        addressBookNameEditText.setHint("Add Address Name");
        layout.addView(addressBookNameEditText); // Notice this is an add method

        // Add another TextView here for the "Description" label
        addressBookAddressEditText.setHint("Add Address");
        layout.addView(addressBookAddressEditText); // Another add method

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(layout)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addAddressBook();
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();
    }

    public void addAddressBook() {
        String url = "/SilverSnug/Address/addAddress";
        final AddressBookRequest request = new AddressBookRequest();

        //Set Address Name
        if(!(addressBookNameEditText.getText().equals(null)))
         request.setAddressName(addressBookNameEditText.getText().toString().trim());
        else {
            Toast.makeText(getApplicationContext(), "Address Name cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("AddressBookActivity", "Address Name cannot be empty");
            return;
        }

        //Set Address
        if(!(addressBookAddressEditText.getText().equals(null)))
            request.setAddress(addressBookAddressEditText.getText().toString().trim());
        else {
            Toast.makeText(getApplicationContext(), "Address cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("AddressBookActivity", "Address cannot be empty");
            return;
        }

        //Set Latitude and Longitude
        String address_parse = addressBookAddressEditText.getText().toString();
        if (address_parse.trim().equals("")) {
            //TODO error message return
            Toast.makeText(getApplicationContext(), "Address cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("AddressBookActivity", "Address cannot be empty");
            return;
        }
        address_parse = addressBookAddressEditText.getText().toString().replaceAll(" ", "+");
        address_parse = address_parse.replaceAll("#", "apt:");

        String geocoding_url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address_parse + "&key=<Google_API_Key>";
        Object result = new GetLatLongOperation(new GetLatLongOperation.AsynResponse() {
            @Override
            public void processFinish(String output) {
                //       TODO Handle empty string
                try {
                    JSONObject jsonObject = new JSONObject(output);

                    double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lng");

                    double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lat");
                    request.setLongitude(lng);
                    request.setLatitude(lat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Set UserID
                request.setUserId(userResponse.getUserId());

                //Call to POST REST API to save New Address
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(request));
                    restClient.executePostAPI(getApplicationContext(), "/SilverSnug/Address/addAddress", jsonObject, new APICallback() {
                        @Override
                        public void onSuccess(JSONObject jsonResponse) {
                            AddressBookResponse response = gson.fromJson(jsonResponse.toString(), AddressBookResponse.class);
                            Log.i("AddressBookActivity", response.toString());
                            loadAddressBookList();
                        }

                        @Override
                        public void onError(String message) {
                            Log.i("AddressBookActivity", message);
                        }
                    });

                } catch (JSONException e) {
                    Log.e("AddressBookActivity", e.getMessage());
                }

            }
        }).execute(geocoding_url);
    }

    public void removeAddress(String addressName) {
        //Call to POST REST API to remove Addresses
        String url = "/SilverSnug/Address/removeAddress?userId=" + userResponse.getUserId() +"&addressName="+addressName;
        AddressBookRequest request = new AddressBookRequest();
        try{
            JSONObject jsonObject = new JSONObject(gson.toJson(request));
            restClient.executePostAPI(getApplicationContext(), url, jsonObject, new APICallback() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    AddressBookResponse response = gson.fromJson(jsonResponse.toString(), AddressBookResponse.class);
                    Log.i("AddressBookActivity", response.toString());
                    Toast.makeText(getApplicationContext(), "Address deleted successfully!", Toast.LENGTH_LONG).show();
                    loadAddressBookList();
                }

                @Override
                public void onError(String message) {
                    Log.i("AddressBookActivity", message);
                }
            });
        } catch (JSONException e) {
            Log.e("AddressBookActivity", e.getMessage());
        }

    }

    public void editAddressBook(String addressId) {
        final AddressBookRequest request = new AddressBookRequest();

        //Set AddressId
        request.setAddressId(addressId);

        //Set Address Name
        if(!(addressBookNameEditText.getText().equals(null)))
            request.setAddressName(addressBookNameEditText.getText().toString().trim());
        else {
            Toast.makeText(getApplicationContext(), "Address Name cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("AddressBookActivity", "Address Name cannot be empty");
            return;
        }

        //Set Address
        if(!(addressBookAddressEditText.getText().equals(null)))
            request.setAddress(addressBookAddressEditText.getText().toString().trim());
        else {
            Toast.makeText(getApplicationContext(), "Address cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("AddressBookActivity", "Address cannot be empty");
            return;
        }

        //Set Latitude and Longitude
        String address_parse = addressBookAddressEditText.getText().toString();
        if (address_parse.trim().equals("")) {
            //TODO error message return
            Toast.makeText(getApplicationContext(), "Address cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("AddressBookActivity", "Address cannot be empty");
            return;
        }
        address_parse = addressBookAddressEditText.getText().toString().replaceAll(" ", "+");
        address_parse = address_parse.replaceAll("#", "apt:");

        String geocoding_url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address_parse + "&key=<Google_API_KEY>";
        Object result = new GetLatLongOperation(new GetLatLongOperation.AsynResponse() {
            @Override
            public void processFinish(String output) {
                //       TODO Handle empty string
                try {
                    JSONObject jsonObject = new JSONObject(output);

                    double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lng");

                    double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lat");
                    request.setLongitude(lng);
                    request.setLatitude(lat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Set UserID
                request.setUserId(userResponse.getUserId());

                //Call to POST REST API to save New Address
                try {
                    JSONObject jsonObject = new JSONObject(gson.toJson(request));
                    restClient.executePostAPI(getApplicationContext(), "/SilverSnug/Address/editAddress", jsonObject, new APICallback() {
                        @Override
                        public void onSuccess(JSONObject jsonResponse) {
                            AddressBookResponse response = gson.fromJson(jsonResponse.toString(), AddressBookResponse.class);
                            Log.i("AddressBookActivity", response.toString());
                            loadAddressBookList();
                        }

                        @Override
                        public void onError(String message) {
                            Log.i("AddressBookActivity", message);
                        }
                    });

                } catch (JSONException e) {
                    Log.e("AddressBookActivity", e.getMessage());
                }

            }
        }).execute(geocoding_url);

    }

    @Override
    public void editFunction(int position) {
        subelement = true;

//        Toast.makeText(getApplicationContext(), "U clicked on EDIT at position "+position+"  !!!!", Toast.LENGTH_LONG).show();

        final AddressBook addressToEdit = responseList.get(position);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        addressBookNameEditText = new EditText(this);
        addressBookAddressEditText = new EditText(this);

        addressBookNameEditText.setHint("Address Name");
        addressBookNameEditText.setText(addressToEdit.getAddressName());
        layout.addView(addressBookNameEditText);

        addressBookAddressEditText.setHint("Address");
        addressBookAddressEditText.setText(addressToEdit.getAddress());
        layout.addView(addressBookAddressEditText);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(layout)
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editAddressBook(addressToEdit.getAddressId());
                    }
                })
                .setNegativeButton("Cancel", null).create();
        dialog.show();


    }

    private static class GetLatLongOperation extends AsyncTask<String, Void, String> {

        public interface AsynResponse {
            void processFinish(String output);
        }

        AsynResponse asynResponse = null;

        public GetLatLongOperation(AsynResponse asynResponse) {
            this.asynResponse = asynResponse;
        }

        @Override
        protected String doInBackground(String... params) {
            String to_return = "";
            URL url_geo = null;
            try {
                url_geo = new URL(params[0]);
                HttpURLConnection conn;
                conn = (HttpURLConnection) url_geo.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                conn.setDoOutput(true);
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        to_return += line;
                    }
                    System.out.println(to_return.toString());
                } else {
                    to_return = "";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return to_return;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            asynResponse.processFinish(result);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
