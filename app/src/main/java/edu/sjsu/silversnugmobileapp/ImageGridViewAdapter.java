package edu.sjsu.silversnugmobileapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class ImageGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<ImageDetails> imageList;
    private int imageWidth;
    private int layout;
    private static final String AWS_BUCKET = "silversnugphotos";

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
        TextView txtname, txtrelation, txtcontactnumber;

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
        holder.txtcontactnumber = (TextView) row.findViewById(R.id.txtContactNumber);
        holder.imageView = (ImageView) row.findViewById(R.id.imageView);
        row.setTag(holder);

        ImageDetails imageDetails = imageList.get(position);

        holder.txtname.setText(imageDetails.getName());
        holder.txtrelation.setText(imageDetails.getRelationship());
        holder.txtcontactnumber.setText(imageDetails.getContactNumber());
        Picasso.get().load(imageDetails.getImagePath()).into(holder.imageView);

        return row;
    }

}
