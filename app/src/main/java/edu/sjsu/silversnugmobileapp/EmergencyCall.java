package edu.sjsu.silversnugmobileapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyModel.EmergencyContactNumber;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.EmergencyContactResponse;


public class EmergencyCall extends AppCompatActivity {
    String phoneNumber = "", address, userId;
    String message;
    TelephonyManager manager;
    private RestClient restClient;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        address = getIntent().getExtras().getString("address");
        userId = getIntent().getExtras().getString("userId");
        System.out.println("UserId " + userId + " inside Emergency call");
        message = "Emergency! I may need help!!." +" My location: " + address;

        System.out.println("inside emergency call: on create");

        restClient = new RestClient();
        gson = new Gson();

        getEmergencyContactNumber();


    }

    public void getEmergencyContactNumber()
    {
        String url = "/SilverSnug/FallDetection/getEmergencyContact?userId=" + userId;

        System.out.println("Inside getEmergencyContactNumber method");

        restClient.executeGetAPI(getApplicationContext(), url, new APICallback() {
            @Override
            public void onSuccess(JSONObject jsonResponse) {
                Log.i("EmergencyCall API: ", jsonResponse.toString());
                EmergencyContactResponse response = gson.fromJson(jsonResponse.toString(), EmergencyContactResponse.class);
                System.out.println(response);
                Log.i("EmergencyCall Obj: ", response.toString());

                phoneNumber= response.getEmergencyContactNumber();
                Log.i("phoneNumber: ", phoneNumber);

                if (!phoneNumber.equals("")) {

                    Log.i("TAG", phoneNumber);
                    System.out.println("Phone number " + phoneNumber);
                    System.out.println("dialing emergency contact started");
                    DialEmergency();
                    System.out.println("dialing emergency contact ended");

                }
            }

            @Override
            public void onError(String message) {
                Log.i("EmergencyCall", message);

            }
        });

    }


    public void sendMessage(String message, String phoneNumber) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS failed, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public void DialEmergency() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String concatTel = "tel:" + phoneNumber;

        Intent callIntent1 = new Intent(Intent.ACTION_CALL);
        callIntent1.setData(Uri.parse(concatTel));
        AudioManager audioManager = (AudioManager)this. getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setBluetoothScoOn(false);
        audioManager.setSpeakerphoneOn(true);
        System.out.println("CAll activity – Start");
        startActivity(callIntent1);

        System.out.println("CAll activity – End");
        System.out.println("SMS activity – Start");

        sendMessage(message, phoneNumber);
        System.out.println("SMS activity – end");
        finish();

    }

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("emergency call on stop : packup and go");
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("emergency call backpressed : packup and go");
        finish();
    }

}











