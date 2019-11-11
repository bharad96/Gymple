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
import com.google.android.gms.maps.UiSettings;
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

/**
 * The DetailsFragment class is a boundary class that interacts with the user
 * Each Activity Centre has its own DetailsFragment, generated upon selection in map/list view
 *
 * Shows the following information:
 * Google images, Activity Centre's brief description, Facilities, Location, Opening hours,
 * Map snippet (linked to Google Direction and Google Map), Google reviews
 *
 * @author Jess Tan
 * @version 1.0, 11 Nov 2019
 */

public class DetailsFragment extends Fragment implements OnMapReadyCallback
{
    private static String API_KEY = "AIzaSyClj6wAO7n_wMSAxu9bs947OUGkw9Kc2mk";
    private String pid = null;

    public static LatLng position;
    public static String place_Title;
    String place_info, postal_Code, placeName, temp_address, facilities;

    private RequestQueue mRequestQueue;
    //private DetailsFragment.AddressResultReceiver mResultReceiver;

    //region Declare XML components
    Button revButt;
    TextView address, gymInfo;
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

    /**
     * Android-level function:
     * Retrieves intent from ActivityCentreManager, set up map snippet, get IDs of items in XML,
     * set up toggle click for viewing more operating hours, set up volley of Google photos
     * @param inflater  Android-level: instantiates menu XML files into Menu objects
     * @param container Android-level: special view that can contain other views
     * @param savedInstanceState Android-level bundle object
     * @return
     */

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.view_full_details,
                container, false);

        setHasOptionsMenu(true);


        //region Initialize Places.
        Places.initialize(getActivity().getApplicationContext(), API_KEY);
        //endregion

        //region Get activity centre's values and details, update values
        position = getActivity().getIntent().getExtras().getParcelable("latLon_values");
        place_Title = getActivity().getIntent().getExtras().getString("place_Title");
        postal_Code = getActivity().getIntent().getExtras().getString("postal_code");
        place_info = ActivityCentreManager.getActivitycentreDesc(postal_Code);
        Log.d("placeinfo", place_info);
        //endregion


        //region Interactive map in description page
        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //endregion

        //region findViewByID-s from XML
        revButt = (Button) view.findViewById(R.id.revbut);
        address = (TextView) view.findViewById(R.id.gym_address);
        gymInfo = (TextView) view.findViewById(R.id.gym_info);
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

    /**
     * onOptionsItemSelected determines action to perform when:
     * User clicks share button
     * User clicks back button
     * @param item Android-level: interface for direct access to action bar
     * @retursn super.onOptionsItemSelected(item) value determines if share menu inflated is shown or not
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle
        // If it returns true, then it has handled
        // the nav drawer indicator touch event
        if((place_Title != null || !place_Title.isEmpty()) &&
                (facilities != null || !facilities.isEmpty()) &&
                (temp_address_no_format != null || !temp_address_no_format.isEmpty()) &&
                (opHours.length != 0)) {


            if (item.getItemId() == R.id.actionbar_share_button) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "GYMPLE");

                String message;

                if(opHours[0] == "")
                {
                    message = "\n" + place_Title + "\n\n" +
                            "Facilities: " + facilities + "\n" +
                            "Address: " + temp_address_no_format + "\n\n" +
                            "Opening hours: \nNot available \n\n";
                }
                else
                {
                    message = "\n" + place_Title + "\n\n" +
                            "Facilities: " + facilities + "\n" +
                            "Address: " + temp_address_no_format + "\n\n" +
                            "Opening hours: \n" +
                            opHours[0] + opHours[1] + opHours[2] + opHours[3] + opHours[4] + opHours[5] + opHours[6] + "\n\n";
                }
                i.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(Intent.createChooser(i, "Share this via"));
            } else if (item.getItemId() == android.R.id.home /*|| item.getItemId() == R.id.actionbar_back_button*/) {
                getActivity().finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * onActivityCreated will update the Action Bar with the Activity Centre's name,
     * set up back button, update facilities and brief description of Activity Centre
     * @param savedInstanceState Android-level bundle object
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {   super.onActivityCreated(savedInstanceState);

        getActivity().setTitle(place_Title);

        //Onclick back button
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); //shows back button

        //set name of place based on KML data
        GetFacilities(place_info);
        DIYGymInfo(place_Title);
    }

    /**
     * onCreateOptionsMenu will update action bar with a layout for share button
     * @param menu Android-level: option menu for action bar
     * @param inflater Android-level: instantiates menu XML files into Menu objects
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.full_desc_actionbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * getPlaceID updates the variable 'pid' with the place ID of the Activity Centre.
     * Place ID is retrieved from Google Geocoding API
     * @param placetitle The current Activity Centre's name in the KML data
     * @param postalCode The curent Activity Centre's postal code in the KML data
     */
    private void getPlaceID(String placetitle, String postalCode)
    {
        //Clean string
        placetitle = placetitle.replaceAll(" ", "%20");
        placetitle = placetitle.replaceAll("'", "%27");

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
                        //HardcodedGymInfoForDemo(plid);
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


    /**
     * parseJSON is called from getPlaceID and updates the Activity Centre's operating hours and address from Google.
     * @param placeID place ID of Activity Centre, retrieved from function getPlaceID
     */
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

                    if(jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            String height = object.getString("height");
                            String photoref = object.getString("photo_reference");
                            String upref = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=" + photoref + "&key=" + API_KEY;

                            mPhotoList.add(new Photo(upref));

                            //Photos API reference
                            //https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=YOUR_API_KEY
                        }

                        TextView noPhotos = (TextView) getActivity().findViewById(R.id.nophotos);
                        noPhotos.setVisibility(View.INVISIBLE);

                        pExampleAdapter = new PhotosAdapter(getActivity(), mPhotoList);
                        pRecyclerView.setAdapter(pExampleAdapter);
                    }
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

    /**
     * DIYGymInfo will update the Activity Centre's brief description with a hard-coded paragraph, based on the Activity Centre's name.
     * Each type of Activity Centre will have its own set of paragraph.
     * @param checkPlaceTitle The current Activity Centre's name in the KML data
     */
    public void DIYGymInfo(String checkPlaceTitle)
    {
        String checkPlaceTitle2 = checkPlaceTitle.toLowerCase(); //so don't need to check upper cases in if-loops below
        String gymInfoAppend = "";
        boolean updated = false;

        //loop if-else checking while there is still a WORD in place title. remove word at the end of every if-loop
        if(checkPlaceTitle2.contains("yoga"))
        {
            gymInfoAppend = gymInfoAppend + checkPlaceTitle + " is a yoga studio. Done right, yoga can directly and positively impact your mental health; Clearing your mind of noise, disorder, and negativity. Soft and stretchable clothing is recommended. ";

            if(checkPlaceTitle2.contains("art"))
            {
                gymInfoAppend = gymInfoAppend + "\n\nBeing an art studio as well, " + checkPlaceTitle + " may have activities catered for younger students. Contact " + checkPlaceTitle + " to check on availability. ";
            }

            checkPlaceTitle2 = checkPlaceTitle2.replaceAll("yoga", "");
            updated = true;
        }
        else if (checkPlaceTitle2.contains("swim"))
        {
            gymInfoAppend = gymInfoAppend + checkPlaceTitle + " is a swimming complex that offers a pleasant and safe environment for all swimmers. Leisure swimmers can take a leisure away from the hustle and bustle of the city. " +
                    checkPlaceTitle + " is also popular with schools for swimming lessons. ";

            checkPlaceTitle2 = checkPlaceTitle2.replaceAll("swim", "");
            updated = true;
        }
        else if(checkPlaceTitle2.contains("secondary school") || checkPlaceTitle2.contains("primary school"))
        {
            gymInfoAppend = gymInfoAppend + checkPlaceTitle + " is an educational institute. Fields are available for booking by contacting the school's management office. ";

            checkPlaceTitle2 = checkPlaceTitle2.replaceAll("school", "");
            updated = true;
        }
        else if (checkPlaceTitle2.contains("stadium"))
        {
            gymInfoAppend = gymInfoAppend + checkPlaceTitle + " is a public multi-purpose stadium operated by Sport Singapore. Open to the public, there is no need for booking with the exception of private events. ";

            checkPlaceTitle2 = checkPlaceTitle2.replaceAll("stadium", "");
            updated = true;
        }
        else if (checkPlaceTitle2.contains("sports") && checkPlaceTitle2.contains("centre"))
        {
            gymInfoAppend = gymInfoAppend + checkPlaceTitle + " is a one-stop integrated community sports facility catering to those who do sports and recreational activities; the perfect sports and leisure venue. " +
                    "\n\nWith its other facilities like air-conditioned sport hall, stadium, fitness gym, " + checkPlaceTitle + " prides itself as a preferred venue for community events, tournaments and a leisure day out for all. ";

            checkPlaceTitle2 = checkPlaceTitle2.replaceAll("sports", "");
            checkPlaceTitle2 = checkPlaceTitle2.replaceAll("centre", "");
            updated = true;
        }
        else if (checkPlaceTitle2.contains("amore fitness"))
        {
            gymInfoAppend = gymInfoAppend + "Amore Fitness is the leading fitness gym in Singapore providing unique state-of-the-art gym equipment specially designed for women. With imported cardiovascular and strength training equipment from " +
                    "Technogym (Italy) and Precor (USA), ladies have a wide selection when planning their workouts. \n\nAmore Fitness is the first gym in Asia to feature the Selection Pro series with Unity Mini which displays exercise demonstrations as a follow guide. ";

            checkPlaceTitle2 = checkPlaceTitle2.replaceAll("amore fitness", "");
            updated = true;
        }

        if(updated == false) //all others include 'gym'
        {
            gymInfoAppend = gymInfoAppend + checkPlaceTitle + " is the leading fitness gym in Singapore. Located all around Singapore and equipped with standard fitness facilities and accessories, newbies " +
                "who are just starting their fitness journey will have little to worry about. Friendly floor trainers are available and will gladly provide assistance when needed. ";
        }

        gymInfo.setText(gymInfoAppend);
    }

    /**
     * GetOperatingHours will update the operating hour of each Activity Centre, based on the Activity Centre's name
     * Operating hours is retrieved from Google using Google Places API
     * @param placeTitle The current Activity Centre's name in the KML data
     */
    //region Opening hours
    public void GetOperatingHours(String placeTitle)
    {
        placeTitle = placeTitle.replaceAll(" ", "%20");
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

                                if(placeDetails.getResult() != null)
                                {
                                    if (placeDetails.getResult().getOpeningHours() != null)
                                    {
                                        openingHours.addAll(placeDetails.getResult().getOpeningHours().getWeekdayText());

                                        //region For Sharing Info
                                        share_opening_hours = openingHours.toString();
                                        share_opening_hours = share_opening_hours.substring(1, share_opening_hours.length() - 1);

                                        opHours = share_opening_hours.split(", ");
                                        //endregion

                                        Calendar calendar = Calendar.getInstance();
                                        int day = calendar.get(Calendar.DAY_OF_WEEK); //1 to 7, sun/mon/tue/wed/thu/fri/sat

                                        for (int j = 0; j < opHours.length; j++) {
                                            opHours[j] = opHours[j] + "\n";
                                        }

                                        for (int i = 0; i < openingHours.size(); i++) {
                                            String[] splitString = openingHours.get(i).split(" ", 2);

                                            textViews[i].setText(splitString[1]);

                                            if (i + 2 == day) {
                                                hoursTextView.setText(splitString[1]);
                                                String temp = i + ", " + day;
                                                Log.d("i, day", temp);
                                            } else if (i == 6) //sat in arraylist
                                            {
                                                if (day == 1) //sat in days
                                                {
                                                    hoursTextView.setText(splitString[1]);
                                                    String temp = i + ", " + day;
                                                    Log.d("i, day", temp);
                                                }
                                            }
                                        }

                                        if (placeDetails.getResult().getOpeningHours().getOpenNow()) {
                                            openCloseTextView.setTextColor(Color.GREEN);
                                            openCloseTextView.setText("OPEN NOW");
                                        } else {
                                            openCloseTextView.setTextColor(Color.RED);
                                            openCloseTextView.setText("CLOSED");
                                        }
                                    }
                                    else {
                                        nullOperationalHours();
                                    }
                                }
                                else {
                                    nullOperationalHours();
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

    /**
     * Updatss map snippet of current Activity Centre by adding marker on map.
     * Disables gesture for map such as panning and zooming.
     * @param googleMap Android-level: map object to display google map
     */
    //region Set up interactive map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker based on lat/lon
        // and move the map's camera to the same location.
        LatLng location = new LatLng(position.latitude, position.longitude);
        googleMap.addMarker(new MarkerOptions().position(location)
                .title(place_Title));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        googleMap.getUiSettings().setAllGesturesEnabled(false);
    }
    //endregion

    /**
     * nullOperatingalHours will update each day's operating hours as "Operating hours not available for the current Activity Centre"
     */
    //region No Operational Hours Available on Google
    public void nullOperationalHours()
    {
        opHours = new String[7];
        opHours[0] = "Operating hours not available \n";

        for (int j = 0; j < 6; j++) {
            opHours[j] = "";
        }

        for (int i = 0; i < 7; i++) {
            textViews[i].setText("Operating hours not available");
        }

        hoursTextView.setText("Operating hours not available");
        openCloseTextView.setText("");
    }
    //endregion

    /**
     * GetFacilities will retrieve the facilities of the current Activity Centre from the KML data
     * and update into facilities. If no facilities are available, facilities will be updated
     * as "Facilities provided are not specified."
     * @param placeInfo The current Activity Centre's description based on KML data
     */
    //region Get Facilities
    public void GetFacilities(String placeInfo)
    {
        if(placeInfo.contains("Facilities: ") || placeInfo.contains("facilities: "))
        {
            if(placeInfo.contains("Operating")){
                //start at 12th character (index 0 ~ 11 = "facilities: ")
                facilities =  placeInfo.substring(12, placeInfo.indexOf("Operating")-1);
            }
            else{
                facilities =  placeInfo.substring(placeInfo.indexOf("Facilities")+12);
            }

            //remove empty space in front of text
            char tempC = facilities.charAt(0);

            while(tempC == ' ')
            {
                facilities = facilities.substring(1);
                tempC = facilities.charAt(0);
            }

            //replace double space to single space
            facilities = facilities.trim().replaceAll(" +", " ");

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