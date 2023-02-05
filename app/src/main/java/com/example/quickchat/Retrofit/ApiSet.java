package com.example.quickchat.Retrofit;

import com.example.quickchat.Models.Login_resp;
import com.example.quickchat.Models.Signup_resp;
import com.example.quickchat.Models.Users;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiSet {

    @FormUrlEncoded
    @POST("signup.php")
    Call<Signup_resp> getRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("profile_pic") String profile_pic,
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<Login_resp> getLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("users.php")
    Call<ArrayList<Users>> getUsers();
}
