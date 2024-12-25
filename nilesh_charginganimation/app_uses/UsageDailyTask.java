package com.si_charginganimation.nilesh_charginganimation.app_uses;

import android.annotation.SuppressLint;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.AsyncTask;


import com.si_charginganimation.nilesh_charginganimation.model.AppUsageAU;
import com.si_charginganimation.nilesh_charginganimation.model.UtilsAU;
import com.si_charginganimation.nilesh_charginganimation.nilbetanim.DBettryplication;

import java.util.ArrayList;
import java.util.HashMap;


public class UsageDailyTask extends AsyncTask<Void, Void, UsageDaily> {
    private Context mContext;
    private OnLoadUsageDaily onLoadUsageDaily;


    public interface OnLoadUsageDaily {
        void onLoadFinish(UsageDaily usageDaily);
    }

    public UsageDailyTask(Context context, OnLoadUsageDaily onLoadUsageDaily) {
        this.mContext = context;
        this.onLoadUsageDaily = onLoadUsageDaily;
    }
     public UsageDaily getUsageDaily() {
        @SuppressLint("WrongConstant") UsageStatsManager usageStatsManager = (UsageStatsManager) this.mContext.getSystemService(Context.USAGE_STATS_SERVICE);
        HashMap hashMap = new HashMap();
        UsageDaily usageDaily = new UsageDaily();
        usageDaily.setTotalTime(0);
        usageDaily.setTimeEnd(0);
        usageDaily.setTimeStart(0);
        usageDaily.setAppUsages(hashMap);
        if (usageStatsManager != null) {
            UsageEvents queryEvents = usageStatsManager.queryEvents(UtilsAU.getZeroClockTimestamp(System.currentTimeMillis()), System.currentTimeMillis());
            UsageEvents.Event event = new UsageEvents.Event();
            String str = "android";
            while (queryEvents.hasNextEvent()) {
                queryEvents.getNextEvent(event);
                if (event.getEventType() == 1) {
                    if (usageDaily.getTimeStart() == 0) {
                        usageDaily.setTimeStart(event.getTimeStamp());
                    }
                    AppUsageAU appUsage = (AppUsageAU) hashMap.get(event.getPackageName());
                    if (appUsage == null) {
                        appUsage = new AppUsageAU();
                        appUsage.setPackageName(event.getPackageName());
                        appUsage.setTotalTime(0);
                        appUsage.setBeginTime(event.getTimeStamp());
                        appUsage.setLastTimeForeGround(event.getTimeStamp());
                        appUsage.setCountOpen(1);
                    } else {
                        appUsage.setLastTimeForeGround(event.getTimeStamp());
                        if (!str.equals(event.getPackageName())) {
                            str = event.getPackageName();
                            appUsage.countOpenTimeUp();
                        }
                    }
                    hashMap.put(event.getPackageName(), appUsage);
                } else if (event.getEventType() == 2) {
                    usageDaily.setTimeEnd(event.getTimeStamp());
                    AppUsageAU appUsage2 = (AppUsageAU) hashMap.get(event.getPackageName());
                    if (appUsage2 != null) {
                        long timeStamp = event.getTimeStamp() - appUsage2.getLastTimeForeGround();
                        appUsage2.setLastTimeBackGround(event.getTimeStamp());
                        if (!event.getPackageName().equals(UtilsAU.getLauncherPackageName())) {
                            appUsage2.totalTimeUp(timeStamp);
                            usageDaily.totalTimeUp(timeStamp);
                        }
                        hashMap.put(event.getPackageName(), appUsage2);
                    }
                }
            }
        }
        return usageDaily;
    }

    public static ArrayList<UsageStats> getUsageList() {
        long currentTimeMillis = System.currentTimeMillis();
        return EventUtils.getUsageList(DBettryplication.c(), UtilsAU.getZeroClockTimestamp(currentTimeMillis), currentTimeMillis);
    }

     public UsageDaily doInBackground(Void... voidArr) {
        return getUsageDaily();
    }

    public void onPostExecute(UsageDaily usageDaily) {
        super.onPostExecute(usageDaily);
        OnLoadUsageDaily onLoadUsageDaily = this.onLoadUsageDaily;
        if (onLoadUsageDaily != null) {
            onLoadUsageDaily.onLoadFinish(usageDaily);
        }
    }
}
