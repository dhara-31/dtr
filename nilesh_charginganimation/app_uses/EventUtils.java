package com.si_charginganimation.nilesh_charginganimation.app_uses;

import android.annotation.SuppressLint;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.Map;


public class EventUtils {
    @SuppressLint("WrongConstant")
    public static ArrayList<UsageStats> getUsageList(Context context, long j, long j2) {
        ArrayList<UsageStats> arrayList = new ArrayList<>();
        for (Map.Entry<String, UsageStats> entry : ((UsageStatsManager) context.getSystemService("usagestats")).queryAndAggregateUsageStats(j, j2).entrySet()) {
            UsageStats value = entry.getValue();
            if (value.getTotalTimeInForeground() > 0) {
                arrayList.add(value);
            }
        }
        return arrayList;
    }
}
