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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MomentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter momentAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static ArrayList<Moment> momentsArr;
    public String placeName;
    private TextView noMoments;

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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(getActivity(), MomentUploadActivity.class);
                startActivity(intent);
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
                                Log.d("Moments Fragment", document.getId() + " => " + document.getData());
                                Log.d("Moments Fragment", document.get("userName").toString());
                                Log.d("Moments Fragment", document.get("userMomentDescription").toString());


                                momentsArr.add(new Moment(document.get("userName").toString(), document.get("userMomentDescription").toString(), document.get("userPhoto").toString()));

                            }
                            momentAdapter.notifyDataSetChanged();
                            if(!momentsArr.isEmpty()){
                                noMoments.setVisibility(View.INVISIBLE);
                                Log.d("Moments Fragment", "MomentArray is not empty! $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                            }
                            else {
                                Log.d("Moments Fragment", "MomentArray is empty! $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                            }
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