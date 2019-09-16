package com.example.android.gymple;

import com.google.android.gms.maps.model.LatLng;

public class DataGov {

    private String name;
    private String desc;
    private LatLng coordinates;

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
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
    public DataGov() {

    }
    public DataGov(String coord){
        String[] a = coord.substring(1,(coord.length()-1)).split(",");
        coordinates = new LatLng(Double.parseDouble(a[1]),Double.parseDouble(a[0]));

    }

}
