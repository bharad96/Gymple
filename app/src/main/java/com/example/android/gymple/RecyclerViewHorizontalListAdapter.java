package com.example.android.gymple;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.ViewHolder> {
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private ArrayList<String> filterVal;
    Context context;

    public RecyclerViewHorizontalListAdapter(ArrayList<String> filterVal , Context context){
        this.filterVal= filterVal;
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
        String a = filterVal.get(position);
        final ViewHolder vh = (ViewHolder) holder;
        vh.textview.setText(a);
    }

    @Override
    public int getItemCount() {
         return filterVal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textview;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            textview = view.findViewById(R.id.searchTextview);
        }
        @Override
        public void onClick(View view) {
            if (selectedItems.get(getAdapterPosition(), false)) {
                selectedItems.delete(getAdapterPosition());
                view.setSelected(false);
            }
            else {
                selectedItems.put(getAdapterPosition(), true);
                view.setSelected(true);
            }
        }
    }
    public SparseBooleanArray getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(SparseBooleanArray selectedItems) {
        this.selectedItems = selectedItems;
    }


}
