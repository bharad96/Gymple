package com.example.android.gymple.Moments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.gymple.R;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

public class MomentUploadActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_upload);

        Button uploadButton = findViewById(R.id.uploadPhoto);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        sessionManager = new SessionManager(this);

        if(sessionManager.isLoggedIn()) {
            user = sessionManager.getUser();
            Log.e("User",  "" + user.getEmailID());
        } else {
            startActivity(new Intent(MomentUploadActivity.this, LoginActivity.class));
        }
    }

    private void openGallery()
    {
        if (sessionManager.getUser() != null) {
            Album.initialize(AlbumConfig.newBuilder(this)
                    .setAlbumLoader(new MediaLoader())
                    .build());

            Album.image(this)
                    .multipleChoice()
                    .camera(true)
                    .selectCount(1)
                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            // ToDo add a proper placeID
                            SelectPhotoActivity.placeID = "Change Later";
//                            SelectPhotoActivity.placeID = detail.getPlaceID();
                            SelectPhotoActivity.photoUrl = result.get(0).getPath();
                            startActivity(new Intent(MomentUploadActivity.this, SelectPhotoActivity.class));
                        }
                    })
                    .onCancel(new Action<String>() {
                        @Override
                        public void onAction(@NonNull String result) {
                        }
                    })
                    .start();
        }
        else
        {
            startActivity(new Intent(MomentUploadActivity.this, LoginActivity.class));
        }
    }
}
