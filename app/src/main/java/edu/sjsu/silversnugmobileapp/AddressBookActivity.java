package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.AddressBookResponse;
import edu.sjsu.silversnugmobileapp.utilities.RVAdapter;

public class AddressBookActivity extends AppCompatActivity {

    ListView lvItems;
    ArrayAdapter<String> mAdapter;
    private RestClient restClient;
    private List<String> labelsList = new ArrayList<>();
    private AddressBookResponse response;
    private Gson gson;
    private EditText addressBookEditText;
    private TextView addressBookTextView;
    ArrayAdapter<AddressBook> adapter;
    private RecyclerView rv;

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

        loadAddressBookList();
        getSupportActionBar().setTitle("Address Book");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadAddressBookList();
    }

    public void loadAddressBookList() {
        String url = "/SilverSnug/Address/getAddress?userId=" + "680cdb82-c044-4dd1-ae84-1a15e54ab502";
        restClient.executeGetAPI(getApplicationContext(), url, new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                AddressBookResponse response = gson.fromJson(jsonResponse.toString(), AddressBookResponse.class);
                Log.i("AddressBookActivity", response.toString());

                List<AddressBook> responseList = response.getAddressBooks();
                for (AddressBook record : responseList)
                    labelsList.add(record.getAddressName());

                final RVAdapter adapter = new RVAdapter(labelsList);
                rv.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                Log.i("AddressBookActivity", message);
            }
        });
    }

    public void addAddress() {
    }

    public void removeAddress(String addressName) {
    }

    public void openMaps(View view) {
//        Intent intent = new Intent(AddressBookActivity.this, MapsActivity.class);
//        AddressBookActivity.this.startActivity(intent);
    }
}
