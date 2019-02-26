package edu.sjsu.silversnugmobileapp.utilities;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.sjsu.silversnugmobileapp.R;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.LabelsViewHolder> {
    Context context;

    public static class LabelsViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView product;

        LabelsViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv);
            product = itemView.findViewById(R.id.product);
        }
    }

    List<String> labels;

    public RVAdapter(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public LabelsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_main, viewGroup, false);
        context = viewGroup.getContext();
        LabelsViewHolder pvh = new LabelsViewHolder(v);
        return pvh;
    }


    @Override
    public void onBindViewHolder(LabelsViewHolder labelsViewHolder, int i) {
        labelsViewHolder.product.setText(labels.get(i));
    }

    @Override
    public int getItemCount() {
        return labels.size();
    }

    void deleteItem(int index) {
        labels.remove(index);
        notifyItemRemoved(index);
    }

}


