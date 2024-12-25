package com.si_charginganimation.nilesh_charginganimation.model;


public class AppUsageAU {
    private long beginTime;
    private int countNotification;
    private int countOpen;
    private long lastTimeBackGround;
    private long lastTimeForeGround;
    private String packageName;
    private long totalTime;

    public AppUsageAU(String str, long j, int i, int i2, long j2, long j3, long j4) {
        this.packageName = str;
        this.beginTime = j;
        this.countOpen = i;
        this.countNotification = i2;
        this.totalTime = j2;
        this.lastTimeForeGround = j3;
        this.lastTimeBackGround = j4;
    }

    public AppUsageAU() {
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public long getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(long j) {
        this.beginTime = j;
    }

    public int getCountOpen() {
        return this.countOpen;
    }

    public void setCountOpen(int i) {
        this.countOpen = i;
    }

    public int getCountNotification() {
        return this.countNotification;
    }

    public void setCountNotification(int i) {
        this.countNotification = i;
    }

    public long getTotalTime() {
        return this.totalTime;
    }

    public void setTotalTime(long j) {
        this.totalTime = j;
    }

    public long getLastTimeForeGround() {
        return this.lastTimeForeGround;
    }

    public void setLastTimeForeGround(long j) {
        this.lastTimeForeGround = j;
    }

    public long getLastTimeBackGround() {
        return this.lastTimeBackGround;
    }

    public void setLastTimeBackGround(long j) {
        this.lastTimeBackGround = j;
    }

    public void totalTimeUp(long j) {
        this.totalTime += j;
    }

    public void countOpenTimeUp() {
        this.countOpen++;
    }
}
