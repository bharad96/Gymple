package com.example.android.gymple;

import android.content.Context;
import android.content.SharedPreferences;


public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String FIRST_NAME = "FIRST_NAME";
    public static final String LAST_NAME = "LAST_NAME";
    public static final String EMAIL_ID = "EMAIL_ID";
    public static final String USER_ID = "USER_ID";
    public static final String PROFILE_IMG = "PROFILE_IMG";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void setUser(User user){
        editor.putBoolean(LOGIN, true);
        editor.putString(FIRST_NAME, user.getFirstName());
        editor.putString(LAST_NAME, user.getLastName());
        editor.putString(EMAIL_ID, user.getEmailID());
        editor.putString(USER_ID, user.getUserID());
        editor.putString(PROFILE_IMG, user.getProfileImageUrl());
        editor.apply();
    }

    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public User getUser(){
        if (sharedPreferences.getString(USER_ID, null) != null) {
            User user = new User();

            user.setFirstName(sharedPreferences.getString(FIRST_NAME, null));
            user.setLastName(sharedPreferences.getString(LAST_NAME, null));
            user.setEmailID(sharedPreferences.getString(EMAIL_ID, null));
            user.setUserID(sharedPreferences.getString(USER_ID, null));
            user.setProfileImageUrl(sharedPreferences.getString(PROFILE_IMG, null));

            return user;
        }

        return null;
    }

    public boolean logout(){
        editor.clear();
        return editor.commit();
    }
}
