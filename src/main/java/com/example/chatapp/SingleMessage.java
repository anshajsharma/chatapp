package com.example.chatapp;

public class SingleMessage {

    String message_body, type , delivery_status , sender , receiver ;
    long timestamp , sent_timestamp , received_timestamp , seen_timestamp;

    public SingleMessage() {
    }

    public SingleMessage(String message_body, String type, String delivery_status, String sender, String receiver, long timestamp, long sent_timestamp, long received_timestamp, long seen_timestamp) {
        this.message_body = message_body;
        this.type = type;
        this.delivery_status = delivery_status;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.sent_timestamp = sent_timestamp;
        this.received_timestamp = received_timestamp;
        this.seen_timestamp = seen_timestamp;
    }

    public SingleMessage(String message_body, String type, String delivery_status, String sender, String receiver, long timestamp) {
        this.message_body = message_body;
        this.type = type;
        this.delivery_status = delivery_status;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    public String getMessage_body() {
        return message_body;
    }

    public void setMessage_body(String message_body) {
        this.message_body = message_body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getSent_timestamp() {
        return sent_timestamp;
    }

    public void setSent_timestamp(long sent_timestamp) {
        this.sent_timestamp = sent_timestamp;
    }

    public long getReceived_timestamp() {
        return received_timestamp;
    }

    public void setReceived_timestamp(long received_timestamp) {
        this.received_timestamp = received_timestamp;
    }

    public long getSeen_timestamp() {
        return seen_timestamp;
    }

    public void setSeen_timestamp(long seen_timestamp) {
        this.seen_timestamp = seen_timestamp;
    }

}
