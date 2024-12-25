
package com.si_charginganimation.nilesh_charginganimation.AnimCAApi1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CAExample {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<CADatum> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CADatum> getData() {
        return data;
    }

    public void setData(List<CADatum> data) {
        this.data = data;
    }

}
