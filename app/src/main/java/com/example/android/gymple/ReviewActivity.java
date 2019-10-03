package com.example.android.gymple;

import android.content.Intent;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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
    private String pid= "ChIJCTok6ekR2jERnFfFyIKukCo", acName="Gymboxx Keat Hong CC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);

        Intent intent = getIntent();
        String acname = intent.getStringExtra("PNAME");
        getSupportActionBar().setTitle("REVIEWS @ " + acname);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        mExampleList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }

    private void parseJSON() {
        //String url = "https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=kitten&image_type=photo&pretty=true";
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + pid + "&key=" + getResources().getString(R.string.API_KEY);
        //String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJCTok6ekR2jERnFfFyIKukCo&key=AIzaSyAZYb1aJxvG2HaptGtfhKiN4LZlqMpDmq4" ;
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
