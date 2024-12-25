package com.si_charginganimation.nilesh_charginganimation.app_uses;


import com.si_charginganimation.nilesh_charginganimation.model.AppUsageAU;

import java.util.Map;


public class UsageDaily {
    private String appFirst;
    private Map<String, AppUsageAU> appUsages;
    private long timeEnd;
    private long timeStart;
    private long totalTime;

    public String getAppFirst() {
        return this.appFirst;
    }

    public void setAppFirst(String str) {
        this.appFirst = str;
    }

    public long getTimeStart() {
        return this.timeStart;
    }

    public void setTimeStart(long j) {
        this.timeStart = j;
    }

    public long getTimeEnd() {
        return this.timeEnd;
    }

    public void setTimeEnd(long j) {
        this.timeEnd = j;
    }

    public long getTotalTime() {
        return this.totalTime;
    }

    public void setTotalTime(long j) {
        this.totalTime = j;
    }

    public Map<String, AppUsageAU> getAppUsages() {
        return this.appUsages;
    }

    public void setAppUsages(Map<String, AppUsageAU> map) {
        this.appUsages = map;
    }

    public void totalTimeUp(long j) {
        this.totalTime += j;
    }
}
