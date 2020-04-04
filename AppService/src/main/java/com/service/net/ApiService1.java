package com.service.net;

import com.service.base.UserBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService1 {

    @POST("index.php?action=test")
    @FormUrlEncoded
    Call<UserBean> getUser(@Field("action") String action, @Field("text") String text);

    @GET("helloworld.txt")
    Call<String> testHttps();
}
