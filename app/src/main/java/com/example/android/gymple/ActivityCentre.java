package com.example.android.gymple;

import com.google.android.gms.maps.model.LatLng;

public class ActivityCentre implements Comparable<ActivityCentre> {

    private String name;
    private String desc;
    private LatLng coordinates;
    private String street_name;
    private double distance;
    private String postalcode;

    public String getPostalcode() {return postalcode;}

    public void setPostalcode(String postalcode) {this.postalcode = postalcode;}

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ActivityCentre() {

    }
    public ActivityCentre(String coord){
        String[] a = coord.substring(1,(coord.length()-1)).split(",");
        coordinates = new LatLng(Double.parseDouble(a[1]),Double.parseDouble(a[0]));

    }

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
