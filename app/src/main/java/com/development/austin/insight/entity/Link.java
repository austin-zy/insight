package com.development.austin.insight.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ARL on 9/3/2016.
 */
public class Link extends RealmObject {
    @PrimaryKey
    private int id;

    private String linkId;
    private String node_ref;
    private String node_point;
    private double distance;

    public Link(){}

    public Link(String node_ref, String node_point, double distance) {
        setLinkId(node_ref+"-"+node_point);
        this.node_ref = node_ref;
        this.node_point = node_point;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String link_id) {
        this.linkId = link_id;
    }

    public String getNode_ref() {
        return node_ref;
    }

    public void setNode_ref(String node_ref) {
        this.node_ref = node_ref;
    }

    public String getNode_point() {
        return node_point;
    }

    public void setNode_point(String node_point) {
        this.node_point = node_point;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
