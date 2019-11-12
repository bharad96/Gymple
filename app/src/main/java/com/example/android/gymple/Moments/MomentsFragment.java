package com.example.android.gymple.Moments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.gymple.DetailsFragment;
import com.example.android.gymple.R;
import com.example.android.gymple.ViewFullDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/**
 * An activity to display the user moments from the current activity centre and an option to upload the moments.
 * @author  Akarapu Bharadwaj
 * @version 1.0, 11 Nov 2019
 */
public class MomentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter momentAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static ArrayList<Moment> momentsArr;
    public String placeName;
    private TextView noMoments;
    private SessionManager sessionManager;
    private User user;

    /**
     * Empty constructor
     */
    public MomentsFragment () {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.moment_recycler_view, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        noMoments = rootView.findViewById(R.id.nomoments);
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        /**
         * Before the user is able to upload moments, the user must first log in with google
         */
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager = new SessionManager(MomentsFragment.this.getContext());

                if(!sessionManager.isLoggedIn()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
                if(sessionManager.isLoggedIn()) {
                    user = sessionManager.getUser();
                    Log.e("User",  "" + user.getEmailID());
                    Intent intent = new Intent(getActivity(), MomentUploadActivity.class);
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        placeName = DetailsFragment.place_Title;
        //keep recycler view here so that newly submitted moments show up
        momentsArr = new ArrayList<Moment>();

        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Log.e("Moments Fragment", "Place Name: " + placeName);
        db.collection("Moments\\" + placeName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("Moments Fragment", document.getId() + " => " + document.getData());
//                                Log.e("Moments Fragment", document.get("userName").toString());
//                                Log.e("Moments Fragment", document.get("userMomentDescription").toString());
//                                Log.e("Moments Fragment", document.get("timestamp").toString());
                                Moment temp = new Moment(document.get("userName").toString(), document.get("userMomentDescription").toString(), document.get("userPhoto").toString(), (Long)document.get("timeStamp"));
                                Log.e("Moments Fragment", "Description : " + temp.getUserMomentDescription());
                                Log.e("Moments Fragment", "Time Stamp : " + temp.getTimestamp());
                                momentsArr.add(temp);

                            }
                            if(!momentsArr.isEmpty()){
                                noMoments.setVisibility(View.INVISIBLE);
                                Log.d("Moments Fragment", "MomentArray is not empty!");
                            }
                            else {
                                Log.d("Moments Fragment", "MomentArray is empty!");
                            }
                            Collections.sort(momentsArr);
                            momentAdapter.notifyDataSetChanged();

                        } else {
                            Log.w("Moments Fragment", "Error getting documents.", task.getException());
                        }
                    }
                });

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true);



        // use a linear layout manager
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        momentAdapter = new MomentsAdapter(momentsArr, getActivity());
        recyclerView.setAdapter(momentAdapter);
    }
}