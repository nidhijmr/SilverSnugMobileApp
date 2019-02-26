package edu.sjsu.silversnugmobileapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Home Screen");
    }

    public void goToAddressBook(View view) {
        Intent intent = new Intent(MainActivity.this, AddressBookActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void goToPillBox(View view) {
//        Intent intent = new Intent(MainActivity.this, AddressBookActivity.class);
//        MainActivity.this.startActivity(intent);
    }

    public void goToPhotoAlbum(View view) {
//        Intent intent = new Intent(MainActivity.this, AddressBookActivity.class);
//        MainActivity.this.startActivity(intent);
    }
}
