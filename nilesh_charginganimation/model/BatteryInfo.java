package com.si_charginganimation.nilesh_charginganimation.model;

import android.content.Intent;
import android.content.SharedPreferences;

public class BatteryInfo {
    public static final String EXTRA_HEALTH = "health";
    public static final String EXTRA_ICON_SMALL = "icon-small";
    public static final String EXTRA_LEVEL = "level";
    public static final String EXTRA_PLUGGED = "plugged";
    public static final String EXTRA_PRESENT = "present";
    public static final String EXTRA_SCALE = "scale";
    public static final String EXTRA_STATUS = "status";
    public static final String EXTRA_TECHNOLOGY = "technology";
    public static final String EXTRA_TEMPERATURE = "temperature";
    public static final String EXTRA_VOLTAGE = "voltage";
    private int health;
    private int iconSmallResId;
    private int level;
    private int plugged;
    private boolean present;
    private int scale;
    private int status;
    private String technology;
    private int temperature;
    private int voltage;

    private BatteryInfo() {
    }

    public BatteryInfo(Intent intent) {
        this.status = intent.getIntExtra("status", 0);
        this.health = intent.getIntExtra(EXTRA_HEALTH, 0);
        this.present = intent.getBooleanExtra(EXTRA_PRESENT, false);
        this.level = intent.getIntExtra("level", 0);
        this.scale = intent.getIntExtra(EXTRA_SCALE, 0);
        this.iconSmallResId = intent.getIntExtra(EXTRA_ICON_SMALL, 0);
        this.plugged = intent.getIntExtra(EXTRA_PLUGGED, 0);
        this.voltage = intent.getIntExtra(EXTRA_VOLTAGE, 0);
        this.temperature = intent.getIntExtra(EXTRA_TEMPERATURE, 0);
        this.technology = intent.getStringExtra(EXTRA_TECHNOLOGY);
    }

    public BatteryInfo(SharedPreferences sharedPreferences) {
        this.status = sharedPreferences.getInt("status", 0);
        this.health = sharedPreferences.getInt(EXTRA_HEALTH, 0);
        this.present = sharedPreferences.getBoolean(EXTRA_PRESENT, false);
        this.level = sharedPreferences.getInt("level", 0);
        this.scale = sharedPreferences.getInt(EXTRA_SCALE, 0);
        this.iconSmallResId = sharedPreferences.getInt(EXTRA_ICON_SMALL, 0);
        this.plugged = sharedPreferences.getInt(EXTRA_PLUGGED, 0);
        this.voltage = sharedPreferences.getInt(EXTRA_VOLTAGE, 0);
        this.temperature = sharedPreferences.getInt(EXTRA_TEMPERATURE, 0);
        this.technology = sharedPreferences.getString(EXTRA_TECHNOLOGY, "Unknown");
    }

    public void saveToIntent(Intent intent) {
        intent.putExtra("status", this.status);
        intent.putExtra(EXTRA_HEALTH, this.health);
        intent.putExtra(EXTRA_PRESENT, this.present);
        intent.putExtra("level", this.level);
        intent.putExtra(EXTRA_SCALE, this.scale);
        intent.putExtra(EXTRA_ICON_SMALL, this.iconSmallResId);
        intent.putExtra(EXTRA_PLUGGED, this.plugged);
        intent.putExtra(EXTRA_VOLTAGE, this.voltage);
        intent.putExtra(EXTRA_TEMPERATURE, this.temperature);
        intent.putExtra(EXTRA_TECHNOLOGY, this.technology);
    }

    public void saveToSharedPreferences(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt("status", this.status);
        edit.putInt(EXTRA_HEALTH, this.health);
        edit.putBoolean(EXTRA_PRESENT, this.present);
        edit.putInt("level", this.level);
        edit.putInt(EXTRA_SCALE, this.scale);
        edit.putInt(EXTRA_ICON_SMALL, this.iconSmallResId);
        edit.putInt(EXTRA_PLUGGED, this.plugged);
        edit.putInt(EXTRA_VOLTAGE, this.voltage);
        edit.putInt(EXTRA_TEMPERATURE, this.temperature);
        edit.putString(EXTRA_TECHNOLOGY, this.technology);
        edit.commit();
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public boolean isCharging() {
        return this.status == 2;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int i) {
        this.health = i;
    }

    public boolean isPresent() {
        return this.present;
    }

    public void setPresent(boolean z) {
        this.present = z;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int i) {
        this.level = i;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int i) {
        this.scale = i;
    }

    public int getIconSmallResId() {
        return this.iconSmallResId;
    }

    public void setIconSmallResId(int i) {
        this.iconSmallResId = i;
    }

    public int getPlugged() {
        return this.plugged;
    }

    public void setPlugged(int i) {
        this.plugged = i;
    }

    public int getVoltage() {
        return this.voltage;
    }

    public void setVoltage(int i) {
        this.voltage = i;
    }

    public int getTemperature() {
        return this.temperature;
    }

    public void setTemperature(int i) {
        this.temperature = i;
    }

    public String getTechnology() {
        return this.technology;
    }

    public void setTechnology(String str) {
        this.technology = str;
    }
}
