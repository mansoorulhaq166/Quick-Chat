package com.example.quickchat.Models;

public class Login_resp {
    String message;

    public Login_resp() {
    }

    public Login_resp(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
