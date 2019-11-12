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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

/**
 * Activity where the user is able to upload his/her moments which include a text description and an optional picture.
 * @author  Akarapu Bharadwaj
 * @version 1.0, 11 Nov 2019
 */
public class MomentUploadActivity extends AppCompatActivity {

    private String photoPath;
    private ProgressDialog progressDialog;
    private ImageView photoImageView;
    EditText momentDescription;
    private String placeName;
    private boolean imageSelected = false;
    private int descriptionLength;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_upload);

        Button selectPhoto = findViewById(R.id.selectPhotoButton);
        /**
         * Open the gallery when the select photo button is pressed
         */
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        Button uploadMoment = findViewById(R.id.uploadMomentButton);

        /**
         * Upload the moment when the upload moment button is pressed.
         * There is a check is make sure that the description length is between 5 and 200 characters long.
         * If there is an image selected then the image is first uploaded online to firebase else the url is set to Null
         */
        uploadMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descriptionLength = momentDescription.getText().toString().length();
                if(descriptionLength >= 5 && descriptionLength <= 200) {
                    progressDialog.show();
                    if(imageSelected) {
                        uploadPhoto();
                    }
                    else{
                        uploadMomentToDatabase("Null");
                    }
                }
                else {
                    if(descriptionLength < 5) {
                        Toasty.error(MomentUploadActivity.this, "Description too short, please try again!").show();
                    }
                    else{
                        Toasty.error(MomentUploadActivity.this, "Description too long, please try again!").show();
                    }
                }
            }
        });

        photoImageView = findViewById(R.id.userImage);
        momentDescription = findViewById(R.id.userInputEditText);



        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Uploading...");
        progressDialog.setCancelable(false);

    }

    @Override
    protected void onResume() {
        super.onResume();

        placeName = DetailsFragment.place_Title;
//        sessionManager = new SessionManager(this);
//
//        if(sessionManager.isLoggedIn()) {
//            user = sessionManager.getUser();
//            Log.e("User",  "" + user.getEmailID());
//        } else {
//            startActivity(new Intent(MomentUploadActivity.this, LoginActivity.class));
//        }

        Glide.with(this)
                .load(photoPath)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .into(photoImageView);
    }

    /**
     * This function opens the android gallery for the user to select the photo. The user may choose to take a picture in here.
     */
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
                    photoPath = result.get(0).getPath();
                    imageSelected = true;
                }
            })
            .onCancel(new Action<String>() {
                @Override
                public void onAction(@NonNull String result) {
                    imageSelected = false;
                }
            })
            .start();
    }

    /**
     * The user's photo is uploaded to firebase storage and the url to the photo is saved and the function upload to database is called with the url passed in
     */
    private void uploadPhoto() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference ref = storageRef.child(placeName + "_" + System.currentTimeMillis() + ".jpg");
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

    /**
     * A moment is created and uploaded into the firebase database.
     * @param url is the url which corresponds to the user's uploaded picture
     */
    private void uploadMomentToDatabase(String url){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        User user = new SessionManager(this).getUser();

        // Create a new user with a first, middle, and last name
        Map<String, Object> moment = new HashMap<>();
        moment.put("userName", user.getFirstName() + " " + user.getLastName());
        moment.put("userPhoto", url);
        moment.put("userMomentDescription", momentDescription.getText().toString());
        moment.put("timeStamp", Calendar.getInstance().getTimeInMillis());

// Add a new document with a generated ID
        db.collection("Moments\\" + placeName)
                .add(moment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressDialog.hide();
                        Log.d("Moment Upload Activity", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toasty.success(MomentUploadActivity.this, "Moment uploaded successfully!").show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Moment Upload Activity", "Error adding document", e);
                        progressDialog.hide();
                    }
                });
    }
}
