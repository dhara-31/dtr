package com.si_charginganimation.nilesh_charginganimation.wallCAApi;


import io.michaelrocks.paranoid.Obfuscate;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

@Obfuscate
public interface WalFristCAAPIInterface {
    @FormUrlEncoded
    @POST("encryption.php")
    Call<WalFirstCAApi> doCreateUserWithField(@Field("packagename") String packagename, @Field("category") String category, @Field("username") String username, @Field("password") String password);
}
