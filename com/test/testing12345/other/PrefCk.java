package com.test.testing12345.other;

import android.content.Context;
import android.content.SharedPreferences;

 public class PrefCk {
    public static String PREF_FIRST_TIME = "PREF_FIRST_TIME";
    public static boolean PREF_FIRST_TIME_VALUE = false;
    public static String PREF_SELECTED_FONT_INT = "PREF_SELECTED_FONT_INT";
    public static String PREF_SELECTED_FONT_STRING = "PREF_SELECTED_FONT_STRING";
    public static int PREF_VALUE_SELECTED_FONT_INT = 0;
    public static String PREF_VALUE_SELECTED_FONT_STRING = "";
    private static SharedPreferences preferences;

    public static SharedPreferences getSharedPreferences(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), 0);
        return context.getSharedPreferences(context.getPackageName(), 0);
    }

    public static int getSelectedFont() {
        return preferences.getInt(PREF_SELECTED_FONT_INT, PREF_VALUE_SELECTED_FONT_INT);
    }

    public static void setSelectedFont(int i) {
        preferences.edit().putInt(PREF_SELECTED_FONT_INT, i).apply();
    }

    public static String getSelectedFontObject() {
        return preferences.getString(PREF_SELECTED_FONT_STRING, PREF_VALUE_SELECTED_FONT_STRING);
    }

    public static void setSelectedFontObject(String str) {
        preferences.edit().putString(PREF_SELECTED_FONT_STRING, str).apply();
    }

    public static boolean getSelectedFirstTimeStatus() {
        return preferences.getBoolean(PREF_FIRST_TIME, PREF_FIRST_TIME_VALUE);
    }

    public static void setSelectedFirstTimeFont(boolean z) {
        preferences.edit().putBoolean(PREF_FIRST_TIME, z).apply();
    }
}
