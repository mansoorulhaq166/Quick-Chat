package com.example.quickchat.Models;

public class Users {
    String id;
    String name;
    String email;
    String profile_pic;
    String status;

    public Users() {
    }

    public Users(String id) {
        this.id = id;
    }

    public Users(String id, String name, String email, String profile_pic, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile_pic = profile_pic;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
