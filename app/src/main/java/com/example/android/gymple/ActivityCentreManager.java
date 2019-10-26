package com.example.android.gymple;

import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.util.Log;

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
/**
 * The ActivityCentreManager class is a controller class that manages the ActivityCentre class
 * @author  Desmond Yeo
 * @version 1.0, 23 Oct 2019
 *
 */
public class ActivityCentreManager {

    private Context context;
    private String jsonString;
    public static ArrayList<ActivityCentre> activitycentreArrayList = new ArrayList<ActivityCentre>();

    /**
     * Constructor for class ActivityCentreManager
     * <p>The constructor will load the json file from the resource file and set the data for each
     * ActivityCentre object
     * <p/>
     * @param resources The resource file that the application contain
     */
    public ActivityCentreManager(Resources resources) {
        InputStream resourceReader = resources.openRawResource(R.raw.datajson);
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
    /**
     * This method will calculate the distance the user is for each of the activity centre
     * and return an ArrayList of ActivityCentre that is within 5km from the user
     * @return ArrayList<ActivityCentre> An ArrayList of ActivityCentre that is within 5km from the user
     */
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

    /**
     * This method will update the activity centre's distance when the user location changed
     * @param location The current location of the user
     *
     */
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

    /**
     * This method return an ArrayList of activity centre based on user search result
     * @param filterResult The activity type of of an activity centre. eg. Gym, Yoga, Swimming pool
     * @param name The name of an activity centre
     * @return An ArrayList of activity centre which has been filtered based on user search result
     */
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