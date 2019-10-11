package com.example.android.gymple;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import androidx.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class SelectPhotoActivity extends AppCompatActivity {
    public static String photoUrl, placeID;
    private ImageView photoImageView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        photoImageView = findViewById(R.id.imageview_photo);
        Button retakeButton = findViewById(R.id.button_retake);
        ImageView tickImageView = findViewById(R.id.imageview_tick);

        toolbar.setTitle("Upload a Photo");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);

        Glide.with(this)
                .load(photoUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(photoImageView);

        retakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                openGallery();
            }
        });

        tickImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                uploadPhoto();
            }
        });
    }

    private void uploadPhoto()
    {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(placeID + "_" + System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = ref.putFile(Uri.fromFile(new File(photoUrl)));

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    uploadPhotoURL(downloadUri.toString());
                } else {

                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.hide();
                Toasty.error(SelectPhotoActivity.this, "An error occured, please try again").show();
                Log.e("Rating", e.getMessage() + "");
            }
        });
    }

    private void uploadPhotoURL(String url)
    {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        User user = new SessionManager(this).getUser();

        Map<String, Object> photo = new HashMap<>();
        photo.put("placeID", placeID);
        photo.put("url", url);
        photo.put("userID", user.getUserID());
        photo.put("authorName", user.getFirstName() + " " + user.getLastName());
        photo.put("timestamp", Calendar.getInstance().getTimeInMillis());
        photo.put("authorPic", user.getProfileImageUrl());

        firestore.collection("Photos")
                .add(photo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.hide();
                        Toasty.success(SelectPhotoActivity.this, "Photo uploaded successfully").show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.hide();
                        Toasty.error(SelectPhotoActivity.this, "An error occured, please try again").show();
                        Log.e("Rating", e.getMessage() + "");
                    }
                });
    }

    private void openGallery()
    {
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
                        SelectPhotoActivity.photoUrl = result.get(0).getPath();

                        startActivity(new Intent(SelectPhotoActivity.this, SelectPhotoActivity.class));
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    }
                })
                .start();
    }
}
