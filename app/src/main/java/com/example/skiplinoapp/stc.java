package com.example.skiplinoapp;


public class stc {

    public double lat;
    public String location;
    public double longitude;

    public stc(){

    }

    public stc(double lat, String location,double longitude){
        this.location = location;
        this.lat = lat;
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
