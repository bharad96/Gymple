package com.example.android.gymple.Moments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.gymple.R;

import java.util.ArrayList;

/**
 * An adapter to bind the moments to the recycler view
 * @author  Akarapu Bharadwaj
 * @version 1.0, 11 Nov 2019
 */
public class MomentsAdapter extends RecyclerView.Adapter<MomentsAdapter.MomentsViewHolder> {
    private ArrayList<Moment> momentsArr;
    Context context;


    // Provide a suitable constructor (depends on the kind of dataset)
    public MomentsAdapter(ArrayList<Moment> momentsArr, Context context) {
        this.momentsArr = momentsArr;
        this.context = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MomentsViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView userName, userDescription;
        ImageView userPhoto;

        public MomentsViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.textview_user_name);
            userDescription = (TextView) itemView.findViewById(R.id.textview_user_description);
            userPhoto = (ImageView) itemView.findViewById(R.id.imageview_user_photo);
        }
    }

    @NonNull
    @Override
    public MomentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moment_card, parent, false);
        return new MomentsViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MomentsViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.userDescription.setText(momentsArr.get(position).getUserMomentDescription());
        holder.userName.setText(momentsArr.get(position).getUserName());
//        holder.userPhoto.setImageResource();

        Glide.with(context)
                .load(momentsArr.get(position).getUserPhoto())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerInside()
                .into(holder.userPhoto);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return momentsArr.size();
    }
}