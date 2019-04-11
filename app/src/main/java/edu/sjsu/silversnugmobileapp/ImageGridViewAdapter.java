package edu.sjsu.silversnugmobileapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.APICallback;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyClient.RestClient;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyRequest.PhotoGalleryRequest;
import edu.sjsu.silversnugmobileapp.VolleyAPI.VolleyResponse.PhotoGalleryResponse;

import static android.content.Context.AUDIO_SERVICE;


public class ImageGridViewAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<ImageDetails> imageList;
    private int imageWidth;
    private int layout;
    String phoneNumber, photoName;

    private RestClient restApiClient;
    private Gson gson;


    public ImageGridViewAdapter(Context c, int layout, ArrayList<ImageDetails> imgList) {
        this.mContext = c;
        this.layout = layout;
        this.imageList = imgList;
    }

    public int getCount() {
        return imageList.size();
    }

    public Object getItem(int position) {
        return imageList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtname, txtrelation; // txtcontactnumber;
        Button callbutton, deletephotobutton;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            row = new View(mContext);
        } else {
            row = convertView;
        }

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(layout, null);

        holder.txtname = (TextView) row.findViewById(R.id.txtName);
        holder.txtrelation = (TextView) row.findViewById(R.id.txtRelation);
       // holder.txtcontactnumber = (TextView) row.findViewById(R.id.txtContactNumber);
        holder.callbutton=(Button) row.findViewById(R.id.button_call);
        holder.deletephotobutton= (Button) row.findViewById(R.id.button_delete);

        holder.callbutton.setOnClickListener((View.OnClickListener) this);
        holder.deletephotobutton.setOnClickListener((View.OnClickListener) this);

        holder.imageView = (ImageView) row.findViewById(R.id.imageView);
        row.setTag(holder);

        ImageDetails imageDetails = imageList.get(position);

        holder.txtname.setText(imageDetails.getName());
        photoName= imageDetails.getName();
        holder.txtrelation.setText(imageDetails.getRelationship());
       // holder.txtcontactnumber.setText(imageDetails.getContactNumber());
        phoneNumber=imageDetails.getContactNumber();
        System.out.println("imagePath "+ imageDetails.getImagePath());
        Picasso.get().load(imageDetails.getImagePath()).into(holder.imageView);

        return row;
    }

    @Override
    public void onClick(View view) {

       switch(view.getId()) {
           case R.id.button_call:

               String concatTel = "tel:" + phoneNumber;
               AudioManager audioManager = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);

               if (!audioManager.isSpeakerphoneOn()) {
                   audioManager.setMode(AudioManager.MODE_IN_CALL);
                   //audioManager.setSpeakerphoneOn(true);
               }

               Intent callIntent = new Intent(Intent.ACTION_CALL);
               callIntent.setData(Uri.parse(concatTel));

               if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                   // TODO: Consider calling
                   //    ActivityCompat#requestPermissions
                   // here to request the missing permissions, and then overriding
                   //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                   //                                          int[] grantResults)
                   // to handle the case where the user grants the permission. See the documentation
                   // for ActivityCompat#requestPermissions for more details.
                   return;
               }
               mContext.startActivity(callIntent);
               break;

           case R.id.button_delete:
               AlertDialog dialog =  new AlertDialog.Builder(view.getContext())
                       .setTitle("Delete Photo")
                       .setMessage("Are you sure you want to delete this Photo?")
                       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                             //  deletePhoto(photoName);
                           }
                       })
                       // A null listener allows the button to dismiss the dialog and take no further action.
                       .setNegativeButton(android.R.string.no, null)
                       .setIcon(android.R.drawable.ic_dialog_alert)
                       .show();
       }
}

  /*  public void deletePhoto(String photoName) {

        restApiClient = new RestClient();
        gson = new Gson();

        String url = "/SilverSnug/PhotoGallery/deletePhoto?userId=" + "680cdb82-c044-4dd1-ae84-1a15e54ab502" +"&photoName="+photoName;
        PhotoGalleryRequest request = new PhotoGalleryRequest();
        try{
            JSONObject jsonObject = new JSONObject(gson.toJson(request));
            restApiClient.executePostAPI(mContext, url, jsonObject, new APICallback() {
                @Override
                public void onSuccess(JSONObject jsonResponse) {
                    PhotoGalleryResponse response = gson.fromJson(jsonResponse.toString(), PhotoGalleryResponse.class);
                    Log.i("PhotoGalleryActivity", response.toString());
                    Toast.makeText(mContext, "Photo deleted successfully!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String message) {
                    Log.i("PhotoGalleryActivity", message);
                }
            });

        } catch (JSONException e) {
            String err = (e.getMessage()==null)?"Pill deletion failed":e.getMessage();
            Log.e("PhotoGalleryActivity", err);
        }
    }*/
}
