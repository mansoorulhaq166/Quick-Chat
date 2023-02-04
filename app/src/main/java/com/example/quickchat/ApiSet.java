package com.example.quickchat;

import com.example.quickchat.Models.Login_rep_model;
import com.example.quickchat.Models.Signup_rep_model;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiSet {

    @FormUrlEncoded
    @POST("signup.php")
    Call<Signup_rep_model> getRegister(
            @Field("name") String name,
            @Field("email") String email,
            @Field("profile_pic") String profile_pic,
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<Login_rep_model> getLogin(
            @Field("email") String email,
            @Field("password") String password
    );
}
