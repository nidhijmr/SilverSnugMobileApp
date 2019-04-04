package edu.sjsu.silversnugmobileapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.PillBox;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.PillBoxRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PillBoxResponse;
import edu.sjsu.silversnugmobileapp.utilities.RVAdapter;

public class PillBoxActivity extends AppCompatActivity {

    ListView lvItems;
    ArrayAdapter<String> mAdapter;
    private RestClient restApiClient;
    private EditText pillnameEditText;
    private EditText pilldosageEditText;
    private EditText pillpotencyEditText;
    private EditText pillnotesEditText;
    private List<String> labelsList = new ArrayList<>();
    private PillBoxResponse response;
    private Gson gson;
    private TextView pillTextView;
    ArrayAdapter<List<PillBox>> adapter;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pill_box);
        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        restApiClient = new RestClient();
        gson = new Gson();
        loadPillBoxList();
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    public void loadPillBoxList() {
        String url = "/SilverSnug/PillBox/getPill?userId=" + "680cdb82-c044-4dd1-ae84-1a15e54ab502";
        restApiClient.executeGetAPI(getApplicationContext(), url, new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                PillBoxResponse response = gson.fromJson(jsonResponse.toString(), PillBoxResponse.class);
                Log.i("PillBoxActivity", response.toString());

                List<PillBox> responseList = response.getPillBoxes();
                labelsList.clear();

                for (PillBox record : responseList)
                    labelsList.add(record.getMedicineName() + '-' + record.getDosage() + '-' + record.getPotency() + '-' + record.getNotes());

                final RVAdapter adapter = new RVAdapter(labelsList);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {

                Log.i("PillBoxActivity", message);
            }
    });
        }


        public void addPillBox(View view) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            PillBoxRequest request = new PillBoxRequest();

            pillnameEditText = new EditText(this);
            pilldosageEditText = new EditText(this);
            pillpotencyEditText=new EditText(this);
            pillnotesEditText=new EditText(this);
            pillnameEditText.setHint("Add Pill Name");
            layout.addView(pillnameEditText);
            pillpotencyEditText.setHint("Add potency");
            layout.addView(pillpotencyEditText);
            pilldosageEditText.setHint("Add dosage");
            layout.addView(pilldosageEditText);
            pillnotesEditText.setHint("Add notes");
            layout.addView(pillnotesEditText);

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setView(layout)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            addPill();
                        }
                    })
                    .setNegativeButton("Cancel", null).create();
            dialog.show();
        }


    public void addPill() {
        String url = "/SilverSnug/PillBox/addPill";
        final PillBoxRequest request = new PillBoxRequest();
        if(!(pillnameEditText.getText().equals(null)))
            request.setMedicineName(pillnameEditText.getText().toString());
        else {
            Toast.makeText(getApplicationContext(), "`Pill Name cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("PillBoxActivity", "Pill Name cannot be empty");
            return;
        }

        if(!(pilldosageEditText.getText().equals(null)))
            request.setDosage(pilldosageEditText.getText().toString());
        else {
            Toast.makeText(getApplicationContext(), "`Pill Dosage cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("PillBoxActivity", "Pill Dosage cannot be empty");
            return;
        }

        if(!(pillpotencyEditText.getText().equals(null)))
            request.setPotency(pillpotencyEditText.getText().toString());
        else {
            Toast.makeText(getApplicationContext(), "`Pill Potency cannot be empty", Toast.LENGTH_LONG).show();
            Log.e("PillBoxActivity", "Pill Potency cannot be empty");
            return;
        }

        if(!(pillnotesEditText.getText().equals(null)))
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
                    loadPillBoxList();
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




