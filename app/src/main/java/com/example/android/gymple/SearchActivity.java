package com.example.android.gymple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.example.android.gymple.MainActivity.filterArrayList;
import static com.example.android.gymple.MainActivity.query;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private Activity activity;
    private RecyclerView recyclerView;
    private RecyclerViewHorizontalListAdapter searchAdapter;
    private ArrayList<String> filterVal;
    private AppCompatButton searchBtn;
    private ArrayList<String> filterResult;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        activity=this;
        getSupportFragmentManager()
                .beginTransaction()
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        filterVal = new ArrayList<String>();
        filterVal.add("Gym");
        filterVal.add("Yoga");
        filterVal.add("Track & field");
        filterVal.add("Swimming");
        filterVal.add("Pool");
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        //
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        searchAdapter = new RecyclerViewHorizontalListAdapter(filterVal, getApplicationContext());
        recyclerView = findViewById(R.id.searchRV);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(searchAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) item.getActionView();
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                submitResult();
                return true;
            }


        });



        return true;
    }

    @Override
    public void onClick(View view) {
        submitResult();
    }

    private void submitResult(){
        SparseBooleanArray sparseBooleanArray = searchAdapter.getSelectedItems();

        filterResult = new ArrayList<String>();
        for(int i = 0;i<searchAdapter.getItemCount();i++){
            if(sparseBooleanArray.get(i)){
                filterResult.add(filterVal.get(i));
            }
        }

        if(filterResult.isEmpty() || filterResult.size()==0){
            filterArrayList=null;
        }
        else
            filterArrayList=filterResult;


        //Log.e("filtered text",""+filterArrayList.get(0));
        query = "" + searchView.getQuery();
        if(query==""){
            query=null;
        }

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}