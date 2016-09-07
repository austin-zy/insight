package com.development.austin.insight.entity;

import io.realm.RealmObject;

/**
 * Created by ARL on 9/6/2016.
 */
public class AccessPoint extends RealmObject {
    private int id;
    private String ap_id;
    private double lat;
    private double lng;

    public AccessPoint() {
    }

    public AccessPoint(int id, String ap_id, double lat, double lng) {
        this.id = id;
        this.ap_id = ap_id;
        this.lat = lat;
        this.lng = lng;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAp_id() {
        return ap_id;
    }

    public void setAp_id(String ap_id) {
        this.ap_id = ap_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
