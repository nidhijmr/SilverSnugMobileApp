package edu.sjsu.silversnugmobileapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.AddressBook;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.PillBox;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.AddressBookResponse;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PillBoxResponse;
import edu.sjsu.silversnugmobileapp.utilities.RVAdapter;

public class PillBoxActivity extends AppCompatActivity {

    ListView lvItems;
    ArrayAdapter<String> mAdapter;
    private RestClient restApiClient;
    private List<String> labelsList = new ArrayList<>();
    private PillBoxResponse response;
    private Gson gson;
    private EditText pillEditText;
    private TextView pillTextView;
    ArrayAdapter<String> adapter;
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
        loadPillBoxList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadPillBoxList();
    }

    public void loadPillBoxList() {
        String url = "/SilverSnug/PillBox/getPill?userId=" + "680cdb82-c044-4dd1-ae84-1a15e54ab502";
        restApiClient.executeGetAPI(getApplicationContext(), url, new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                PillBoxResponse response = gson.fromJson(jsonResponse.toString(), PillBoxResponse.class);
                Log.i("PillBoxActivity", response.toString());

                List<PillBox> responseList = response.getPillBoxes();
                for (PillBox record : responseList)
                    labelsList.add(record.getMedicineName());

                final RVAdapter adapter = new RVAdapter(labelsList);
                rv.setAdapter(adapter);

            }

            @Override
            public void onError(String message) {

                Log.i("PillBoxActivity", message);
            }
    });
        }


    }

