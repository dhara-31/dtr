package com.test.testing12345.adsclass;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.testing12345.adsclass.StylishFontCkModel;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class StoreageCkPref {
    private static final String PREF_NAME = "StoragePref";
    int PRIVATE_MODE = 0;
    Context _context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    String THEME_ID = "THEME_ID";
    String TEMP_THEME_ID = "TEMP_THEME_ID";
    String CUS_TEMP_THEME_ID = "CUS_TEMP_THEME_ID";
    String BITMAP_ID = "BITMAP_ID";
    String WALL_BITMAP_ID = "WALL_BITMAP_ID";
    String TEMP_BITMAP_ID = "TEMP_BITMAP_ID";
    String FILE_PATH = "FILE_PATH";
    String FONT_LST = "FONT_LST";
    String CS_LST = "CS_LST";
    String SHOW_AS_LST = "SHOW_AS_LST";
    String SHOW_CS_LST = "SHOW_CS_LST";
    String SHOW_TS_LST = "SHOW_TS_LST";
    String APP_FIRST = "APP_FIRST";

    public StoreageCkPref(Context context) {
        this._context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        this.pref = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }

    public void setTHEME_ID(int t_id) {

        this.editor.putInt(THEME_ID, t_id);
        this.editor.commit();
     }

    public int getTHEME_ID() {
        return  pref.getInt(THEME_ID, 0);
    }
    public void setTempTHEME_ID(int t_id) {

        this.editor.putInt(TEMP_THEME_ID, t_id);
        this.editor.commit();
    }

    public int getTempTHEME_ID() {
        return  pref.getInt(TEMP_THEME_ID, 0);
    }
    public void setCusTempTHEME_ID(int t_id) {

        this.editor.putInt(CUS_TEMP_THEME_ID, t_id);
        this.editor.commit();
    }

    public int getCusTempTHEME_ID() {
        return  pref.getInt(CUS_TEMP_THEME_ID, 1);
    }
    public void setBitmapS(String encoded) {
        this.editor.putString(BITMAP_ID, encoded);
        this.editor.commit();
    }

    public String getBitmapS() {
        return  pref.getString(BITMAP_ID, null);    }
    public void setWallBitmapS(String encoded) {
        this.editor.putString(WALL_BITMAP_ID, encoded);
        this.editor.commit();
    }

    public String getWallBitmapS() {
        return  pref.getString(WALL_BITMAP_ID, null);    }

    public void setTempBitmapS(String encoded) {
        this.editor.putString(TEMP_BITMAP_ID, encoded);
        this.editor.commit();
    }

    public String getTempBitmapS() {
        return  pref.getString(TEMP_BITMAP_ID, null);    }

    public void setFilePath(File temp_path) {
        this.editor.putString(FILE_PATH, temp_path.getAbsolutePath());
        this.editor.commit();
    }

    public String getFILE_PATH() {
        return  pref.getString(FILE_PATH, null);
    }

    public void setSHOW_AS_LST(boolean b) {
        this.editor.putBoolean(SHOW_AS_LST, b);
        this.editor.commit();
    }

    public boolean getSHOW_AS_LST() {
        return pref.getBoolean(SHOW_AS_LST, false);
    }
    public void setSHOW_TS_LST(boolean b) {
        this.editor.putBoolean(SHOW_TS_LST, b);
        this.editor.commit();
    }

    public boolean getSHOW_TS_LST() {
        return pref.getBoolean(SHOW_TS_LST, false);
    }

    public void setAPP_FIRST(boolean b) {
        this.editor.putBoolean(APP_FIRST, b);
        this.editor.commit();
    }

    public boolean getAPP_FIRST() {
        return pref.getBoolean(APP_FIRST, true);
    }

    public void saveFavorites(ArrayList<StylishFontCkModel> favorites) {



        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FONT_LST, jsonFavorites);

        editor.commit();
    }



    public void removeFavorite(int stylishFontModel) {
        ArrayList<StylishFontCkModel> favorites = getFavorites();
        if (favorites != null) {
             favorites.remove(stylishFontModel);
             saveFavorites(favorites);
        }
    }

    public ArrayList<StylishFontCkModel> getFavorites() {
        ArrayList<StylishFontCkModel> favorites = null;



        if (pref.contains(FONT_LST)) {
            String jsonFavorites = pref.getString(FONT_LST, null);







            favorites= (ArrayList<StylishFontCkModel>) fromJson(jsonFavorites,
                    new TypeToken<ArrayList<StylishFontCkModel>>() {
                    }.getType());
        } else
            return null;

        return (ArrayList<StylishFontCkModel>) favorites;
    }
    public static Object  fromJson(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }
    public void addFavorite(StylishFontCkModel notificationModel) {

        ArrayList<StylishFontCkModel> favorites = getFavorites();
        if (favorites == null)
            favorites = new ArrayList<StylishFontCkModel>();
        favorites.add(notificationModel);
        saveFavorites(favorites);

    }



    public void saveCSList(ArrayList<Integer> cs) {



        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(cs);

        editor.putString(CS_LST, jsonFavorites);

        editor.commit();
    }



    public void removeCsList(Integer pos) {
        ArrayList<Integer> favorites = getCsList();
        if (favorites != null) {
            favorites.remove(pos);
            saveCSList(favorites);
        }
    }

    public ArrayList<Integer> getCsList() {
        ArrayList<Integer> csaa = null;



        if (pref.contains(CS_LST)) {
            String jsonFavorites = pref.getString(CS_LST, null);







            csaa= (ArrayList<Integer>) fromJson1(jsonFavorites,
                    new TypeToken<ArrayList<Integer>>() {
                    }.getType());
        } else
            return null;

        return (ArrayList<Integer>) csaa;
    }
    public static Object  fromJson1(String jsonString, Type type) {
        return new Gson().fromJson(jsonString, type);
    }
    public void addCs(Integer aa) {

        ArrayList<Integer> csll = getCsList();
        if (csll == null)
            csll = new ArrayList<Integer>();
        csll.add(aa);
        saveCSList(csll);

    }
}
