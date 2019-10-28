package com.example.android.gymple;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.IntentService;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.gymple.PlaceDetails.PlaceDetails;
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
import com.example.android.gymple.Photo;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import android.content.Intent;

import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class DetailsFragment extends Fragment implements OnMapReadyCallback
{
    private static String API_KEY = "AIzaSyClj6wAO7n_wMSAxu9bs947OUGkw9Kc2mk";
    private String pid = null;

    public static LatLng position;
    public static String place_Title;
    String place_info, postal_Code, placeName, temp_address, facilities;

    private RequestQueue mRequestQueue;
    private DetailsFragment.AddressResultReceiver mResultReceiver;

    //region Declare XML components
    Button revButt;
    ImageButton shareButton, actionbarShareButton;
    TextView address, mName, gymInfo;
    Toolbar toolbar;
    //endregion

    private ArrayList<Photo> mPhotoList;
    private RecyclerView pRecyclerView;
    private PhotosAdapter pExampleAdapter;

    TextView[] textViews;
    TextView hoursTextView, openCloseTextView, mondayTextView, tuesdayTextView, wednesdayTextView, thursdayTextView, fridayTextView, saturdayTextView, sundayTextView;

    PlaceDetails placeDetails;
    String temp_address_no_format, share_opening_hours;

    String[] opHours;
    final ArrayList<String> openingHours = new ArrayList<>();

    private ActionBarDrawerToggle actionBarDrawerToggle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.view_full_details,
                container, false);

        setHasOptionsMenu(true);

        //region OnClick Back Button
        ImageButton backButton = (ImageButton) view.findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        //endregion

        //region idk for what ah
        // Initialize Places.
        Places.initialize(getActivity().getApplicationContext(), API_KEY);
        //endregion

        //region Get activity centre's values and details, update values
        position = getActivity().getIntent().getExtras().getParcelable("latLon_values");
        place_Title = getActivity().getIntent().getExtras().getString("place_Title");
        place_info = getActivity().getIntent().getExtras().getString("place_info");
        Log.d("placeinfo", place_info);
        postal_Code = getActivity().getIntent().getExtras().getString("postal_code");
        //endregion

        //region Calls to get address from lat/lon
        mResultReceiver = new DetailsFragment.AddressResultReceiver(null);
        startIntentService();
        //endregion

        //region Interactive map in description page
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //endregion

        //region findViewByID-s from XML
        revButt = (Button) view.findViewById(R.id.revbut);
        address = (TextView) view.findViewById(R.id.gym_address);
        mName = (TextView) view.findViewById(R.id.gym_title);
        gymInfo = (TextView) view.findViewById(R.id.gym_info);
        shareButton = (ImageButton) view.findViewById(R.id.share_button);
        actionbarShareButton = (ImageButton) view.findViewById(R.id.actionbar_share_button);
        //endregion

        mRequestQueue = Volley.newRequestQueue(getActivity());
        getPlaceID(place_Title, postal_Code);

        //region Operating hours, setting variables
        LinearLayout arrowImageView = view.findViewById(R.id.linearlayout_opening_hours_trigger);
        final LinearLayout openingHoursLL = view.findViewById(R.id.linearlayout_opening_hours);

        mondayTextView = view.findViewById(R.id.textview_monday_timing);
        tuesdayTextView = view.findViewById(R.id.textview_tuesday_timing);
        wednesdayTextView = view.findViewById(R.id.textview_wednesday_timing);
        thursdayTextView = view.findViewById(R.id.textview_thursday_timing);
        fridayTextView = view.findViewById(R.id.textview_friday_timing);
        saturdayTextView = view.findViewById(R.id.textview_saturday_timing);
        sundayTextView = view.findViewById(R.id.textview_sunday_timing);
        openCloseTextView = view.findViewById(R.id.textview_open_close);
        hoursTextView = view.findViewById(R.id.textview_hours);

        textViews = new TextView[7];
        textViews[0] = mondayTextView;
        textViews[1] = tuesdayTextView;
        textViews[2] = wednesdayTextView;
        textViews[3] = thursdayTextView;
        textViews[4] = fridayTextView;
        textViews[5] = saturdayTextView;
        textViews[6] = sundayTextView;

        //endregion

        //region Toggle click to expand / hide daily operating hours
        arrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openingHoursLL.getVisibility() == View.VISIBLE)
                    openingHoursLL.setVisibility(View.GONE);
                else
                    openingHoursLL.setVisibility(View.VISIBLE);
            }
        });
        //endregion

        //region Set onclick button event to link to reviews page
        revButt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getActivity().getApplicationContext(), ReviewActivity.class);
                i.putExtra("place_name", place_Title);
                i.putExtra("postal", postal_Code);
                startActivity(i);
            }
        });
        //endregion

        //region Set onclick button to pop-up share
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "GYMPLE");

                String message = "\n" + placeName +"\n\n" +
                        "Facilities: " + facilities + "\n" +
                        "Address: " + temp_address_no_format + "\n\n" +
                        "Opening hours: \n" +
                        opHours[0] + opHours[1] + opHours[2] + opHours[3] + opHours[4] + opHours[5] + opHours[6] + "\n\n";

                i.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(i, "Share this via"));
            }
        });
        //endregion

        //region Volley google photos
        pRecyclerView = view.findViewById(R.id.recycler_view_photos);
        pRecyclerView.setHasFixedSize(true);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        pRecyclerView.setLayoutManager(horizontalLayoutManagaer);

        mPhotoList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(getActivity());
        //endregion

        return view ;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event

        if(item.getItemId()== R.id.actionbar_share_button)
        {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "GYMPLE");

            String message = "\n" + placeName +"\n\n" +
                    "Facilities: " + facilities + "\n" +
                    "Address: " + temp_address_no_format + "\n\n" +
                    "Opening hours: \n" +
                    opHours[0] + opHours[1] + opHours[2] + opHours[3] + opHours[4] + opHours[5] + opHours[6] + "\n\n";

            i.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(i, "Share this via"));
        }
        else if(item.getItemId() == R.id.actionbar_back_button)
        {
            getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        // you can add listener of elements here
          /*Button mButton = (Button) view.findViewById(R.id.button);
            mButton.setOnClickListener(this); */
    }

    private void UpdateGymTitle(String placeTitle)
    {
        String placetitleqwerty = placeTitle + "";
        Log.d("placetitleqwerty", placetitleqwerty);

        if (placeTitle.length() > 25) {
            placeTitle = placeTitle.substring(0, 24);
            placeTitle = placeTitle.concat("..");
        }

        mName.setText(placeTitle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {   super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(place_Title);

        //set name of place based on KML data
        UpdateGymTitle(place_Title);
        GetFacilities(place_info);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.full_desc_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getPlaceID(String placetitle, String postalCode)
    {
        //Clean string
        placetitle = placetitle.replaceAll(" ", "");
        placetitle = placetitle.replaceAll("-", "");

        //Splitting String
        String[] unameD1 = placetitle.split(" ");
        String aggString = "";

        int arlength = unameD1.length;
        for (int i = 0; i < arlength; i++) {
            aggString = aggString + "%20" + unameD1[i];
        }

        String url = "https://maps.googleapis.com/maps/api/geocode/json?&address=" + postalCode + aggString + "&key=" + API_KEY;
        Log.d("url2", url);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("results");
                    if (jsonArray.length() > 0) {
                        JSONObject first = (JSONObject) jsonArray.get(0);
                        Log.d("mpid", first.toString());
                        String plid = first.getString("place_id");
                        Log.d("getstring", plid);
                        parseJSON(plid);
                        pid = plid;
                        HardcodedGymInfoForDemo(plid);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("error", "error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(req);
    }

    private void parseJSON(String placeID) {

        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&key=" + API_KEY;
        Log.d("urlme", url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("result");

                    //region Format address, GetOperatingHours, Set Address
                    //region Address formatting
                    //mName.setText(jsonObject.getString("name"));

                    temp_address_no_format = jsonObject.get("formatted_address").toString();

                    String temp_address1 = temp_address_no_format.substring(0, temp_address_no_format.indexOf(", Singapore")+2);
                    String temp_address2 = temp_address_no_format.substring(temp_address_no_format.indexOf(", Singapore")+2);

                    temp_address = temp_address1 + System.getProperty("line.separator") + temp_address2;
                    //endregion

                    placeName = jsonObject.getString("name");
                    GetOperatingHours(placeName);

                    address.setText(temp_address_no_format);
                    //endregion

                    //region Getting PHOTO array elements
                    JSONArray jsonArray = jsonObject.getJSONArray("photos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String height = object.getString("height");
                        String photoref = object.getString("photo_reference");
                        String upref = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=" + photoref + "&key=" + API_KEY;

                        mPhotoList.add(new Photo(upref));

                        //Photos API reference
                        //https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=YOUR_API_KEY
                    }

                    pExampleAdapter = new PhotosAdapter(getActivity(), mPhotoList);
                    pRecyclerView.setAdapter(pExampleAdapter);
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

    public void HardcodedGymInfoForDemo(String placeID)
    {
        if(placeID != null) {
            switch (placeID) {
                case "ChIJmRnrx-wP2jERBnqNTg-3Tv0": //amore fitness
                    gymInfo.setText("Amore Fitness is the leading fitness gym in Singapore providing unique state-of-the-art gym equipment specially designed for women. With imported cardiovascular and strength training equipment from Technogym (Italy) and Precor (USA), ladies have a wide selection when planning their workouts.\n\n" +
                            "The functional training zone is also equipped with fitness accessories such as: Agility ladder, battle rope, kettlebell, swiss ball, TRX® Suspension Trainer, OMNIA⁸, QUEENAX.\n\n" +
                            "Amore Fitness is the first gym in Asia to feature the Selection Pro series with Unity Mini which displays exercise demonstrations as a follow guide. Newbies just starting their fitness journey will have little to worry about using the various functions of these new equipment. Our friendly floor trainers will also gladly provide assistance when needed.");
                    break;
                case "ChIJWaHnCyAQ2jERPzL-9jFkzWA": //jurong east clubfitt / activesg
                    gymInfo.setText("Jurong East ActiveSG Gym, formerly known as Jurong East ClubFITT Gym, is a public gym operated by Sport Singapore.\n\nBeing the first one-stop integrated centre, Jurong East Sport Centre, formerly known as Jurong East Sport and Recreation Centre, marks a milestone in Sport Singapore’s facilities development when it opened in 2000. It was the first pool to offer a lazy river, wave pool and fun slides to the masses at an affordable rate.\n\nWith its other facilities like air-conditioned sports hall, stadium, fitness gym, Jurong East Sport Centre prides itself as a preferred venue for community events, tournaments and a leisure day out for all.");
                    break;
                case "ChIJEwW7gpoP2jER4o0mrMTZddM": //jurong west clubfitt / activesg
                    gymInfo.setText("Jurong West ActiveSG Gym, formerly known as Jurong West ClubFITT Gym, is a public gym operated by Sport Singapore. As the 3rd integrated facility with pool features, Jurong West Sport Centre, formerly known as Jurong West Sport and Recreation Centre, has raised the benchmark for all swimming pools in 2006. \n\n" +
                            "With close proximity to the Pioneer MRT station, it has been able to position itself to be a sport and leisure venue. More than just being a choice venue for sports activities, Jurong West Sport Centre offers a range of food and beverage outlets with ample sheltered parking lots. It is the largest integrated sports centre in Singapore.");
                    break;

                default:
                    gymInfo.setText("No information provided.");
                    break;
            }
        }
        else
            gymInfo.setText("Null placeID.");
    }

    //region Opening hours
    public void GetOperatingHours(String placeTitle)
    {placeTitle = placeTitle.replaceAll(" ", "%20");
        placeTitle = placeTitle.replaceAll("'", "%27");
        Log.d("placetitle", placeTitle);
        Log.d("placetitle2", placeName);

        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?key=" + API_KEY + "&input=" + placeTitle + "&inputtype=textquery&fields=place_id";
        Log.d("urlop", url);

        final RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String placeID = "";

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray candidates = jsonObject.getJSONArray("candidates");

                    if (candidates.length() > 0)
                    {
                        placeID = candidates.getJSONObject(0).getString("place_id").toString();
                    }

                    if (!placeID.equals(""))
                    {
                        String url = "https://maps.googleapis.com/maps/api/place/details/json?key=" + API_KEY + "&placeid=" + placeID + "&fields=opening_hours";
                        Log.d("urlop2", url);
                        pid = placeID;

                        final StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                placeDetails = gson.fromJson(response, PlaceDetails.class);

                                if(placeDetails.getResult().getOpeningHours() != null)
                                {
                                    openingHours.addAll(placeDetails.getResult().getOpeningHours().getWeekdayText());

                                    //region For Sharing Info
                                    share_opening_hours = openingHours.toString();
                                    share_opening_hours = share_opening_hours.substring(1, share_opening_hours.length()-1);

                                    opHours = share_opening_hours.split(", ");
                                    //endregion

                                    Calendar calendar = Calendar.getInstance();
                                    int day = calendar.get(Calendar.DAY_OF_WEEK); //1 to 7, sun/mon/tue/wed/thu/fri/sat

                                    for(int j = 0; j<opHours.length; j++)
                                    {
                                        opHours[j] = opHours[j] + "\n";
                                    }

                                    for (int i=0; i < openingHours.size(); i++)
                                    {
                                        String[] splitString = openingHours.get(i).split(" ", 2);

                                        textViews[i].setText(splitString[1]);

                                        if(i+2 == day)
                                        {
                                            hoursTextView.setText(splitString[1]);
                                            String temp = i + ", " + day;
                                            Log.d("i, day", temp);
                                        }
                                        else if(i==6) //sat in arraylist
                                        {
                                            if(day==1) //sat in days
                                            {
                                                hoursTextView.setText(splitString[1]);
                                                String temp = i + ", " + day;
                                                Log.d("i, day", temp);
                                            }
                                        }
                                    }

                                    if (placeDetails.getResult().getOpeningHours().getOpenNow())
                                    {
                                        openCloseTextView.setTextColor(Color.GREEN);
                                        openCloseTextView.setText("OPEN NOW");
                                    }
                                    else
                                    {
                                        openCloseTextView.setTextColor(Color.RED);
                                        openCloseTextView.setText("CLOSED");
                                    }
                                }
                                else
                                {
                                    opHours = new String[7];
                                    opHours[0] = "Operating hours not available \n";

                                    for(int j = 0; j < 6; j++)
                                    {
                                        opHours[j] = "";
                                    }

                                    for (int i = 0; i < 7; i++) {
                                        textViews[i].setText("Operating hours not available");
                                    }

                                    hoursTextView.setText("Operating hours not available");
                                    openCloseTextView.setText("");
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley", "An error occurred");
                            }
                        });

                        mRequestQueue.add(stringRequest2);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley2", "An error occurred");
            }
        });

        mRequestQueue.add(stringRequest);
    }
    //endregion

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
        Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        getActivity().startService(intent);
    }

    private void displayAddressOutput(final String addressText){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //address.setText(addressText);
                TextView gymAddress = (TextView) getActivity().findViewById(R.id.gym_address);
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
        /*if( position!= null && position!= null) {
            return position.latitude;
        }*/
        return 0;
    }

    public double getLongitude()
    {
        /*if( position!= null && position!= null) {
            return position.longitude;
        }*/
        return 0;
    }
    //endregion
    //region Get Facilities
    public void GetFacilities(String placeInfo)
    {
        if(placeInfo.contains("Facilities:") || placeInfo.contains("facilities:"))
        {
            facilities =  placeInfo.substring(12, placeInfo.indexOf("Operating")-1);
        }
        else
        {
            facilities = "Facilities provided are not specified.";
        }

        TextView facility = (TextView) getActivity().findViewById(R.id.facilities);
        Log.d("placeinfo2", place_info);
        facility.setText(facilities);
    }

    //endregion

}