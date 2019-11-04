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
import java.util.ListIterator;

/**
 * The ActivityCentreManager class is a controller class that manages the ActivityCentre class
 * @author  Desmond Yeo
 * @version 1.0, 23 Oct 2019
 *
 */
public class ActivityCentreManager implements Subject {

    private Context context;
    public static ArrayList<ActivityCentre> activitycentreArrayList = new ArrayList<ActivityCentre>();
    private static ArrayList<ActivityCentre> datachangeAL;
    private static ActivityCentreManager INSTANCE = null;
    private static ArrayList<RepositoryObserver> mObservers;
    private static Resources resources;
    /**
     * Constructor for class ActivityCentreManager
     * <p>The constructor will load the json file from the resource file and set the data for each
     * ActivityCentre object
     * <p/>
     * @param resources The resource file that the application contain
     */
    public ActivityCentreManager(Resources resources) {
        mObservers = new ArrayList<>();
        if(activitycentreArrayList!=null)
            activitycentreArrayList.clear();
        this.resources = resources;
        String jsonString;
        String jsonString2;
        String jsonString3;

        InputStream resourceReader = resources.openRawResource(R.raw.datajson);
        InputStream resourceReader2 = resources.openRawResource(R.raw.playsg);
        InputStream resourceReader3 = resources.openRawResource(R.raw.aquaticsg);
        Writer writer = new StringWriter();
        Writer writer2 = new StringWriter();
        Writer writer3 = new StringWriter();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(resourceReader2, "UTF-8"));
            BufferedReader reader3 = new BufferedReader(new InputStreamReader(resourceReader3, "UTF-8"));
            String line = reader.readLine();
            String line2 = reader2.readLine();
            String line3 = reader3.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
            while (line2 != null) {
                writer2.write(line2);
                line2 = reader2.readLine();
            }
            while (line3 != null) {
                writer3.write(line3);
                line3 = reader3.readLine();
            }
        } catch (Exception e) {
            Log.e("a", "Unhandled exception while using JSONResourceReader", e);
        } finally {
            try {
                resourceReader.close();
                resourceReader2.close();
                resourceReader3.close();
            } catch (Exception e) {
                Log.e("b", "Unhandled exception while using JSONResourceReader", e);
            }
        }

        try {
            jsonString = writer.toString();
            jsonString2 = writer2.toString();
            jsonString3 = writer3.toString();
            JSONObject jObj = new JSONObject(jsonString);
            JSONObject jObj2 = new JSONObject(jsonString2);
            JSONObject jObj3 = new JSONObject(jsonString3);
            //get the list of all lccation
            JSONArray jsonArry = jObj.getJSONArray("features");
            JSONArray jsonArry2 = jObj2.getJSONArray("features");
            JSONArray jsonArry3 = jObj3.getJSONArray("features");
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

            for (int i = 0; i < jsonArry2.length(); i++) {
                //get individual location
                JSONObject JSONObject = jsonArry2.getJSONObject(i);
                JSONObject a = JSONObject.getJSONObject("properties");
                JSONObject b = JSONObject.getJSONObject("geometry");
                ActivityCentre activitycentre = new ActivityCentre(b.getString("coordinates"));
                activitycentre.setName(a.getString("Name"));
                activitycentre.setDesc(a.getString("description"));
                activitycentre.setPostalcode(a.getString("ADDRESSPOSTALCODE"));
                activitycentre.setStreet_name(a.getString("ADDRESSSTREETNAME"));
                activitycentreArrayList.add(activitycentre);
            }
            for (int i = 0; i < jsonArry3.length(); i++) {
                //get individual location
                JSONObject JSONObject = jsonArry3.getJSONObject(i);
                JSONObject a = JSONObject.getJSONObject("properties");
                JSONObject b = JSONObject.getJSONObject("geometry");
                ActivityCentre activitycentre = new ActivityCentre(b.getString("coordinates"));
                activitycentre.setName(a.getString("Name"));
                activitycentre.setDesc(a.getString("description"));
                activitycentre.setPostalcode(a.getString("ADDRESSPOSTALCODE"));
                activitycentre.setStreet_name(a.getString("ADDRESSSTREETNAME"));
                activitycentreArrayList.add(activitycentre);
            }


            for (int i = 0; i < ActivityCentreManager.activitycentreArrayList.size(); i++) {
                ActivityCentre activityCentreI = activitycentreArrayList.get(i);
                for (int k = 0; k < ActivityCentreManager.activitycentreArrayList.size(); k++) {
                    ActivityCentre activityCentreK = activitycentreArrayList.get(k);
                    if(i != k && activityCentreI.getPostalcode().equals(activityCentreK.getPostalcode())){
                        activitycentreArrayList.remove(k);
                        k--;
                    }

                }
            }
            int a = -0;

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
        datachangeAL=nearestCentreList;
        notifyObservers();
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

    /**
     * This method return a list of activity centres that contained user search query
     * @param query the partial name of an activity centre
     * @return collections of activity centre which has been filerter based on user search query
     */

//    public static ArrayList<ActivityCentre> getFilteredList(String query) {
//        ArrayList<ActivityCentre> nearestCentreList = new ArrayList<ActivityCentre>();
//        for (int i = 0; i < ActivityCentreManager.activitycentreArrayList.size(); i++) {
//            //Location from Gov Data
//            ActivityCentre activitycentre = ActivityCentreManager.activitycentreArrayList.get(i);
//            double distanceInMeters = activitycentre.getDistance();
//            if (distanceInMeters < 5000 && distanceInMeters != 0) {
//                if (activitycentre.getName().contains(query))
//                    nearestCentreList.add(activitycentre);
//            }
//        }
//        Collections.sort(nearestCentreList);
//        return nearestCentreList;
//    }

    /**
     * This method return an ArrayList of activity centre based on user search input(s)
     * @param filterResult The activity type of an activity centre. eg. Gym, Yoga, Swimming pool
     * @param name The name of an activity centre
     * @return An ArrayList of activity centre which has been filtered based on user search input(s)
     */
    public static ArrayList<ActivityCentre> getFilterResult(ArrayList<String> filterResult, String name) {
        ArrayList<ActivityCentre> filterAndSearchResult = new ArrayList<ActivityCentre>();
        if(filterResult != null && name != null)
        {
            for (int i=0; i<activitycentreArrayList.size(); i++)
            {
                for (int j=0; j<filterResult.size(); j++)
                {
                    if ((activitycentreArrayList.get(i).getDesc().toLowerCase().contains(filterResult.get(j).toLowerCase())
                      || activitycentreArrayList.get(i).getDesc().toLowerCase().contains(filterResult.get(j).toLowerCase()))
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
                    if (activitycentreArrayList.get(i).getDesc().toLowerCase().contains(filterResult.get(j).toLowerCase()) || activitycentreArrayList.get(i).getName().toLowerCase().contains(filterResult.get(j).toLowerCase()))
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
        datachangeAL.clear();
        datachangeAL.addAll(filterAndSearchResult);
        notifyObservers();
        return filterAndSearchResult;
    }

    /**
     * This method return the description for an activity centre based on their postal code
     * @param postal The postal code of an activity centre
     * @return Description of an activity centre
     */
    public static String getActivitycentreDesc(String postal) {
        for (int i = 0; i < activitycentreArrayList.size(); i++){
            if(activitycentreArrayList.get(i).getPostalcode().equals(postal)){
                return activitycentreArrayList.get(i).getDesc();
            }
        }
        return null;
    }

    /**
     * Singleton class that allows only a single instance
     * @return Instances of itself
     */
    public static ActivityCentreManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ActivityCentreManager(resources);
        }
        return INSTANCE;
    }

    /**
     * Register observer class for notification when the data changes
     * @param repositoryObserver The observer class that wish to be notified for when data change
     */
    @Override
    public void registerObserver(RepositoryObserver repositoryObserver) {
        if(!mObservers.contains(repositoryObserver)) {
            mObservers.add(repositoryObserver);
        }
    }

    /**
     * Remove observer class for notification
     * @param repositoryObserver The observer class that wish to be removed for notification
     */
    @Override
    public void removeObserver(RepositoryObserver repositoryObserver) {
        if(mObservers.contains(repositoryObserver)) {
            mObservers.remove(repositoryObserver);
        }
    }

    /**
     * This method will be called whenever there is a change in data to notify the Observer class(ListviewController)
     */
    public static void notifyObservers() {
        for (RepositoryObserver observer: mObservers) {
            observer.onUserDataChanged(datachangeAL);
        }
    }
}