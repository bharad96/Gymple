package com.example.android.gymple;
import com.google.android.gms.maps.model.LatLng;


/**
 * The ActivityCentre class represents a Activity Centre objects.
 * @author  Desmond Yeo
 * @version 1.0, 23 Oct 2019
 *
 */
public class ActivityCentre implements Comparable<ActivityCentre> {

    private String name;
    private String desc;
    private LatLng coordinates;
    private String street_name;
    private double distance;
    private String postalcode;

    /**
     * Returns the postal code of the Activity Centre
     * @return postal code
     */
    public String getPostalcode() {return postalcode;}

    /**
     * Set the postal code for Activity Centre
     * @param postalcode The postal code of the Activity Centre
     */
    public void setPostalcode(String postalcode) {this.postalcode = postalcode;}

    /**
     * Returns the distance of the Activity Centre from the user location
     * @return distance in meter
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Set the distance for Activity Centre
     * @param distance The distance of the Activity Centre from the user location
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Returns the coordinates of the Activity Centre.
     * @return Coordinates
     */
    public LatLng getCoordinates() {
        return coordinates;
    }

    /**
     * Set the coordinates for Activity Centre
     * @param coordinates The coordinates of the Activity Centre
     */
    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Returns the street name of the Activity Centre.
     * @return Street Name
     */
    public String getStreet_name() {
        return street_name;
    }

    /**
     * Set the street name for Activity Centre
     * @param street_name The street name of the Activity Centre
     */
    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    /**
     * Returns the Name of the Activity Centre.
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name for Activity Centre
     * @param name The name of the Activity Centre
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the description of the Activity Centre.
     * @return Description
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Set the description for Activity Centre
     * @param desc The description of the Activity Centre
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Default constructor for class ActivityCentre
     */
    public ActivityCentre() {

    }
    /**
     * Another constructor for class ActivityCentre
     * @param coord  Coordinates of the Activity Centre
     */
    public ActivityCentre(String coord){
        String[] a = coord.substring(1,(coord.length()-1)).split(",");
        coordinates = new LatLng(Double.parseDouble(a[1]),Double.parseDouble(a[0]));

    }

    /**
     * Sort the Activity Centre's distance from the user in ascending order
     * @param activityCentre  ActivityCentre object
     */
    @Override
    public int compareTo(ActivityCentre activityCentre) {
        if(activityCentre.getDistance()>this.getDistance())
            return -1;
        else if (activityCentre.getDistance()<this.getDistance()){
            return 1;
        }
        else return 0;
    }
}
