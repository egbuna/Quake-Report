package com.example.android.quakereport;

/**
 * Created by RAYNOLD on 5/13/2017.
 */

public class Quake {
    private double mMagnitude;

    private String mLocation;

    private long mDate;

    private String mUrl;

    public Quake(double magnitude, String location, long date, String url) {
        mMagnitude = magnitude;
        mLocation = location;
        mDate = date;
        mUrl = url;
    }

    public double getMagnitude() {
        return mMagnitude;
    }

    public String getLocation() {
        return mLocation;
    }
    public long getDate() {
        return mDate;
    }
    public String getUrl() {
        return mUrl;
    }

}
