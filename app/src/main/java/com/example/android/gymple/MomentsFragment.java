package com.example.android.gymple;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MomentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter momentAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static ArrayList<Moment> momentsArr;

    public MomentsFragment () {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.moment_recycler_view, container, false);
        momentsArr = new ArrayList<Moment>();
        // Create temp array of moments
        momentsArr.add(new Moment("User 1", "First Workout", "test"));
        momentsArr.add(new Moment("User 1", "Second Workout", "test"));
        momentsArr.add(new Moment("User 2", "First Workout", "test"));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        recyclerView.setHasFixedSize(true);


        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        // use a linear layout manager
//        layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // specify an adapter (see also next example)
        momentAdapter = new MomentsAdapter(momentsArr, getActivity());
        recyclerView.setAdapter(momentAdapter);

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

        //TODO keep recycler view here so that newly submitted moments show up

    }
}