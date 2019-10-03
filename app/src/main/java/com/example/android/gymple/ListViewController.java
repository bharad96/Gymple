package com.example.android.gymple;

import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;


public class ListViewController extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private AppCompatActivity activity;
    private ArrayList<ActivityCentre> activityCentreArrayList;
    private TextView textView;
    private BottomSheetBehavior bottomSheetBehavior;


    public ListViewController(AppCompatActivity activity, ArrayList<ActivityCentre> activityCentreArrayList, TextView textView, BottomSheetBehavior bottomSheetBehavior){
        this.activity=activity;
        this.activityCentreArrayList=activityCentreArrayList;
        this.textView=textView;
        this.bottomSheetBehavior=bottomSheetBehavior;
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listviewitem, parent, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder myViewHolder,final int position) {
        String name = activityCentreArrayList.get(position).getName();
        String address = activityCentreArrayList.get(position).getStreet_name();
        double distance = activityCentreArrayList.get(position).getDistance();
        final MyViewHolder vh = (MyViewHolder) myViewHolder;
        vh.name.setText(name);
        vh.address.setText(address);
        vh.distance.setText(String.format("%.2f", distance)+"m");



    }
    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView address;
        TextView distance;
        public MyViewHolder(View itemView) {
            super(itemView);
            distance = itemView.findViewById(R.id.textview_distance);
            address = itemView.findViewById(R.id.textview_address);
            name = itemView.findViewById(R.id.textview_name);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            ActivityCentre ac = activityCentreArrayList.get(getAdapterPosition());
            Intent myIntent = new Intent(activity.getApplicationContext(), FullDetail.class);
            Intent myIntent2 = new Intent(activity.getApplicationContext(), FetchAddressIntentService.class);
            myIntent.putExtra("latLon_values", ac.getCoordinates()); //Optional parameters
            myIntent.putExtra("place_ID", "");
            myIntent.putExtra("place_Title", ac.getName());
            myIntent.putExtra("place_info", ac.getDesc());
            activity.startActivity(myIntent);

        }
    }
    @Override
    public int getItemCount() {
        if(activityCentreArrayList==null || activityCentreArrayList.size()==0){
            this.textView.setText("No Results Available");
            bottomSheetBehavior.setPeekHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, Resources.getSystem().getDisplayMetrics()));
            return 0;
        }
        else{
            this.textView.setText("Activity Centres Nearby");
            bottomSheetBehavior.setPeekHeight((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, Resources.getSystem().getDisplayMetrics()));
        }
        return activityCentreArrayList.size();
    }

    public void updateList(ArrayList<ActivityCentre> list){
        if(activityCentreArrayList!=null) {
            activityCentreArrayList.clear();
            activityCentreArrayList.addAll(list);
        }
        else{
            activityCentreArrayList = list;
        }
        notifyDataSetChanged();
    }

}
