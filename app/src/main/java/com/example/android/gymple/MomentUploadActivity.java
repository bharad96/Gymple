package com.example.android.gymple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MomentUploadActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment_upload);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        SessionManager sessionManager = new SessionManager(this);

        if(sessionManager.isLoggedIn()) {
            user = sessionManager.getUser();
            Log.e("User",  "" + user.getEmailID());
        } else {
            startActivity(new Intent(MomentUploadActivity.this, LoginActivity.class));
        }
    }
}
