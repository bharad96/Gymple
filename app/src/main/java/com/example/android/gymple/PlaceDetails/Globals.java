package com.example.android.gymple.PlaceDetails;

import android.app.Application;

public class Globals extends Application {

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public String placeid;


}
