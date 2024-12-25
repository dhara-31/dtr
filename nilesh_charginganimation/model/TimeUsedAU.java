package com.si_charginganimation.nilesh_charginganimation.model;

import android.graphics.drawable.Drawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;


public class TimeUsedAU {
    private String appName;
    private Drawable iconApp;
    private String packageName;
    private long totalTime;

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public long getTotalTime() {
        return this.totalTime;
    }

    public void setTotalTime(long j) {
        this.totalTime = j;
    }

    public Drawable getIconApp() {
        return this.iconApp;
    }

    public void setIconApp(Drawable drawable) {
        this.iconApp = drawable;
    }

    public String toString() {
        return "ApplicationUsedTime{packageName='" + this.packageName + "', appName='" + this.appName + "', totalTime='" + this.totalTime + "'}";
    }
    public static Comparator<TimeUsedAU> ListLM = new Comparator<TimeUsedAU>() {


        @Override
        public int compare(TimeUsedAU songModel, TimeUsedAU t1) {




            return  Long.compare(t1.getTotalTime(), songModel.getTotalTime());


        }
    };

}

