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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.PillBoxRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.UserRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PillBoxResponse;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;

public class EditPill extends AppCompatActivity {

    private static ObjectMapper mapper = new ObjectMapper();
    private Gson gson;
    private RestClient restApiClient;
    private EditText pillnameEditText;
    private EditText pilldosageEditText;
    private EditText pillpotencyEditText;
    private EditText pillnotesEditText;
    PillBoxResponse pillResponse;
    PillBoxRequest pillRequest;
    Button Cancel, Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        pillnameEditText = new EditText(this);
        pilldosageEditText = new EditText(this);
        pillpotencyEditText=new EditText(this);
        pillnotesEditText=new EditText(this);
        Save = findViewById(R.id.Save);
        Cancel = findViewById(R.id.Cancel);

        restApiClient = new RestClient();
        gson = new Gson();

        Intent i = getIntent();
        Bundle b = i.getExtras();
    }


    public void savePillDetails(View view) {

        String url = "/SilverSnug/PillBox/editPill";
        final PillBoxRequest request = new PillBoxRequest();
        if (!(pillnameEditText.getText().equals(null)))
            request.setMedicineName(pillnameEditText.getText().toString());
        else {
            Toast.makeText(getApplicationContext(), "`Pill Name cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("PillBoxActivity", "Pill Name cannot be empty");
            return;
        }

        if (!(pilldosageEditText.getText().equals(null)))
            request.setDosage(pilldosageEditText.getText().toString());
        else {
            Toast.makeText(getApplicationContext(), "`Pill Dosage cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("PillBoxActivity", "Pill Dosage cannot be empty");
            return;
        }

        if (!(pillpotencyEditText.getText().equals(null)))
            request.setPotency(pillpotencyEditText.getText().toString());
        else {
            Toast.makeText(getApplicationContext(), "`Pill Potency cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("PillBoxActivity", "Pill Potency cannot be empty");
            return;
        }

        if (!(pillnotesEditText.getText().equals(null)))
            request.setNotes(pillnotesEditText.getText().toString());
        else {
            Toast.makeText(getApplicationContext(), "`Pill notes cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("PillBoxActivity", "Pill notes cannot be empty");
            return;
        }
        request.setUserId("680cdb82-c044-4dd1-ae84-1a15e54ab502");
        try {
            JSONObject jsonObject = new JSONObject(gson.toJson(request));
            restApiClient.executePostAPI(getApplicationContext(), url, jsonObject, new APICallback() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    PillBoxResponse response = gson.fromJson(jsonResponse.toString(), PillBoxResponse.class);
                    Log.i("PillBoxActivity", response.toString());
                    Intent intent = new Intent(EditPill.this, PillBoxActivity.class);
                    startActivity(intent);
                    Log.i("Save Pill Success", "Save User Success");
                    /* loadPillBoxList();*/
                }

                @Override
                public void onError(String message) {
                    Log.i("PillBoxActivity", message);
                }
            });

        } catch (JSONException e) {
            Log.e("PillBoxActivity", e.getMessage());
        }
    }
}
