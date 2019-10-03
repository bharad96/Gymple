package com.example.android.gymple;

import android.Manifest;
import android.app.IntentService;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpResponse;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import android.content.Intent;

import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FullDetail extends AppCompatActivity implements OnMapReadyCallback {

    private String API_KEY = "AIzaSyBqeCRKy7LyjO2DjDsndB08EmQRgS-GKR4";
    private String pid= "ChIJCTok6ekR2jERnFfFyIKukCo";

    public static LatLng position;

    String place_ID, place_Title, place_info;

    private RequestQueue mRequestQueue;
    private AddressResultReceiver mResultReceiver;

    //Declare XML components
    Button revButt;
    TextView address, mName;

    String temp_address, facilities;

    public static ArrayList<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_full_details);

        //region OnClick Back Button
        ImageButton backButton = (ImageButton)findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //endregion

        //region Set Toolbar Title
        //Toolbar toolbar = findViewById(R.id.title_tab);
        //toolbar.setTitle(place_Title);
        //getSupportActionBar().hide();
        //setSupportActionBar(toolbar);
        //endregion

        //region idk for what ah
        // Initialize Places.
        Places.initialize(getApplicationContext(), API_KEY);

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
        //endregion

        //region Get activity centre's values and details, update values
        position = getIntent().getExtras().getParcelable("latLon_values");
        place_ID = getIntent().getExtras().getString("place_ID");
        place_Title = getIntent().getExtras().getString("place_Title");
        place_info = getIntent().getExtras().getString("place_info");

        //UpdateValues();
        //endregion

        //region Calls to get address from lat/lon
        mResultReceiver = new AddressResultReceiver(null);
        startIntentService();
        //endregion

        //region Interactive map in description page
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //endregion

        revButt = (Button) findViewById(R.id.revbut);
        address = (TextView) findViewById(R.id.gym_address);
        mName = (TextView) findViewById(R.id.gym_title);

        GetFacilities();

        Collections.sort(photos);

        PhotosAdapter photosAdapter = new PhotosAdapter(photos, FullDetail.this);
        RecyclerView photosRecyclerView = findViewById(R.id.recyclerview_photos);
        photosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photosRecyclerView.setAdapter(photosAdapter);

        //region Set onclick button event to link to reviews page
        revButt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), ReviewActivity.class);
                i.putExtra("place_name", place_Title);
                startActivity(i);
            }
        });
        //endregion

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();
    }

    private void parseJSON() {

        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + pid + "&key=" + API_KEY;
        //String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJCTok6ekR2jERnFfFyIKukCo&key=AIzaSyAZYb1aJxvG2HaptGtfhKiN4LZlqMpDmq4" ;
        Log.d("urlme", url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("result");

                    //region address and title
                    temp_address = jsonObject.get("formatted_address").toString();

                    String temp_address1 = temp_address.substring(0, temp_address.indexOf(", Singapore")+2);
                    String temp_address2 = temp_address.substring(temp_address.indexOf(", Singapore")+2);

                    temp_address = temp_address1 + System.getProperty("line.separator") + temp_address2;

                    mName.setText(jsonObject.getString("name"));
                    address.setText(temp_address);
                    //endregion

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

    //region Set up interactive map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker based on lat/lon
        // and move the map's camera to the same location.
        LatLng location = new LatLng(position.latitude, position.longitude);
        googleMap.addMarker(new MarkerOptions().position(location)
                .title("Marker based on location"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
    }
    //endregion

    //region Get address from lat/lon
    private void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        startService(intent);
    }

    private void displayAddressOutput(final String addressText){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //address.setText(addressText);
                TextView gymAddress = (TextView)findViewById(R.id.gym_address);
                gymAddress.setText(addressText);
            }
        });
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            if (resultData == null) {
                return;
            }

            // Display the address string
            // or an error message sent from the intent service.
            String mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            if (mAddressOutput == null) {
                mAddressOutput = "";
            }
            displayAddressOutput(mAddressOutput);
        }
    }

    public double getLatitude()
    {
        return this.position.latitude;
    }

    public double getLongitude()
    {
        return this.position.longitude;
    }
    //endregion

    //region Get Facilities
    public void GetFacilities()
    {
        if(place_info.contains("Facilities:") || place_info.contains("facilities:"))
        {
            facilities =  place_info.substring(12, place_info.indexOf("Operating")-1);
        }
        else
        {
            facilities = "Facilities provided are not specified.";
        }

        TextView facility = (TextView) findViewById(R.id.facilities);
        facility.setText(facilities);
    }
    //endregion
}
