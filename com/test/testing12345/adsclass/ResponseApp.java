package com.test.testing12345.adsclass;

import java.util.ArrayList;

public class ResponseApp {
    private ArrayList<DetailAds> adsdetail = new ArrayList<>();
    private AppDetailKeboa appdetail;

    public ArrayList<DetailAds> getAdsdetail() {
        return adsdetail;
    }

    public void setAdsdetail(ArrayList<DetailAds> adsdetail) {
        this.adsdetail = adsdetail;
    }

    public AppDetailKeboa getAppdetail() {
        return appdetail;
    }

    public void setAppdetail(AppDetailKeboa appdetail) {
        this.appdetail = appdetail;
    }

}
