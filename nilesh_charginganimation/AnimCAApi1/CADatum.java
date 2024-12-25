
package com.si_charginganimation.nilesh_charginganimation.AnimCAApi1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CADatum {

    @SerializedName("original_url")
    @Expose
    private String originalUrl;
    @SerializedName("resized_url")
    @Expose
    private String resizedUrl;
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailUrl;

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getResizedUrl() {
        return resizedUrl;
    }

    public void setResizedUrl(String resizedUrl) {
        this.resizedUrl = resizedUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

}
