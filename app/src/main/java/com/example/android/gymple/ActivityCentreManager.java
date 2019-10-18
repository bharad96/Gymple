package com.example.android.gymple;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;

public class ActivityCentreManager {
    Context context;
    private String jsonString;
    public static ArrayList<ActivityCentre> activitycentreArrayList = new ArrayList<ActivityCentre>();

    public ActivityCentreManager(Resources resources, int id) {
        InputStream resourceReader = resources.openRawResource(id);
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e("a", "Unhandled exception while using JSONResourceReader", e);
        } finally {
            try {
                resourceReader.close();
            } catch (Exception e) {
                Log.e("b", "Unhandled exception while using JSONResourceReader", e);
            }
        }

        try {
            jsonString = writer.toString();
            JSONObject jObj = new JSONObject(jsonString);
            //get the list of all lccation
            JSONArray jsonArry = jObj.getJSONArray("features");
            for (int i = 0; i < jsonArry.length(); i++) {
                //get individual location
                JSONObject JSONObject = jsonArry.getJSONObject(i);
                JSONObject a = JSONObject.getJSONObject("properties");
                JSONObject b = JSONObject.getJSONObject("geometry");
                ActivityCentre activitycentre = new ActivityCentre(b.getString("coordinates"));
                activitycentre.setName(a.getString("Name"));
                activitycentre.setDesc(a.getString("description"));
                activitycentre.setPostalcode(a.getString("ADDRESSPOSTALCODE"));
                activitycentre.setStreet_name(a.getString("ADDRESSSTREETNAME"));
                activitycentreArrayList.add(activitycentre);
            }
        } catch (JSONException ex) {
            Log.e("JsonParser Example", "unexpected JSON exception", ex);
        }
    }

    //get nearest location based on current location
    public static ArrayList<ActivityCentre> getNearestCentre() {
        ArrayList<ActivityCentre> nearestCentreList = new ArrayList<ActivityCentre>();
        for (int i = 0; i < ActivityCentreManager.activitycentreArrayList.size(); i++) {
            //Location from Gov Data
            ActivityCentre activitycentre = ActivityCentreManager.activitycentreArrayList.get(i);
            //Calculate distance
            double distanceInMeters = activitycentre.getDistance();
            if (distanceInMeters < 5000 && distanceInMeters != 0) {
                nearestCentreList.add(activitycentre);
            }
        }
        Collections.sort(nearestCentreList);
        return nearestCentreList;
    }

    public static void updateDistance(Location location) {
        Location loc1 = new Location("");
        loc1.setLatitude(location.getLatitude());
        loc1.setLongitude(location.getLongitude());
        for (int i = 0; i < ActivityCentreManager.activitycentreArrayList.size(); i++) {
            //Location from Gov Data
            ActivityCentre activitycentre = ActivityCentreManager.activitycentreArrayList.get(i);
            Location loc2 = new Location("");
            loc2.setLatitude(activitycentre.getCoordinates().latitude);
            loc2.setLongitude(activitycentre.getCoordinates().longitude);
            //Calculate distance
            double distanceInMeters = loc1.distanceTo(loc2);
            activitycentre.setDistance(Math.round(distanceInMeters * 100.0) / 100.0);

        }
    }

    public static ArrayList<ActivityCentre> getFilteredList(String query) {
        ArrayList<ActivityCentre> nearestCentreList = new ArrayList<ActivityCentre>();
        for (int i = 0; i < ActivityCentreManager.activitycentreArrayList.size(); i++) {
            //Location from Gov Data
            ActivityCentre activitycentre = ActivityCentreManager.activitycentreArrayList.get(i);
            double distanceInMeters = activitycentre.getDistance();
            if (distanceInMeters < 5000 && distanceInMeters != 0) {
                if (activitycentre.getName().contains(query))
                    nearestCentreList.add(activitycentre);
            }
        }
        Collections.sort(nearestCentreList);
        return nearestCentreList;
    }

    public static ArrayList<ActivityCentre> getFilterResult(ArrayList<String> filterResult, String name) {
        ArrayList<ActivityCentre> filterAndSearchResult = new ArrayList<ActivityCentre>();
        if(filterResult != null && name != null)
        {
            for (int i=0; i<activitycentreArrayList.size(); i++)
            {
                for (int j=0; j<filterResult.size(); j++)
                {
                    if (activitycentreArrayList.get(i).getDesc().toLowerCase().contains(filterResult.get(j).toLowerCase())
                            && activitycentreArrayList.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                        double distanceInMeters = activitycentreArrayList.get(i).getDistance();
                        if (distanceInMeters < 5000 && distanceInMeters != 0) {
                            filterAndSearchResult.add(activitycentreArrayList.get(i));
                            break;
                        }
                    }
                }
            }
        }
        else if (filterResult == null && name != null)
        {
            for (int i=0; i<activitycentreArrayList.size(); i++)
            {
                if (activitycentreArrayList.get(i).getName().toLowerCase().contains(name.toLowerCase()))
                {
                    double distanceInMeters = activitycentreArrayList.get(i).getDistance();
                    if (distanceInMeters < 5000 && distanceInMeters != 0)
                    {
                        filterAndSearchResult.add(activitycentreArrayList.get(i));
                    }
                }
            }

        }
        else if (filterResult != null && name == null)
        {
            for (int i=0; i<activitycentreArrayList.size(); i++)
            {
                for (int j=0; j<filterResult.size(); j++)
                {
                    if (activitycentreArrayList.get(i).getDesc().toLowerCase().contains(filterResult.get(j).toLowerCase()))
                    {
                        double distanceInMeters = activitycentreArrayList.get(i).getDistance();
                        if (distanceInMeters < 5000 && distanceInMeters != 0) {
                            filterAndSearchResult.add(activitycentreArrayList.get(i));
                            break;
                        }
                    }
                }
            }
        }
        else
        {
            return getNearestCentre();
        }
        Collections.sort(filterAndSearchResult);
        return filterAndSearchResult;
    }
}