package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;


public class LoginActivity extends AppCompatActivity {

    private RestClient restApiClient;
    private Gson gson;
    private RecyclerView rv;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button btnlogin;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        restApiClient = new RestClient();
        gson = new Gson();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        username = findViewById(R.id.txtUsername);
        password = findViewById(R.id.txtPassword);
        btnlogin = findViewById(R.id.btnLogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    public void loginUser() {
        String url = "/SilverSnug/User/login";

        if(username.getText() == null || username.getText().length()==0 || password.getText() == null || password.getText().length()==0) {
            Toast.makeText(LoginActivity.this, "Please enter username and password!", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("userName", username.getText());
            postparams.put("password", password.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        restApiClient.executePostAPI(getApplicationContext(), url, postparams , new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) throws JSONException {

                if (jsonResponse.getString("message").equals("Invalid username/credentials.")) {
                    Toast.makeText(LoginActivity.this, "Invalid username/credentials!", Toast.LENGTH_SHORT).show();
                    Log.e("error loggin in", "Invalid username/credentials");
                } else {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(intent);
                    Log.i("Success", "success");
                }
            }

            @Override
            public void onError(String message) {

                Log.i("error", message);
            }
        });
    }

}
