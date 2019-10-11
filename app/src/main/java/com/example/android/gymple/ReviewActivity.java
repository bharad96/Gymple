package com.example.android.gymple;

import com.example.android.gymple.R;
import com.example.android.gymple.VolleyController;

import android.content.Intent;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.gymple.DetailAdapter;
import com.example.android.gymple.ReviewsAdapter;
import com.example.android.gymple.Details;
import com.example.android.gymple.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ReviewsAdapter mExampleAdapter;
    private ArrayList<Reviews> mExampleList;
    private RequestQueue mRequestQueue;
    private String API_KEY = "AIzaSyBqeCRKy7LyjO2DjDsndB08EmQRgS-GKR4";
    private String pid;
    private String acName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);

        Intent intent = getIntent();
        acName = intent.getStringExtra("place_name");
        pid = intent.getStringExtra("pid");
        Log.d("place name = ", acName);

        //region Set Toolbar Title
        Toolbar toolbar = findViewById(R.id.title_tab);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().hide();

        //endregion

        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle("Reviews @ " + acName);
        }
        else
        {
            Log.d("getsupportactionbar = " , "null");
        }

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mExampleList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }

    private void parseJSON() {
        //String url = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=kitten&image_type=photo&pretty=true";
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + pid + "&key=" + API_KEY;
        Log.d("urlme", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("result");
                    //JSONObject jname = jsonObject.getJSONObject("name");
                    acName = jsonObject.getString("name");
                    JSONArray jsonArray = jsonObject.getJSONArray("reviews");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String creatorName = object.getString("author_name");
                        String imageUrl = object.getString("profile_photo_url");
                        String rating = object.getString("rating");
                        String comm = object.getString("text");
                        String time = object.getString("relative_time_description");

                        //int likeCount = object.getInt("likes");

                        mExampleList.add(new Reviews(imageUrl, creatorName, comm, rating, time, acName));

                        Log.d("result", object.getString("author_name"));
                        Log.d("result2", object.getString("rating"));
                        Log.d("result3", object.getString("profile_photo_url"));
                        Log.d("result4", object.getString("text"));

                    }

                    mExampleAdapter = new ReviewsAdapter(ReviewActivity.this, mExampleList);
                    mRecyclerView.setAdapter(mExampleAdapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
    }


}
