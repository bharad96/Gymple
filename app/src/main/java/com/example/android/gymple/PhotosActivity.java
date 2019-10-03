package com.example.android.gymple;

import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import com.example.android.gymple.R;
import com.example.android.gymple.PhotosAdapter;
import com.example.android.gymple.StaggeredPhotosAdapter;
import com.example.android.gymple.Photo;


public class PhotosActivity extends AppCompatActivity {
    public static ArrayList<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        Toolbar toolbar = findViewById(R.id.title_tab);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Photos");
        setSupportActionBar(toolbar);

        Collections.sort(photos);

        PhotosAdapter photosAdapter = new PhotosAdapter(photos, PhotosActivity.this);
        RecyclerView photosRecyclerView = findViewById(R.id.recyclerview_photos);
        photosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photosRecyclerView.setAdapter(photosAdapter);
    }
}