package com.example.android.gymple;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static java.lang.Math.round;


public class ListViewController extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity activity;
    private ActivityCentreManager activitycentreManager;

    public ListViewController(Activity activity, ActivityCentreManager activitycentreManager){
        this.activity=activity;
        this.activitycentreManager=activitycentreManager;
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listviewitem, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder myViewHolder,final int position) {
        String name = ActivityCentreManager.getNearestCentre().get(position).getName();
        String address = ActivityCentreManager.getNearestCentre().get(position).getStreet_name();
        double distance = ActivityCentreManager.getNearestCentre().get(position).getDistance();
        final MyViewHolder vh = (MyViewHolder) myViewHolder;
        vh.name.setText(name);
        vh.address.setText(address);
        vh.distance.setText(String.format("%.2f", distance)+"m");

    }
    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView address;
        TextView distance;
        public MyViewHolder(View itemView) {
            super(itemView);
            distance = itemView.findViewById(R.id.textview_distance);
            address = itemView.findViewById(R.id.textview_address);
            name = itemView.findViewById(R.id.textview_name);
        }
    }
    @Override
    public int getItemCount() {
        return ActivityCentreManager.getNearestCentre().size();
    }

}
