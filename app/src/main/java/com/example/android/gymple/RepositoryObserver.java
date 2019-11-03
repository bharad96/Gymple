package com.example.android.gymple;

import android.location.Location;

import com.example.android.gymple.ActivityCentre;
import java.util.ArrayList;

public interface RepositoryObserver {
    void onUserDataChanged(ArrayList<ActivityCentre> list);
}
