package com.example.android.gymple;

import android.Manifest;
import android.app.IntentService;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.HttpResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import android.content.Intent;

import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class FullDetail extends AppCompatActivity {

    private GoogleMap mMap;
    int AUTOCOMPLETE_REQUEST_CODE = 1; //idk just giving some random value, idek what it does lol help

    private static final String TAG = FullDetail.class.getName();
    private GoogleApiClient mGoogleApiClient;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBqeCRKy7LyjO2DjDsndB08EmQRgS-GKR4";

    //static = variables will exist for the entire run of program
    //single copy can be shared across all classes in the package
    public static LatLng position;

    String place_ID, place_Title, place_info;
    String operatingHoursFromGovData, telFromGovData;

    private AddressResultReceiver mResultReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_full_details);

        //get activity centre's values and details
        position = getIntent().getExtras().getParcelable("latLon_values");
        place_ID = getIntent().getExtras().getString("place_ID");
        place_Title = getIntent().getExtras().getString("place_Title");
        place_info = getIntent().getExtras().getString("place_info");


        // Initialize Places.
            Places.initialize(getApplicationContext(), API_KEY);

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        //autoComplete();
        UpdateValues();

        ////////////////////////////////
        mResultReceiver = new AddressResultReceiver(null);
        //getLocation();

        startIntentService();
    }


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

    //region Update values into UI
    public void UpdateValues()
    {
        UpdateGymTitle();
        UpdateOperatingHours();
    }

    public void UpdateGymTitle()
    {
        TextView gymTitle = (TextView)findViewById(R.id.gym_title);
        if(place_Title.length() > 25)
        {
            place_Title = place_Title.substring(0, 24);
            place_Title = place_Title.concat("..");
        }
        gymTitle.setText(place_Title);
    }

    public void UpdateOperatingHours()
    {
        operatingHoursFromGovData = place_info.substring(place_info.indexOf("Operating"), place_info.length());

        if(operatingHoursFromGovData.contains("Tel:"))
        {
            telFromGovData = place_info.substring(place_info.indexOf("Tel: "), place_info.indexOf("Tel: ") + 14);
            operatingHoursFromGovData = operatingHoursFromGovData.replace(telFromGovData, "");

            //Toast.makeText(FullDetail.this, operatingHoursFromGovData, Toast.LENGTH_LONG).show();
        }

        TextView operatingHours = (TextView)findViewById(R.id.operating_Hours);
        operatingHours.setText(operatingHoursFromGovData);
    }
    //endregion

    /*public void autoComplete() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.OPENING_HOURS,
                                                    Place.Field.PHOTO_METADATAS, Place.Field.RATING);

        // Start the autocomplete intent.
        //Intent intent = new Autocomplete.IntentBuilder(
         //       AutocompleteActivityMode.FULLSCREEN, fields)
          //      .build(this);

//        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

        for(int i = 0; i<fields.size(); i++)
        {
            Log.d("fields list", fields.get(i).toString());
        }
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
    }*/
}
