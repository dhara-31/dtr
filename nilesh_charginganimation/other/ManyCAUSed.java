package com.si_charginganimation.nilesh_charginganimation.other;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ManyCAUSed {
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean isAvailable = false;
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                isAvailable = true;
            }
            return isAvailable;
        } catch (Exception e) {
            return false;
        }

    }

    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E\ndd\nLLL");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();

        return dateTime;
    }

    public static String getDate2() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, dd LLL");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();

        return dateTime;
    }

    public static String getDate3() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE & \nLLL dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        String time = simpleDateFormat2.format(calendar.getTime()).toString();

        return dateTime + " at " + time;
    }
    public static String getDate4() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd LLLL");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();

        return   dateTime;
    }
    @SuppressLint("WrongConstant")
    public static boolean isMyServiceRunning(Class<?> cls,Context context) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager)context.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {

                return true;
            }
        }
        return false;
    }
}
