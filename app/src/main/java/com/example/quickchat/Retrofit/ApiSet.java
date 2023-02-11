package com.example.quickchat.Retrofit;

import com.example.quickchat.Models.Login_resp;
import com.example.quickchat.Models.Messages;
import com.example.quickchat.Models.Save_msg_resp;
import com.example.quickchat.Models.Seen_upd;
import com.example.quickchat.Models.Signup_resp;
import com.example.quickchat.Models.Users;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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

    @FormUrlEncoded
    @POST("update_seen.php")
    Call<Seen_upd> updateSeenStatus(
            @Field("message_id") String message_id
    );

    @FormUrlEncoded
    @POST("save_message.php")
    Call<Save_msg_resp> saveMsgs(
            @Field("message") String message,
            @Field("sender_email") String currentUserEmail,
            @Field("receiver_email") String receivedEmail
    );

    @GET("users.php")
    Call<ArrayList<Users>> getUsers();

    @GET("get_msg.php")
    Call<ArrayList<Messages>> getMessages();

}
