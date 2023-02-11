package com.example.quickchat.Models;

public class Messages {
    String message_id;
    String sender_email;
    String receiver_email;
    String message;
    int seen_status;
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

    public Messages(String message_id, String sender_email, String receiver_email,
                    String message, int seen_status, long time_stamp) {
        this.message_id = message_id;
        this.sender_email = sender_email;
        this.receiver_email = receiver_email;
        this.message = message;
        this.seen_status = seen_status;
        this.time_stamp = time_stamp;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public int getSeen_status() {
        return seen_status;
    }

    public void setSeen_status(int seen_status) {
        this.seen_status = seen_status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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