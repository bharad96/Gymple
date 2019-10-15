package com.example.android.gymple;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.ArrayList;

import com.example.android.gymple.R;
import com.example.android.gymple.Photo;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ExampleViewHolder>{
    private Context mContext;
    private ArrayList<Photo> mPhotoList;


    public PhotosAdapter(Context context, ArrayList<Photo> exampleList) {
        mContext = context;
        mPhotoList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.detail_photos_item, parent, false);
        return new ExampleViewHolder(v);
    }

    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        Photo currentItem = mPhotoList.get(position);

        String imageUrl = currentItem.getmImageUrl();

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.detail_photo_view);

        }
    }
}
