package com.graphhopper.chilango.data.gps;

import java.io.Serializable;

/**
 * Created by martinwurflein on 11.04.17.
 */

public class GPSPoint implements Serializable{
    public GPSPoint(double lat, double lon, double alt, float accuracy) {
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
        this.accuracy = accuracy;
    }

    public double lat;
    public double lon;
    public double alt;
    public float accuracy;
}
