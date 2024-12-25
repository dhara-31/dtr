package com.si_charginganimation.nilesh_charginganimation.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.BatteryManager;
import android.util.Log;


import com.si_charginganimation.nilesh_charginganimation.nilbetanim.DBettryplication;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class UtilsAU {
    public static final long DAY_IN_MILLIS = 86400000;
    private static final String MARKET_DETAILS_ID = "market://details?id=";
    private static final String PLAY_STORE_LINK = "https://play.google.com/store/apps/details?id=";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);

    public static int[] progressColor() {
        return new int[]{Color.parseColor("#FF0000"), Color.parseColor("#FF8000"), Color.parseColor("#FFFF00"), Color.parseColor("#BFFF00"), Color.parseColor("#40FF00")};
    }


    public static String getAppName(String str) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = DBettryplication.c().getApplicationContext().getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(str, 0);
        } catch (PackageManager.NameNotFoundException unused) {
            applicationInfo = null;
        }
        if (applicationInfo != null) {
            str = String.valueOf(packageManager.getApplicationLabel(applicationInfo));
        }
        return (String) str;
    }

    public static String convertTimes(int i) {
        int i2 = i / 3600;
        int i3 = (i % 3600) / 60;
        int i4 = i % 60;
        if (i2 == 0 && i4 == 0) {
            return String.format("%2dm", Integer.valueOf(i3));
        }
        if (i3 == 0 && i2 == 0) {
            return String.format("%2ds", Integer.valueOf(i4));
        }
        if (i2 != 0) {
            return String.format("%2dh%2dm", Integer.valueOf(i2), Integer.valueOf(i3));
        }
        return String.format("%2dm", Integer.valueOf(i3));
    }

    public static long getZeroClockTimestamp(long j) {
        long j2 = j - (j % DAY_IN_MILLIS);
        Log.i("Wingbu", " DateTransUtils-getZeroClockTimestamp()  获取当日00:00:00的时间戳,东八区则为早上八点 :" + j2);
        return j2;
    }

    public static String getLauncherPackageName() {
        PackageManager packageManager = DBettryplication.c().getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        @SuppressLint("WrongConstant") ResolveInfo resolveActivity = packageManager.resolveActivity(intent, 65536);
        return resolveActivity != null ? resolveActivity.activityInfo.packageName : "";
    }

    public static double getBatteryCapacity(Context context) {
        try {
            return ((Double) Class.forName("com.android.internal.os.PowerProfile").getMethod("getBatteryCapacity", new Class[0]).invoke(Class.forName("com.android.internal.os.PowerProfile").getConstructor(Context.class).newInstance(context), new Object[0])).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0d;
        }
    }

    public static int getCurrentAmpeCharge() {
        @SuppressLint("WrongConstant") BatteryManager batteryManager = (BatteryManager) DBettryplication.c().getSystemService("batterymanager");
        int intProperty = batteryManager.getIntProperty(2) / 1000;
        return intProperty == 0 ? batteryManager.getIntProperty(2) : intProperty;
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + 0.5f);
    }


}
