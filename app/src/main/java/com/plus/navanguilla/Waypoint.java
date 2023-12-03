package com.plus.navanguilla;

import com.google.android.gms.maps.model.LatLng;

public class Waypoint {
    private LatLng location;
    private String action;
    private float approachBearing; // The expected bearing when approaching the waypoint
    private float actionRange; // Distance within which the action is applicable

    // Constructor
    public Waypoint(LatLng location, String action, float approachBearing, float actionRange) {
        this.location = location;
        this.action = action;
        this.approachBearing = approachBearing;
        this.actionRange = actionRange;
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
}
