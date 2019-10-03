package com.example.android.gymple;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import com.example.android.gymple.R;
import com.example.android.gymple.Photo;

public class StaggeredPhotosAdapter extends  RecyclerView.Adapter<StaggeredPhotosAdapter.PhotosViewHolder>{
    private ArrayList<Photo> images;
    Context context;

    public StaggeredPhotosAdapter(ArrayList<Photo> images, Context context){
        this.images = images;
        this.context = context;
    }

    @Override
    public PhotosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.staggered_photo_recycler_view,parent,false);
        return new PhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotosViewHolder holder, final int position) {
        //final Transformation transformation = new RoundedCornersTransformation(8, 0, RoundedCornersTransformation.CornerType.ALL);
        //Picasso.get().load(images.get(position)).transform(transformation).fit().into(holder.imageView);

        Glide.with(context)
                .load(images.get(position).getUrl())
                //.centerInside()
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                .apply(new RequestOptions().centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class PhotosViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public PhotosViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview_photo);
        }
    }
}