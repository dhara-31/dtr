package com.si_charginganimation.nilesh_charginganimation.AnimCAApi1;

import io.michaelrocks.paranoid.Obfuscate;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
@Obfuscate
public interface APIInterface {
    @FormUrlEncoded
    @POST("index.php")
    Call<CAExample> doCreateUserWithField(@Header("Authorization") String Authorizing, @Field("packagename") String packagename, @Field("category") String category, @Header("key") String key);
}
