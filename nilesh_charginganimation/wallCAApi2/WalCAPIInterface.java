package com.si_charginganimation.nilesh_charginganimation.wallCAApi2;

import io.michaelrocks.paranoid.Obfuscate;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
@Obfuscate
public interface WalCAPIInterface {
    @FormUrlEncoded
    @POST("index.php")
    Call<WalCAExample> doCreateUserWithField(@Header("Authorization") String Authorizing, @Field("packagename") String packagename, @Field("category") String category, @Header("key") String key);
}
