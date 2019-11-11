package com.example.android.gymple;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;


public class ListViewController extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RepositoryObserver {
    private Activity activity;
    private ArrayList<ActivityCentre> activityCentreArrayList;
    private TextView textView;
    private BottomSheetBehavior bottomSheetBehavior;
    private Subject mUserDataRepository;

    /**
     * Contructor for creating ListViewController adapter
     * @param activity activty
     * @param activityCentreArrayList  ArrayList of activity centre to for ListViewController to generate list view
     * @param bottomSheetBehavior Android view
     * @param textView Android user interface view
     */
    public ListViewController(Activity activity, ArrayList<ActivityCentre> activityCentreArrayList, TextView textView, BottomSheetBehavior bottomSheetBehavior){
        this.activity=activity;
        this.activityCentreArrayList=activityCentreArrayList;
        this.textView=textView;
        this.bottomSheetBehavior=bottomSheetBehavior;
        mUserDataRepository = ActivityCentreManager.getInstance();
        mUserDataRepository.registerObserver(this);
    }

    @Override
    public RecyclerView.ViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listviewitem, parent, false);

        return new MyViewHolder(view);

    }

    /**
     * Binds the ViewHolder
     * And update Textview and bottomSheetBehavior PeekHeight level according to item count
     * @param myViewHolder ViewHolder a design pattern which can be applied when using a custom adapter.
     * @param position Index of the ArrayList
     */
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

    @Override
    public void onUserDataChanged(ArrayList<ActivityCentre> list) {
        if(activityCentreArrayList!=null) {
            activityCentreArrayList.clear();
            activityCentreArrayList.addAll(list);
        }
        else{
            activityCentreArrayList = list;
        }
        notifyDataSetChanged();
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
            Intent myIntent = new Intent(activity.getApplicationContext(), ViewFullDetails.class);
            myIntent.putExtra("latLon_values", ac.getCoordinates()); //Optional parameters

            myIntent.putExtra("place_ID", "");
            myIntent.putExtra("place_Title", ac.getName());
            myIntent.putExtra("place_info", ac.getDesc());
            myIntent.putExtra("postal_code", ac.getPostalcode());
            activity.startActivity(myIntent);

        }
    }

    /**
     * Return the number of item inside ArrayList
     * And update Textview and bottomSheetBehavior PeekHeight level according to item count
     * @return Item count The number of item inside the ArrayList
     */
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

    /**
     * Update the list view controller by passing in an updated ArrayList
     * @param list ArrayList of activity centre
     */
    public void updateList(ArrayList<ActivityCentre> list){
//        if(activityCentreArrayList!=null) {
//            activityCentreArrayList.clear();
//            activityCentreArrayList.addAll(list);
//        }
//        else{
//            activityCentreArrayList = list;
//        }
//        notifyDataSetChanged();
    }

}
