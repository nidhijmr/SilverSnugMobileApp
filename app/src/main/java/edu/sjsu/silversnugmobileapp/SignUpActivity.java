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

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.UserRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;

public class SignUpActivity extends AppCompatActivity {
    private Gson gson;
    private RestClient restApiClient;
    TextView userNameSignUp, emailIDSignUp, emergContactNumberSignUp, firstNameSignUp, lastNameSignUp, passwordSignUp, phoneNumberSignUp, roleSignUp;
    UserResponse userResponse;
    UserRequest userRequest;
    Button CancelSignUp, SaveSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
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

        userNameSignUp = findViewById(R.id.txtuserNameProfileSignUp);
        emailIDSignUp = findViewById(R.id.txtemailIdProfileSignUp);
        emergContactNumberSignUp = findViewById(R.id.txtemergencyContactNumberProfileSignUp);
        firstNameSignUp = findViewById(R.id.txtfirstNameProfileSignUp);
        lastNameSignUp = findViewById(R.id.txtlastNameProfileSignUp);
        passwordSignUp = findViewById(R.id.txtpasswordProfileSignUp);
        phoneNumberSignUp = findViewById(R.id.txtphoneNumberProfileSignUp);
        roleSignUp = findViewById(R.id.txtroleProfileSignUp);
        //profileImageProfileSignUp = findViewById(R.id.txtprofileImageProfileSignUp);
        SaveSignUp = findViewById(R.id.SaveSignUp);
        CancelSignUp = findViewById(R.id.CancelSignUp);

        restApiClient = new RestClient();
        gson = new Gson();
    }

    public void cancelSignUp(View view) {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        SignUpActivity.this.startActivity(intent);
    }

    public void saveUserProfile(View view){

        String url = "/SilverSnug/User/Signup";
        JSONObject userObject = new JSONObject();
        try {
            userObject.put("userName", userNameSignUp.getText());
            userObject.put("emailId", emailIDSignUp.getText());
            userObject.put("emergencyContactNumber", emergContactNumberSignUp.getText());
            userObject.put("firstName", firstNameSignUp.getText());
            userObject.put("lastName", lastNameSignUp.getText());
            userObject.put("password", passwordSignUp.getText());
            userObject.put("phoneNumber", phoneNumberSignUp.getText());
            //userObject.put("profileImage", profileImageProfileSignUp.getText());
            userObject.put("role", roleSignUp.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        restApiClient.executePostAPI(getApplicationContext(), url, userObject , new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                Toast.makeText(SignUpActivity.this, "SignUp Successfull, please login!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                SignUpActivity.this.startActivity(intent);
                //Log.i("Signup User Success", userResponse.toString());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(SignUpActivity.this, "Signup User error!", Toast.LENGTH_SHORT).show();
                Log.i("Edit User Request error", message);
            }
        });
    }

}
