package com.development.austin.insight.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Austin on 5/29/16.
 */
public class Place extends RealmObject {
    @PrimaryKey
    private int id;

    private String placeId;
    private String placeName;
    private double latitude;
    private double longitude;
    private String building;
    private String block;
    private int floor;
    private String Desc;
    private String tag;
    private Boolean isExit;
    private String owner;
    private Boolean isToilet;
    private String created_at;
    private String updated_at;

    public Place(){}

    public Place(int id, String placeId, String placeName, double latitude, double longitude, String building, String block, int floor, String desc, Boolean isExit, String owner, Boolean isToilet, String tag) {
        this.id = id;
        this.placeId = placeId;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.building = building;
        this.block = block;
        this.floor = floor;
        Desc = desc;
        this.isExit = isExit;
        this.owner = owner;
        this.isToilet = isToilet;
        this.tag = tag;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatted_date = sdf.format(date);
        this.created_at = formatted_date;
    }

    public Place(int id, String placeId, String placeName, double latitude, double longitude, String building, String block, int floor, String desc, Boolean isExit, String owner, Boolean isToilet, String tag, String created_at) {
        this.id = id;
        this.placeId = placeId;
        this.placeName = placeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.building = building;
        this.block = block;
        this.floor = floor;
        Desc = desc;
        this.isExit = isExit;
        this.owner = owner;
        this.isToilet = isToilet;
        this.tag = tag;
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatted_date = sdf.format(date);
        this.created_at = created_at;
        this.updated_at = formatted_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getIsExit() {
        return isExit;
    }

    public void setExit(Boolean exit) {
        isExit = exit;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getIsToilet() {
        return isToilet;
    }

    public void setToilet(Boolean toilet) {
        isToilet = toilet;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
