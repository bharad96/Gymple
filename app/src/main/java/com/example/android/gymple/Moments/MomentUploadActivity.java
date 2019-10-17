package com.example.android.gymple.Moments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.gymple.DetailsFragment;
import com.example.android.gymple.R;
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
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class MomentUploadActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private User user;
    private String photoPath, placeID;
    private ProgressDialog progressDialog;
    private ImageView photoImageView;
    EditText momentDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_upload);

        Button selectPhoto = findViewById(R.id.selectPhotoButton);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        Button uploadMoment = findViewById(R.id.uploadMomentButton);
        uploadMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });

        photoImageView = findViewById(R.id.userImage);
        momentDescription = findViewById(R.id.userInputEditText);

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

        Glide.with(this)
                .load(photoPath)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(photoImageView);
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
                            placeID = DetailsFragment.getPlaceName();
                            photoPath = result.get(0).getPath();
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
    // TODO add a progressDialog
    private void uploadPhoto() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(placeID + "_" + System.currentTimeMillis() + ".jpg");
        UploadTask uploadTask = ref.putFile(Uri.fromFile(new File(photoPath)));

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
                    Uri downloadLinkUri = task.getResult();
                    Log.d("URL for photo : ", downloadLinkUri.toString());
                    uploadMomentToDatabase(downloadLinkUri.toString());
                } else {

                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.hide();
                        Toasty.error(MomentUploadActivity.this, "An error occured, please try again").show();
                        Log.e("Rating", e.getMessage() + "");
                    }
                });
    }

    private void uploadMomentToDatabase(String url){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User user = new SessionManager(this).getUser();

        // Create a new user with a first, middle, and last name
        Map<String, Object> moment = new HashMap<>();
        moment.put("userName", user.getFirstName() + " " + user.getLastName());
        moment.put("userPhoto", url);
        moment.put("userMomentDescription", momentDescription.getText().toString());

// Add a new document with a generated ID
        db.collection("Moments")
                .add(moment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // TODO add a progressDialog
                        Log.d("Moment Upload Activity", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toasty.success(MomentUploadActivity.this, "Moment uploaded successfully!").show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Moment Upload Activity", "Error adding document", e);
                    }
                });
    }
}
