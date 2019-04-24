package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.UserRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;

public class EditProfile extends AppCompatActivity {
    private static ObjectMapper mapper = new ObjectMapper();
    private Gson gson;
    private RestClient restApiClient;
    TextView userNameEdit, emailIDEdit, emergContactNumberEdit, firstNameEdit, lastNameEdit, passwordEdit, phoneNumberEdit, roleEdit;
    UserResponse userResponse;
    UserRequest userRequest;
    Button Cancel, Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        userNameEdit = findViewById(R.id.txtuserNameProfileEdit);
        emailIDEdit = findViewById(R.id.txtemailIdProfileEdit);
        emergContactNumberEdit = findViewById(R.id.txtemergencyContactNumberProfileEdit);
        firstNameEdit = findViewById(R.id.txtfirstNameProfileEdit);
        lastNameEdit = findViewById(R.id.txtlastNameProfileEdit);
        passwordEdit = findViewById(R.id.txtpasswordProfileEdit);
        phoneNumberEdit = findViewById(R.id.txtphoneNumberProfileEdit);
        roleEdit = findViewById(R.id.txtroleProfileEdit);
        //profileImageProfileEdit = findViewById(R.id.txtprofileImageProfileEdit);
        Save = findViewById(R.id.Save);
        Cancel = findViewById(R.id.Cancel);

        restApiClient = new RestClient();
        gson = new Gson();

        Intent i = getIntent();
        Bundle b =  i.getExtras();
        userResponse = (UserResponse)b.get("userResponse");
        Log.i("UserResponse", userResponse.toString());
        setUserDetails(userResponse);
    }

    public void setUserDetails(UserResponse userResponse) {

        if(userResponse != null) {
            userNameEdit.setText(userResponse.getUserName());
            emailIDEdit.setText(userResponse.getEmailId());
            emergContactNumberEdit.setText(userResponse.getEmergencyContactNumber());
            firstNameEdit.setText(userResponse.getFirstName());
            lastNameEdit.setText(userResponse.getLastName());
            passwordEdit.setText(userResponse.getPassword());
            phoneNumberEdit.setText(userResponse.getPhoneNumber());
            roleEdit.setText(userResponse.getRole());
        }
    }

    public void cancelEdit(View view) {
        Intent intent = new Intent(EditProfile.this, ViewProfile.class);
        intent.putExtra("userResponse", userResponse);
        EditProfile.this.startActivity(intent);
    }

    public void saveUserProfile(View view){

//        userRequest.setUserName(userNameEdit.getText().toString());
//        userRequest.setEmailId(emailIDEdit.getText().toString());
//        userRequest.setEmergencyContactNumber(emergContactNumberEdit.getText().toString());
//        userRequest.setFirstName(firstNameEdit.getText().toString());
//        userRequest.setLastName(lastNameEdit.getText().toString());
//        userRequest.setPassword(passwordEdit.getText().toString());
//        userRequest.setPhoneNumber(phoneNumberEdit.getText().toString());
//        userRequest.setProfileImage(profileImageProfileEdit.getText().toString());
//        userRequest.setRole(roleEdit.getText().toString())
//        JsonNode json = mapper.valueToTree(userRequest);

        String url = "/SilverSnug/User/EditUser";
        JSONObject userObject = new JSONObject();
        try {
            userObject.put("userName", userNameEdit.getText());
            userObject.put("emailId", emailIDEdit.getText());
            userObject.put("emergencyContactNumber", emergContactNumberEdit.getText());
            userObject.put("firstName", firstNameEdit.getText());
            userObject.put("lastName", lastNameEdit.getText());
            userObject.put("password", passwordEdit.getText());
            userObject.put("phoneNumber", phoneNumberEdit.getText());
           // userObject.put("profileImage", profileImageProfileEdit.getText());
            userObject.put("role", roleEdit.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        restApiClient.executePostAPI(getApplicationContext(), url, userObject , new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                Toast.makeText(EditProfile.this, "User Details saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfile.this, ViewProfile.class);
                intent.putExtra("userResponse", userResponse);
                EditProfile.this.startActivity(intent);
                Log.i("Save User Success", "Save User Success");
            }

            @Override
            public void onError(String message) {
                Toast.makeText(EditProfile.this, "Edit User Request error!", Toast.LENGTH_SHORT).show();
                Log.i("Edit User Request error", message);
            }
        });
    }

}

