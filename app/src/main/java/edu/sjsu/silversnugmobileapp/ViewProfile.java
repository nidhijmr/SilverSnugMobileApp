package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.PillBox;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PillBoxResponse;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;
import edu.sjsu.silversnugmobileapp.utilities.RVAdapter;

public class ViewProfile extends AppCompatActivity {

    private Gson gson;
    private RestClient restApiClient;
    TextView userName, emailID, emergContactNumber, firstName, lastName, password, phoneNumber, role;
    Button EditProfile;
    UserResponse userResponse;
    String user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userName = findViewById(R.id.txtuserNameProfile);
        emailID = findViewById(R.id.txtemailIdProfile);
        emergContactNumber = findViewById(R.id.txtemergencyContactNumberProfile);
        firstName = findViewById(R.id.txtfirstNameProfile);
        lastName = findViewById(R.id.txtlastNameProfile);
        password = findViewById(R.id.txtpasswordProfile);
        phoneNumber = findViewById(R.id.txtphoneNumberProfile);
        role = findViewById(R.id.txtroleProfile);

        EditProfile = findViewById(R.id.EditProfile);
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this, EditProfile.class);
                intent.putExtra("userResponse", userResponse);
                ViewProfile.this.startActivity(intent);
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        restApiClient = new RestClient();
        gson = new Gson();

        Intent i = getIntent();
        Bundle b =  i.getExtras();
        user_name =  b.get("userName").toString();
        getUser(user_name);

    }


    public void getUser(String user_name) {
        Log.i("UserResponse", user_name);
        String url = "/SilverSnug/User/GetUser?userName=" + user_name;
        restApiClient.executeGetAPI(getApplicationContext(), url, new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                userResponse = gson.fromJson(jsonResponse.toString(), UserResponse.class);
                Log.i("UserResponse", userResponse.toString());
                userName.setText(userResponse.getUserName());
                emailID.setText(userResponse.getEmailId());
                emergContactNumber.setText(userResponse.getEmergencyContactNumber());
                firstName.setText(userResponse.getFirstName());
                lastName.setText(userResponse.getLastName());
                password.setText(userResponse.getPassword());
                phoneNumber.setText(userResponse.getPhoneNumber());
                role.setText(userResponse.getRole());
            }

            @Override
            public void onError(String message) {

                Log.i("UserResponse", message);
            }
        });
    }

}
