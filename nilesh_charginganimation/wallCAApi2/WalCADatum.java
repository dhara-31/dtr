
package com.si_charginganimation.nilesh_charginganimation.wallCAApi2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalCADatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("wallpaper")
    @Expose
    private List<WallpaperCA> wallpaperCA = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<WallpaperCA> getWallpaper() {
        return wallpaperCA;
    }

    public void setWallpaper(List<WallpaperCA> wallpaperCA) {
        this.wallpaperCA = wallpaperCA;
    }
}
