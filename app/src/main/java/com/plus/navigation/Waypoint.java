package com.plus.navigation;

import com.google.android.gms.maps.model.LatLng;

public class Waypoint {
    private LatLng location;
    private String action;
    private float approachBearing; // The expected bearing when approaching the waypoint
    private float actionRange; // Distance within which the action is applicable
    private boolean isMessageSpoken; // New field to track if the message has been spoken
    // Constructor
    public Waypoint(LatLng location, String action, float approachBearing, float actionRange) {
        this.location = location;
        this.action = action;
        this.approachBearing = approachBearing;
        this.actionRange = actionRange;
        this.isMessageSpoken = false; // Initialize to false
    }

    // Getters
    public LatLng getLocation() {
        return location;
    }

    public String getAction() {
        return action;
    }

    public float getApproachBearing() {
        return approachBearing;
    }

    public float getActionRange() {
        return actionRange;
    }

    public void setMessageSpoken(boolean spoken) {
        isMessageSpoken = spoken;
    }

    public boolean isMessageSpoken() {
        return isMessageSpoken;
    }
}
