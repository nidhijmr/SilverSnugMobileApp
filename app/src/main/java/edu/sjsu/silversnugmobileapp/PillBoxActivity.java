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
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.AddressBookRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.PillBoxRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.AddressBookResponse;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PillBoxResponse;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.UserResponse;
import edu.sjsu.silversnugmobileapp.utilities.RVAdapter;
import edu.sjsu.silversnugmobileapp.utilities.RecyclerTouchListener;

public class PillBoxActivity extends AppCompatActivity {

    ListView lvItems;
    ArrayAdapter<String> mAdapter;
    private RestClient restApiClient;
    private EditText pillnameEditText;
    private EditText pilldosageEditText;
    private EditText pillpotencyEditText;
    private Gson gson;
    private RVAdapter adapter = null;
    private RecyclerView rv;
    private EditText pillnotesEditText;
    private List<String> labelsList = new ArrayList<>();
    private PillBoxResponse response;
    private Gson gson;
    private TextView pillTextView;
    private UserResponse userResponse;
    private PillBox pillBox;

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

        adapter = new RVAdapter(labelsList);
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rv, new RecyclerTouchListener.ClickListener() {


            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, final int position) {
                AlertDialog dialog =  new AlertDialog.Builder(view.getContext())
                        .setTitle("Delete Pill")
                        .setMessage("Are you sure you want to delete this pill?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String pillSelectedToRemove = labelsList.get(position);
                                System.out.print(labelsList.get(position));
                                String[] pillname = labelsList.get(position).split("\n");
                                System.out.print(pillname);
                                String[] pillnamenew = pillname[0].split(":");

                                removePill(pillnamenew[1]);
                                loadPillBoxList();
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }));
                loadPillBoxList();
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

    public void loadPillBoxList() {
        if(userResponse==null){
            Intent i = getIntent();
            Bundle b =  i.getExtras();
            userResponse =  (UserResponse)b.get("userResponse");
            Log.i("userResponse: ", userResponse.toString());
        }
        String url = "/SilverSnug/PillBox/getPill?userId=" + userResponse.getUserId();
        restApiClient.executeGetAPI(getApplicationContext(), url, new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                PillBoxResponse response = gson.fromJson(jsonResponse.toString(), PillBoxResponse.class);
                Log.i("PillBoxActivity", response.toString());

                List<PillBox> responseList = response.getPillBoxes();
                labelsList.clear();

                if(responseList != null) {
                    for (PillBox record : responseList)
                        labelsList.add("PillName : " + record.getMedicineName() + '\n' + "PillDosage : " + record.getDosage() + '\n' + "PillPotency : " + record.getPotency() + '\n' + "PillNotes : " + record.getNotes());
                }

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
        request.setUserId(userResponse.getUserId());
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

    public void removePill(String medicineName) {
        System.out.print(medicineName);
        String url = "/SilverSnug/PillBox/deletePill?userId=" + userResponse.getUserId() +"&medicineName="+medicineName;
        PillBoxRequest request = new PillBoxRequest();
        try{
            JSONObject jsonObject = new JSONObject(gson.toJson(request));
            System.out.print(jsonObject);
            restApiClient.executePostAPI(getApplicationContext(), url, jsonObject, new APICallback() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    PillBoxResponse response = gson.fromJson(jsonResponse.toString(), PillBoxResponse.class);
                    System.out.print(response);
                    Log.i("PillBoxActivity", response.toString());
                    Toast.makeText(getApplicationContext(), "Pill deleted successfully!", Toast.LENGTH_LONG).show();
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



    public void editPill(View view){

        Intent intent = new Intent(PillBoxActivity.this, EditPill.class);
        intent.putExtra("userResponse", userResponse);
        startActivity(intent);

}


}








