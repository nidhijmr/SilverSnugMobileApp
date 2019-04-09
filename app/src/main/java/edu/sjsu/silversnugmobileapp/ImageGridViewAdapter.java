package edu.sjsu.silversnugmobileapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import static android.content.Context.AUDIO_SERVICE;


public class ImageGridViewAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<ImageDetails> imageList;
    private int imageWidth;
    private int layout;
    String phoneNumber;

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
               deletePhoto();
               break;
       }

    }

    public void deletePhoto()
    {

    }
}
