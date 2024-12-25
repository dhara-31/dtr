
package com.si_charginganimation.nilesh_charginganimation.wallCAApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalFirstCAApi {

    @SerializedName("packageName")
    @Expose
    private String packageName;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("headerkey")
    @Expose
    private String headerkey;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeaderkey() {
        return headerkey;
    }

    public void setHeaderkey(String headerkey) {
        this.headerkey = headerkey;
    }

}
