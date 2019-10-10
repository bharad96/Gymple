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
    private String pid= "ChIJmRnrx-wP2jERBnqNTg-3Tv0";

    //listOfGymInfo.add(1, "ChIJEwW7gpoP2jER4o0mrMTZddM"); //jurong east clubfitt / activesg
    //listOfGymInfo.add(2, "ChIJWaHnCyAQ2jERPzL-9jFkzWA"); //jurong west clubfitt / activesg
    //listOfGymInfo.add(3, "ChIJmRnrx-wP2jERBnqNTg-3Tv0"); //amore fitness jurong point 2


    public static LatLng position;

    String place_ID, place_Title, place_info;

    private RequestQueue mRequestQueue;
    private AddressResultReceiver mResultReceiver;

    //Declare XML components
    Button revButt;
    TextView address, mName, gymInfo;

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
        gymInfo = (TextView) findViewById(R.id.gym_info);

        GetFacilities();
        HardcodedGymInfoForDemo();

        /*Collections.sort(photos);

        PhotosAdapter photosAdapter = new PhotosAdapter(photos, FullDetail.this);
        RecyclerView photosRecyclerView = findViewById(R.id.recyclerview_photos);
        photosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        photosRecyclerView.setAdapter(photosAdapter);*/

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

    public void HardcodedGymInfoForDemo()
    {
        switch(pid)
        {
            case "ChIJmRnrx-wP2jERBnqNTg-3Tv0": //amore fitness
                gymInfo.setText("Amore Fitness is the leading fitness gym in Singapore providing unique state-of-the-art gym equipment specially designed for women. With imported cardiovascular and strength training equipment from Technogym (Italy) and Precor (USA), ladies have a wide selection when planning their workouts.\n\n" +
                        "The functional training zone is also equipped with fitness accessories such as: Agility ladder, battle rope, kettlebell, swiss ball, TRX® Suspension Trainer, OMNIA⁸, QUEENAX.\n\n" +
                        "Amore Fitness is the first gym in Asia to feature the Selection Pro series with Unity Mini which displays exercise demonstrations as a follow guide. Newbies just starting their fitness journey will have little to worry about using the various functions of these new equipment. Our friendly floor trainers will also gladly provide assistance when needed.");
                break;
            case "ChIJEwW7gpoP2jER4o0mrMTZddM": //jurong east clubfitt / activesg
                gymInfo.setText("Jurong East ActiveSG Gym, formerly known as Jurong East ClubFITT Gym, is a public gym operated by Sport Singapore.\n\nBeing the first one-stop integrated centre, Jurong East Sport Centre, formerly known as Jurong East Sport and Recreation Centre, marks a milestone in Sport Singapore’s facilities development when it opened in 2000. It was the first pool to offer a lazy river, wave pool and fun slides to the masses at an affordable rate.\n\nWith its other facilities like air-conditioned sports hall, stadium, fitness gym, Jurong East Sport Centre prides itself as a preferred venue for community events, tournaments and a leisure day out for all.");
                break;
            case "ChIJWaHnCyAQ2jERPzL-9jFkzWA": //jurong west clubfitt / activesg
                gymInfo.setText("Jurong West ActiveSG Gym, formerly known as Jurong West ClubFITT Gym, is a public gym operated by Sport Singapore. As the 3rd integrated facility with pool features, Jurong West Sport Centre, formerly known as Jurong West Sport and Recreation Centre, has raised the benchmark for all swimming pools in 2006. \n\n" +
                        "With close proximity to the Pioneer MRT station, it has been able to position itself to be a sport and leisure venue. More than just being a choice venue for sports activities, Jurong West Sport Centre offers a range of food and beverage outlets with ample sheltered parking lots. It is the largest integrated sports centre in Singapore.");
                break;

            default: gymInfo.setText("No information provided.");
        }
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
