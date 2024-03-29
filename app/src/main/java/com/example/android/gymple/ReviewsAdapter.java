package com.example.android.gymple;
/**
 * The ReviewsAdapter is a controller class that manges review objects
 * @author  Hisyam J
 * @version 1.0, 15 Nov 2019
 *
 */
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.RelativeLayout;

import com.example.android.gymple.Reviews;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import com.example.android.gymple.R;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<Reviews> mExampleList;

    /**
     * Reviews constructor
     * @param context context from previous screen
     * @param exampleList list of  binded elements to view
     */
    public ReviewsAdapter(Context context, ArrayList<Reviews> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.review_recycler, parent, false);
        return new ExampleViewHolder(v);
    }

    /**
     * Binding the viewholder for the XML view withe Google reviews from Google places API
     * @param holder view holder of all the UI elements
     * @param position position of the current item in list
     */
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {

        Reviews currentItem = mExampleList.get(position);

        if(getItemCount() > 0) {
            String imageUrl = currentItem.getImageUrl();
            //String creatorName = currentItem.getCreator();
            //int likeCount = currentItem.getLikeCount();
            String creatorName = currentItem.getAname();
            String comm = currentItem.getText();
            String rate = currentItem.getRating();
            String time = currentItem.getrTime();
            String acname = currentItem.getcName();


            holder.mTextViewCreator.setText(creatorName);
            //holder.mTextViewCreator.setVisibility(View.GONE);
            holder.commentView.setText(comm);
            holder.rateme.setRating(Integer.parseInt(rate));
            //holder.mRate.setText("Rating: " + rate);
            holder.mTime.setText(time);

            //holder.mTextViewLikes.setText("Likes: " + likeCount);
            Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
        }
        else
        {
            holder.mRel.setVisibility(View.GONE);
            holder.mNorev.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Counts the total number of items in the list (total no of images)
     * @return total image count
     */
    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    /**
     * Detclaring the different uI elements in the view
     */
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewCreator, commentView, mRate, mTime, mNorev;
        public TextView mTextViewLikes;
        public RatingBar rateme;
        public Toolbar mBar;
        public RelativeLayout mRel;


        /**
         * Initializing the views from XML view
         * @param itemView
         */
        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
            commentView = itemView.findViewById(R.id.comment);
            rateme = itemView.findViewById(R.id.ratingbar);
            //mRate = itemView.findViewById(R.id.ratingbar);
            mTime = itemView.findViewById(R.id.reltime);
            mRel = itemView.findViewById(R.id.revLay);
            mNorev = itemView.findViewById(R.id.norevs);
            //mBar = itemView.findViewById(R.id.toolbar);

            //mTextViewLikes = itemView.findViewById(R.id.text_view_likes);
        }
    }
}




