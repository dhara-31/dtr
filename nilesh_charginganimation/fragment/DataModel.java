package com.si_charginganimation.nilesh_charginganimation.fragment;

import android.net.Uri;

public class DataModel {
    String FilePath;

    Uri uri;
    int height;

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }
}
