package com.si_charginganimation.nilesh_charginganimation.nilbetanim;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AppBettry implements Serializable {
    @SerializedName("adstatus")
    @Expose
    private String adstatus;
    @SerializedName("admobbanner")
    @Expose
    private String admobbanner;
    @SerializedName("admobinter")
    @Expose
    private String admobinter;
    @SerializedName("admobnative")
    @Expose
    private String admobnative;
    @SerializedName("admobappopen")
    @Expose
    private String admobappopen;
    @SerializedName("counter")
    @Expose
    private String counter;
    @SerializedName("appscreennumber")
    @Expose
    private String appscreennumber;
    @SerializedName("splashtour")
    @Expose
    private String splashtour;
    @SerializedName("splashinter")
    @Expose
    private String splashinter;
    @SerializedName("bannertonative")
    @Expose
    private String bannertonative;
    @SerializedName("bignative")
    @Expose
    private String bignative;
    @SerializedName("privacy")
    @Expose
    private String privacy;

    public String getAdstatus() {
        return adstatus;
    }

    public void setAdstatus(String adstatus) {
        this.adstatus = adstatus;
    }

    public String getAdmobbanner() {
        return admobbanner;
    }

    public void setAdmobbanner(String admobbanner) {
        this.admobbanner = admobbanner;
    }

    public String getAdmobinter() {
        return admobinter;
    }

    public void setAdmobinter(String admobinter) {
        this.admobinter = admobinter;
    }

    public String getAdmobnative() {
        return admobnative;
    }

    public void setAdmobnative(String admobnative) {
        this.admobnative = admobnative;
    }

    public String getAdmobappopen() {
        return admobappopen;
    }

    public void setAdmobappopen(String admobappopen) {
        this.admobappopen = admobappopen;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getAppscreennumber() {
        return appscreennumber;
    }

    public void setAppscreennumber(String appscreennumber) {
        this.appscreennumber = appscreennumber;
    }

    public String getSplashtour() {
        return splashtour;
    }

    public void setSplashtour(String splashtour) {
        this.splashtour = splashtour;
    }

    public String getSplashinter() {
        return splashinter;
    }

    public void setSplashinter(String splashinter) {
        this.splashinter = splashinter;
    }

    public String getBannertonative() {
        return bannertonative;
    }

    public void setBannertonative(String bannertonative) {
        this.bannertonative = bannertonative;
    }

    public String getBignative() {
        return bignative;
    }

    public void setBignative(String bignative) {
        this.bignative = bignative;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

}
