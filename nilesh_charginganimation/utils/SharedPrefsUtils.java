package com.si_charginganimation.nilesh_charginganimation.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public final class SharedPrefsUtils {
    private SharedPrefsUtils() {
    }

    public static String getStringPreference(Context context, String str) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (defaultSharedPreferences != null) {
            return defaultSharedPreferences.getString(str, "null");
        }
        return null;
    }

    public static boolean setStringPreference(Context context, String str, String str2) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (defaultSharedPreferences == null || TextUtils.isEmpty(str)) {
            return false;
        }
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putString(str, str2);
        return edit.commit();
    }

    public static float getFloatPreference(Context context, String str, float f) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences != null ? defaultSharedPreferences.getFloat(str, f) : f;
    }

    public static boolean setFloatPreference(Context context, String str, float f) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (defaultSharedPreferences == null) {
            return false;
        }
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putFloat(str, f);
        return edit.commit();
    }

    public static long getLongPreference(Context context, String str, long j) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences != null ? defaultSharedPreferences.getLong(str, j) : j;
    }

    public static int getIntegerPreference(Context context, String str, int i) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences != null ? defaultSharedPreferences.getInt(str, i) : i;
    }

    public static boolean setIntegerPreference(Context context, String str, int i) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (defaultSharedPreferences == null) {
            return false;
        }
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putInt(str, i);
        return edit.commit();
    }

    public static boolean getBooleanPreference(Context context, String str, boolean z) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return defaultSharedPreferences != null ? defaultSharedPreferences.getBoolean(str, z) : z;
    }

    public static boolean setBooleanPreference(Context context, String str, boolean z) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (defaultSharedPreferences == null) {
            return false;
        }
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putBoolean(str, z);
        return edit.commit();
    }
}
