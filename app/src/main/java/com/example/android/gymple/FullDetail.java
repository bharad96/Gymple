package com.example.android.gymple;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class FullDetail extends AppCompatActivity {

    private GoogleMap mMap;
    int AUTOCOMPLETE_REQUEST_CODE = 1; //idk just giving some random value, idek what it does lol help
    private static final String TAG = FullDetail.class.getName();

    LatLng position;
    String place_ID, place_Title, place_Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_full_details);

        //get activity centre's values and details
        position = getIntent().getExtras().getParcelable("latLon_values");
        place_ID = getIntent().getExtras().getString("place_ID");
        place_Title = getIntent().getExtras().getString("place_Title");
        place_Info = getIntent().getExtras().getString("place_Info");

        //toast message just to see what's the lat lng / placeID values
        //String temp = position.toString();
        Toast.makeText(FullDetail.this, place_Title, Toast.LENGTH_LONG).show();

        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyBqeCRKy7LyjO2DjDsndB08EmQRgS-GKR4");

        // Create a new Places client instance.
        //PlacesClient placesClient = Places.createClient(this);

        //autoComplete();
        UpdateValues();
    }

    public void UpdateValues()
    {
        TextView gymTitle = (TextView)findViewById(R.id.gym_title);
        gymTitle.setText(place_Title);
    }

    public void autoComplete() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //idk why need the line below but the function called for it
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
