package com.example.android.gymple;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.ViewHolder> {

    private ArrayList<ActivityCentre> activityCentreArrayList;
    Context context;

    public RecyclerViewHorizontalListAdapter(ArrayList<ActivityCentre> activityCentreArrayList, Context context){
        this.activityCentreArrayList= activityCentreArrayList;
        this.context = context;
    }

    @Override
    public RecyclerViewHorizontalListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View catView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_search_cat, parent, false);
        ViewHolder cv = new ViewHolder(catView);
        return cv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHorizontalListAdapter.ViewHolder holder, int position) {
        String a = activityCentreArrayList.get(position).getName();
        Log.e("test",""+a);
        final ViewHolder vh = (ViewHolder) holder;
        vh.checkBox.setText(a);
    }

    @Override
    public int getItemCount() {
        Log.e("test",""+activityCentreArrayList.size());
        return activityCentreArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatCheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            checkBox = view.findViewById(R.id.checkBox);
        }
    }
}
