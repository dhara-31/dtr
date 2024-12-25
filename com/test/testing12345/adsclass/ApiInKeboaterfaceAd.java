package com.test.testing12345.adsclass;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInKeboaterfaceAd {


    @FormUrlEncoded
    @POST("api.php")
    Call<ResponseApp> getAll(@Field("package") String deviceId);
}
