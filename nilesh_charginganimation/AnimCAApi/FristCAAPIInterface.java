package com.si_charginganimation.nilesh_charginganimation.AnimCAApi;


import io.michaelrocks.paranoid.Obfuscate;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
@Obfuscate
public interface FristCAAPIInterface {
    @FormUrlEncoded
    @POST("encryption.php")
    Call<FirstCAApi> doCreateUserWithField(@Field("packagename") String packagename, @Field("category") String category, @Field("username") String username, @Field("password") String password);
}
