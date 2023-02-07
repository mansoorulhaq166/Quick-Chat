package com.example.quickchat.Models;

public class Messages {
    String id;
    String sender_email;
    String receiver_email;
    String message;
    long time_stamp;

    public Messages() {
    }

    public Messages(String message) {
        this.message = message;
    }

    public Messages(String sender_email, String message) {
        this.sender_email = sender_email;
        this.message = message;
    }

    public Messages(String id, String sender_email, String receiver_email, String message, long time_stamp) {
        this.id = id;
        this.sender_email = sender_email;
        this.receiver_email = receiver_email;
        this.message = message;
        this.time_stamp = time_stamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_email() {
        return sender_email;
    }

    public void setSender_email(String sender_email) {
        this.sender_email = sender_email;
    }

    public String getReceiver_email() {
        return receiver_email;
    }

    public void setReceiver_email(String receiver_email) {
        this.receiver_email = receiver_email;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }
}