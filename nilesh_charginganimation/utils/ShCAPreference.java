package com.si_charginganimation.nilesh_charginganimation.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShCAPreference {

    public static final String APP_PREFS_NAME = "CCCCCAAAA";

    private SharedPreferences caPreference;
    private SharedPreferences.Editor caPrefEditor;
    Context context;

    String sound_ca = "sound";
    String duration_ca = "duration";
    String closed_ca = "closed";
    String lockScreen_ca = "lockScreen";
    String percentage_ca = "percentage";
    String service_ca = "service";

    String lottie_position_ca = "lottie_position";
    String lottie = "lottie";
    String screen_type_ca = "screen_type";
    String videoUri_ca = "video_uri";
    String Image_ca = "Image";
    String lastPer_ca = "last_per";

    String type_ca = "type";
    String shapeType_ca = "shapeType";
    String fontStyle_ca = "fontStyle";
    String clockPos_ca = "clockPos";
    String clockColor_ca = "clockColor";
    String amplitude_ca = "amplitude";
    String borderWidth_ca = "borderWidth";
    String colorBorder_ca = "colorBorder";
    String waveColor_ca = "waveColor";
    String bgColor_ca = "bgColor";


    String ptFilepath_ca = "ptFilepath";
    String ptBarColor_ca = "ptBarColor";
    String ptWaveColor_ca = "ptWaveColor";
    String ptTextColor_ca = "ptTextColor";
    String ptTagText_ca = "ptTagText";
    String ptOrTagText_ca = "ptOrTagText";
    String ptTagFont_ca = "ptTagFont";

    String ctFilepath_ca = "cptFilepath";
    String ctBarColor_ca = "cptBarColor";
    String ctWaveColor_ca = "cptWaveColor";
    String ctTextColor_ca = "cptTextColor";
    String ctTagText_ca = "cptTagText";
    String ctTagFont_ca = "cptTagFont";
    String ctIconColor_ca = "ctIconColor";


    String atFilepath_ca = "atFilepath";
    String atWaveColor_ca = "atWaveColor";
    String atTextColor_ca = "atTextColor";
    String atTagText_ca = "atTagText";
    String atTagFont_ca = "atTagFont";

    String animList_ca = "animList";


    String animFontStyle_ca = "animFontStyle";
    String animFontColor_ca = "animFontColor";

    String bubbleFilepath_ca = "bubbleFilepath";
    String bubbleWaveColor_ca = "bubbleWaveColor";


    String fullAlarm_ca = "fullAlarm";
    String fullAlarmLevel_ca = "fullAlarmLevel";
    String fullAlarmPos_ca = "fullAlarmPos";
    String fullAlarmRing_ca = "fullAlarmRing";

    String lowAlarm_ca = "lowAlarm";
    String lowAlarmLevel_ca = "lowAlarmLevel";
    String lowAlarmPos_ca = "lowAlarmPos";
    String lowAlarmRing_ca = "lowAlarmRing";

    String tempAlarm_ca = "tempAlarm";
    String tempAlarmLevel_ca = "tempAlarmLevel";
    String tempAlarmPos_ca = "tempAlarmPos";
    String tempAlarmRing_ca = "tempAlarmRing";


    String atEdited_ca = "atEdited";
    String ctEdited_ca = "ctEdited";
    String ptEdited_ca = "ptEdited";

    String ntTheme_ca ="ntTheme";
    String ntwColor_ca ="nwColor";
    String ntFont_ca = "ntFont";
    String ntColorType_ca = "ntColorType";

    String OwlFont_ca = "OwlFont";



    String theme_ca ="theme";
    private String WBS_ca ="wbs";


    public ShCAPreference(Context context) {
        this.caPreference = context.getSharedPreferences(APP_PREFS_NAME, Context.MODE_PRIVATE);
        this.caPrefEditor = caPreference.edit();
        this.context = context;
    }

    public void setSound_ca(boolean b) {


        caPrefEditor.putBoolean(sound_ca, b);
        caPrefEditor.commit();

    }

    public boolean getOnOffSound() {


        return caPreference.getBoolean(sound_ca, false);

    }

    public void setType_ca(String t) {
        caPrefEditor.putString(type_ca, t);
        caPrefEditor.commit();

    }

    public String getType_ca() {
        return caPreference.getString(type_ca, "cs_anim");
    }

    public int getfuid() {
        return caPreference.getInt("v", 0);
    }

    public void setfulid(int duration1) {
        caPrefEditor.putInt("v", duration1);
        caPrefEditor.commit();
    }



    public void setDuration_ca(int duration1) {
        caPrefEditor.putInt(duration_ca, duration1);
        caPrefEditor.commit();
    }

    public int getDuration_ca() {
        return caPreference.getInt(duration_ca, 1);
    }

    public void setClosed_ca(int c) {
        caPrefEditor.putInt(closed_ca, c);
        caPrefEditor.commit();
    }

    public int getClosed_ca() {
        return caPreference.getInt(closed_ca, 1);
    }


    public void setShowLockScreen(boolean ls) {
        caPrefEditor.putBoolean(lockScreen_ca, ls);
        caPrefEditor.commit();
    }

    public boolean getShowLockScreen() {


        return caPreference.getBoolean(lockScreen_ca, false);

    }

    public void setShowPercentage(boolean ls) {
        caPrefEditor.putBoolean(percentage_ca, ls);
        caPrefEditor.commit();
    }

    public boolean getShowPercentage() {


        return caPreference.getBoolean(percentage_ca, true);

    }

    public void setServiceOnOff(boolean ls) {
        caPrefEditor.putBoolean(service_ca, ls);
        caPrefEditor.commit();
    }

    public boolean getServiceOnOff() {


        return caPreference.getBoolean(service_ca, false);

    }


    public void setLottie_position_ca(int l_position) {
        caPrefEditor.putInt(lottie_position_ca, l_position);
        caPrefEditor.commit();
    }

    public int getLottie_position_ca() {
        return caPreference.getInt(lottie_position_ca, 0);
    }


    public void setLastPer_ca(String s_lottie) {
        caPrefEditor.putString(lastPer_ca, s_lottie);
        caPrefEditor.commit();
    }

    public String getLastPer_ca() {
        return caPreference.getString(lastPer_ca, " ");
    }


    public void setVideo(String selectedVideoUri) {
        caPrefEditor.putString(videoUri_ca, selectedVideoUri);
        caPrefEditor.commit();
    }

    public String getVideoUri_ca() {
        return caPreference.getString(videoUri_ca, null);
    }

    public void setImage_ca(String selectedVideoUri) {
        caPrefEditor.putString(Image_ca, selectedVideoUri);
        caPrefEditor.commit();
    }

    public String getImage_ca() {
        return caPreference.getString(Image_ca, null);
    }


    public void setShapeType_ca(int st) {
        caPrefEditor.putInt(shapeType_ca, st);
        caPrefEditor.commit();
    }

    public int getShapeType_ca() {
        return caPreference.getInt(shapeType_ca, 1);
    }


    public void setFontStyle_ca(String fs) {
        caPrefEditor.putString(fontStyle_ca, fs);
        caPrefEditor.commit();
    }

    public String getFontStyle_ca() {
        return caPreference.getString(fontStyle_ca, "font/font1.ttf");
    }

    public void setClockPos_ca(String cp) {
        caPrefEditor.putString(clockPos_ca, cp);
        caPrefEditor.commit();

    }

    public String getClockPos_ca() {
        return caPreference.getString(clockPos_ca, "top");

    }

    public void setClockColor_ca(int cc) {
        caPrefEditor.putInt(clockColor_ca, cc);
        caPrefEditor.commit();
    }

    public int getClockColor_ca() {
        return caPreference.getInt(clockColor_ca, 0xFFFFFFFF);

    }

    public void setColorBorder_ca(int cc) {
        caPrefEditor.putInt(colorBorder_ca, cc);
        caPrefEditor.commit();
    }

    public int getcolorBorder() {
        return caPreference.getInt(colorBorder_ca, 0);

    }

    public void setWaveColor_ca(int cc) {
        caPrefEditor.putInt(waveColor_ca, cc);
        caPrefEditor.commit();
    }

    public int getWaveColor_ca() {
        return caPreference.getInt(waveColor_ca, 0xFFFFFFFF);

    }

    public void setBgColor_ca(int cc) {
        caPrefEditor.putInt(bgColor_ca, cc);
        caPrefEditor.commit();
    }

    public int getBgColor_ca() {
        return caPreference.getInt(bgColor_ca, 0);

    }

    public void setBorderWidth_ca(Float cc) {
        caPrefEditor.putFloat(borderWidth_ca, cc);
        caPrefEditor.commit();
    }

    public float getBorderWidth_ca() {
        return caPreference.getFloat(borderWidth_ca, 0);

    }

    public void setAmplitude_ca(int cc) {
        caPrefEditor.putInt(amplitude_ca, cc);
        caPrefEditor.commit();
    }

    public int getAmplitude_ca() {
        return caPreference.getInt(amplitude_ca, 70);

    }


    public void setPtFilepath_ca(String t) {
        caPrefEditor.putString(ptFilepath_ca, t);
        caPrefEditor.commit();

    }


    public String getPtFilepath_ca() {
        return caPreference.getString(ptFilepath_ca, "");

    }

    public void setPtTextColor_ca(int t) {
        caPrefEditor.putInt(ptTextColor_ca, t);
        caPrefEditor.commit();

    }


    public int getPtTextColor_ca() {
        return caPreference.getInt(ptTextColor_ca, 0);

    }

    public void setPtBarColor_ca(int t) {
        caPrefEditor.putInt(ptBarColor_ca, t);
        caPrefEditor.commit();

    }


    public int getPtBarColor_ca() {
        return caPreference.getInt(ptBarColor_ca, 0);

    }

    public void setPtWaveColor_ca(int t) {
        caPrefEditor.putInt(ptWaveColor_ca, t);
        caPrefEditor.commit();

    }


    public int getPtWaveColor_ca() {
        return caPreference.getInt(ptWaveColor_ca, 0);

    }

    public void setPtTagText_ca(String t) {
        caPrefEditor.putString(ptTagText_ca, t);
        caPrefEditor.commit();

    }


    public String getPtTagText_ca() {
        return caPreference.getString(ptTagText_ca, "");

    }    public void setPtOrTagText_ca(String t) {
        caPrefEditor.putString(ptOrTagText_ca, t);
        caPrefEditor.commit();

    }


    public String getPtORTagText() {
        return caPreference.getString(ptOrTagText_ca, "");

    }

    public void setPtFont(String t) {
        caPrefEditor.putString(ptTagFont_ca, t);
        caPrefEditor.commit();

    }


    public String getPtFont() {
        return caPreference.getString(ptTagFont_ca, "");

    }


    public void setCtFilepath_ca(String t) {
        caPrefEditor.putString(ctFilepath_ca, t);
        caPrefEditor.commit();

    }


    public String getCtFilepath_ca() {
        return caPreference.getString(ctFilepath_ca, "");

    }

    public void setCtTextColor_ca(int t) {
        caPrefEditor.putInt(ctTextColor_ca, t);
        caPrefEditor.commit();

    }


    public int getCtTextColor_ca() {
        return caPreference.getInt(ctTextColor_ca, 0);

    }

    public void setCtBarColor_ca(int t) {
        caPrefEditor.putInt(ctBarColor_ca, t);
        caPrefEditor.commit();

    }


    public int getCtBarColor_ca() {
        return caPreference.getInt(ctBarColor_ca, 0);

    }

    public void setCtWaveColor_ca(int t) {
        caPrefEditor.putInt(ctWaveColor_ca, t);
        caPrefEditor.commit();

    }


    public int getCtWaveColor_ca() {
        return caPreference.getInt(ctWaveColor_ca, 0);

    }

    public void setCtIconColor_ca(int t) {
        caPrefEditor.putInt(ctIconColor_ca, t);
        caPrefEditor.commit();

    }


    public int getCtIconColor_ca() {
        return caPreference.getInt(ctIconColor_ca, 0);

    }

    public void setCtTagText_ca(String t) {
        caPrefEditor.putString(ctTagText_ca, t);
        caPrefEditor.commit();

    }


    public String getCtTagText_ca() {
        return caPreference.getString(ctTagText_ca, "");

    }

    public void setCtFont(String t) {
        caPrefEditor.putString(ctTagFont_ca, t);
        caPrefEditor.commit();

    }


    public String getCtFont() {
        return caPreference.getString(ctTagFont_ca, "font/font1.ttf");

    }


    public void setAtFilepath_ca(String t) {
        caPrefEditor.putString(atFilepath_ca, t);
        caPrefEditor.commit();

    }


    public String getAtFilepath_ca() {
        return caPreference.getString(atFilepath_ca, "");

    }

    public void setAtTextColor_ca(int t) {
        caPrefEditor.putInt(atTextColor_ca, t);
        caPrefEditor.commit();

    }


    public int getAtTextColor_ca() {
        return caPreference.getInt(atTextColor_ca, 0);

    }

    public void setAtWaveColor_ca(int t) {
        caPrefEditor.putInt(atWaveColor_ca, t);
        caPrefEditor.commit();

    }


    public int getAtWaveColor_ca() {
        return caPreference.getInt(atWaveColor_ca, 0);

    }

    public void setAtTagText_ca(String t) {
        caPrefEditor.putString(atTagText_ca, t);
        caPrefEditor.commit();

    }


    public String getAtTagText_ca() {
        return caPreference.getString(atTagText_ca, "");

    }

    public void setAtFont(String t) {
        caPrefEditor.putString(atTagFont_ca, t);
        caPrefEditor.commit();

    }


    public String getAtFont() {
        return caPreference.getString(atTagFont_ca, "font/font1.ttf");

    }

    public void setAnimList_ca(int t) {
        caPrefEditor.putInt(animList_ca, t);
        caPrefEditor.commit();

    }


    public int getAnimList_ca() {
        return caPreference.getInt(animList_ca, 1);

    }

    public void setAnimFontStyle_ca(String fs) {
        caPrefEditor.putString(animFontStyle_ca, fs);
        caPrefEditor.commit();
    }

    public String getAnimFontStyle_ca() {
        return caPreference.getString(animFontStyle_ca, "font/font1.ttf");
    }

    public void setAnimFontColor_ca(int t) {
        caPrefEditor.putInt(animFontColor_ca, t);
        caPrefEditor.commit();

    }


    public int getAnimFontColor_ca() {
        return caPreference.getInt(animFontColor_ca, Color.WHITE);

    }


    public void setBubbleFilepath_ca(String t) {
        caPrefEditor.putString(bubbleFilepath_ca, t);
        caPrefEditor.commit();

    }


    public String getBubbleFilepath_ca() {
        return caPreference.getString(bubbleFilepath_ca, "");

    }


    public void setBubbleWaveColor_ca(int t) {
        caPrefEditor.putInt(bubbleWaveColor_ca, t);
        caPrefEditor.commit();

    }


    public int getBubbleWaveColor_ca() {
        return caPreference.getInt(bubbleWaveColor_ca, 0);

    }

    public void setFullAlarm_ca(boolean t) {
        caPrefEditor.putBoolean(fullAlarm_ca, t);
        caPrefEditor.commit();

    }


    public boolean getFullAlarm_ca() {
        return caPreference.getBoolean(fullAlarm_ca, false);

    }

    public void setFullAlarmLevel_ca(int t) {
        caPrefEditor.putInt(fullAlarmLevel_ca, t);
        caPrefEditor.commit();

    }


    public int getFullAlarmLevel_ca() {
        return caPreference.getInt(fullAlarmLevel_ca, 0);

    }

    public void setFullAlarmPos_ca(int t) {
        caPrefEditor.putInt(fullAlarmPos_ca, t);
        caPrefEditor.commit();

    }


    public int getFullAlarmPos_ca() {
        return caPreference.getInt(fullAlarmPos_ca, 0);

    }


    public void setLowAlarm_ca(boolean t) {
        caPrefEditor.putBoolean(lowAlarm_ca, t);
        caPrefEditor.commit();

    }


    public boolean getLowAlarm_ca() {
        return caPreference.getBoolean(lowAlarm_ca, false);

    }

    public void setLowAlarmLevel_ca(int t) {
        caPrefEditor.putInt(lowAlarmLevel_ca, t);
        caPrefEditor.commit();

    }


    public int getLowAlarmLevel_ca() {
        return caPreference.getInt(lowAlarmLevel_ca, 0);

    }

    public void setLowAlarmPos_ca(int t) {
        caPrefEditor.putInt(lowAlarmPos_ca, t);
        caPrefEditor.commit();

    }


    public int getLowAlarmPos_ca() {
        return caPreference.getInt(lowAlarmPos_ca, 0);

    }

    public void setTempAlarm_ca(boolean t) {
        caPrefEditor.putBoolean(tempAlarm_ca, t);
        caPrefEditor.commit();

    }


    public boolean getTempAlarm_ca() {
        return caPreference.getBoolean(tempAlarm_ca, false);

    }

    public void setTempAlarmLevel_ca(int t) {
        caPrefEditor.putInt(tempAlarmLevel_ca, t);
        caPrefEditor.commit();

    }


    public int getTempAlarmLevel_ca() {
        return caPreference.getInt(tempAlarmLevel_ca, 0);

    }

    public void setTempAlarmPos_ca(int t) {
        caPrefEditor.putInt(tempAlarmPos_ca, t);
        caPrefEditor.commit();

    }


    public int getTempAlarmPos_ca() {
        return caPreference.getInt(tempAlarmPos_ca, 0);

    }

    public void setAtEdited_ca(boolean t) {
        caPrefEditor.putBoolean(atEdited_ca, t);
        caPrefEditor.commit();

    }


    public boolean getAtEdited_ca() {
        return caPreference.getBoolean(atEdited_ca, false);

    }

    public void setCtEdited_ca(boolean t) {
        caPrefEditor.putBoolean(ctEdited_ca, t);
        caPrefEditor.commit();

    }


    public boolean getCtEdited_ca() {
        return caPreference.getBoolean(ctEdited_ca, false);

    }


    public void setPtEdited_ca(boolean t) {
        caPrefEditor.putBoolean(ptEdited_ca, t);
        caPrefEditor.commit();

    }


    public boolean getPtEdited_ca() {
        return caPreference.getBoolean(ptEdited_ca, false);

    }

    public void setFullAlarmRing_ca(String t) {
        caPrefEditor.putString(fullAlarmRing_ca, t);
        caPrefEditor.commit();

    }


    public String getFullAlarmRing_ca() {
        return caPreference.getString(fullAlarmRing_ca, "off");

    }

    public void setLowAlarmRing_ca(String t) {
        caPrefEditor.putString(lowAlarmRing_ca, t);
        caPrefEditor.commit();

    }


    public String getLowAlarmRing_ca() {
        return caPreference.getString(lowAlarmRing_ca, "off");

    }

    public void setTempAlarmRing_ca(String t) {
        caPrefEditor.putString(tempAlarmRing_ca, t);
        caPrefEditor.commit();

    }


    public String getTempAlarmRing_ca() {
        return caPreference.getString(tempAlarmRing_ca, "off");

    }
    public void setThemeType(int t) {
        caPrefEditor.putInt(theme_ca, t);
        caPrefEditor.commit();

    }


    public int getThemeType() {
        return caPreference.getInt(theme_ca, 4);

    }


    public void setNtTheme_ca(int t) {
        caPrefEditor.putInt(ntTheme_ca, t);
        caPrefEditor.commit();

    }


    public int getNtTheme_ca() {
        return caPreference.getInt(ntTheme_ca, 0);

    }


    public void setNtWColor(int t) {
        caPrefEditor.putInt(ntwColor_ca, t);
        caPrefEditor.commit();

    }


    public int getNtWColor() {
        return caPreference.getInt(ntwColor_ca, 0);
    }    public void setNtColorType_ca(int t) {
        caPrefEditor.putInt(ntColorType_ca, t);
        caPrefEditor.commit();

    }


    public int getNtColorType_ca() {
        return caPreference.getInt(ntColorType_ca, 1);
    }

    public void setNtFont_ca(String t) {
        caPrefEditor.putString(ntFont_ca, t);
        caPrefEditor.commit();

    }


    public String getNtFont_ca() {
        return caPreference.getString(ntFont_ca, "font/font1.ttf");

    }


    public void setOwlFont_ca(String t) {
        caPrefEditor.putString(OwlFont_ca, t);
        caPrefEditor.commit();

    }


    public String getowlFont() {
        return caPreference.getString(OwlFont_ca, "font/font1.ttf");

    }


    public void saveColorList(List<String> favorites) {


        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        caPrefEditor.putString(WBS_ca, jsonFavorites);

        caPrefEditor.commit();
    }




    public void addWBS(String product) {
        List<String> chatList = getColorList();
        if (chatList == null)
            chatList = new ArrayList<String>();
        chatList.add(product);
        saveColorList(chatList);
    }

    public void removeWBS(String product) {
        ArrayList<String> chatList = getColorList();
        if (chatList != null) {
            chatList.remove(product);
            saveColorList(chatList);
        }
    }

    public ArrayList<String> getColorList() {

        List<String> chatList;
        if (caPreference.contains(WBS_ca)) {
            String jsonFavorites = caPreference.getString(WBS_ca, null);
            Gson gson = new Gson();
            String[] chatItem = gson.fromJson(jsonFavorites,
                    String[].class);

            chatList = Arrays.asList(chatItem);
            chatList = new ArrayList<String>(chatList);
        } else {
            return new ArrayList<>();
        }

        return (ArrayList<String>) chatList;
    }




}
