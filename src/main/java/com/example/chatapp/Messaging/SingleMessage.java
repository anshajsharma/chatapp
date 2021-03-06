package com.example.chatapp.Messaging;

public class SingleMessage {

    String message_body;
    String type;
    String delivery_status;
    String sender;
    String receiver;
    String image_url;
    String video_url;
    String availability;
    String downloaded;
    String file_name;
    String file_url;
    String location_URL;
    String messageID;
    long timestamp , sent_timestamp , received_timestamp , seen_timestamp ;

    public SingleMessage(String message_body, String type, String delivery_status, String sender, String receiver, String image_url, String video_url, String availability, String downloaded, String file_name, String file_url, String location_URL, long timestamp, long sent_timestamp, long received_timestamp, long seen_timestamp) {
        this.message_body = message_body;
        this.type = type;
        this.delivery_status = delivery_status;
        this.sender = sender;
        this.receiver = receiver;
        this.image_url = image_url;
        this.video_url = video_url;
        this.availability = availability;
        this.downloaded = downloaded;
        this.file_name = file_name;
        this.file_url = file_url;
        this.location_URL = location_URL;
        this.timestamp = timestamp;
        this.sent_timestamp = sent_timestamp;
        this.received_timestamp = received_timestamp;
        this.seen_timestamp = seen_timestamp;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public SingleMessage(String message_body, String type, String delivery_status, String sender, String receiver, String image_url, String video_url, String availability, String downloaded, String file_name, String file_url, String location_URL, String messageID, long timestamp, long sent_timestamp, long received_timestamp, long seen_timestamp) {
        this.message_body = message_body;
        this.type = type;
        this.delivery_status = delivery_status;
        this.sender = sender;
        this.receiver = receiver;
        this.image_url = image_url;
        this.video_url = video_url;
        this.availability = availability;
        this.downloaded = downloaded;
        this.file_name = file_name;
        this.file_url = file_url;
        this.location_URL = location_URL;
        this.messageID = messageID;
        this.timestamp = timestamp;
        this.sent_timestamp = sent_timestamp;
        this.received_timestamp = received_timestamp;
        this.seen_timestamp = seen_timestamp;
    }

    public SingleMessage(String message_body, String type, String delivery_status, String sender, String receiver, String image_url, String video_url, String availability, String downloaded, String file_name, String file_url, long timestamp, long sent_timestamp, long received_timestamp, long seen_timestamp) {
        this.message_body = message_body;
        this.type = type;
        this.delivery_status = delivery_status;
        this.sender = sender;
        this.receiver = receiver;
        this.image_url = image_url;
        this.video_url = video_url;
        this.availability = availability;
        this.downloaded = downloaded;
        this.file_name = file_name;
        this.file_url = file_url;
        this.timestamp = timestamp;
        this.sent_timestamp = sent_timestamp;
        this.received_timestamp = received_timestamp;
        this.seen_timestamp = seen_timestamp;
    }

    public SingleMessage(String message_body, String type, String delivery_status, String sender, String receiver, String image_url, String video_url, String availability, String downloaded, String file_name, long timestamp, long sent_timestamp, long received_timestamp, long seen_timestamp) {
        this.message_body = message_body;
        this.type = type;
        this.delivery_status = delivery_status;
        this.sender = sender;
        this.receiver = receiver;
        this.image_url = image_url;
        this.video_url = video_url;
        this.availability = availability;
        this.downloaded = downloaded;
        this.file_name = file_name;
        this.timestamp = timestamp;
        this.sent_timestamp = sent_timestamp;
        this.received_timestamp = received_timestamp;
        this.seen_timestamp = seen_timestamp;
    }

    public String getFile_url() {
        return file_url;
    }

    public String getLocation_URL() {
        return location_URL;
    }

    public void setLocation_URL(String location_URL) {
        this.location_URL = location_URL;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public SingleMessage(String message_body, String type, String delivery_status, String sender, String receiver, String image_url, String video_url, String availability, String downloaded, long timestamp, long sent_timestamp, long received_timestamp, long seen_timestamp) {
        this.message_body = message_body;
        this.type = type;
        this.delivery_status = delivery_status;
        this.sender = sender;
        this.receiver = receiver;
        this.image_url = image_url;
        this.video_url = video_url;
        this.availability = availability;
        this.downloaded = downloaded;
        this.timestamp = timestamp;
        this.sent_timestamp = sent_timestamp;
        this.received_timestamp = received_timestamp;
        this.seen_timestamp = seen_timestamp;
    }

    public String getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(String downloaded) {
        this.downloaded = downloaded;
    }

    public SingleMessage(String message_body, String type, String delivery_status, String sender, String receiver, String image_url, String video_url, long timestamp, long sent_timestamp, long received_timestamp, long seen_timestamp, String availability) {
        this.message_body = message_body;
        this.type = type;
        this.delivery_status = delivery_status;
        this.sender = sender;
        this.receiver = receiver;
        this.image_url = image_url;
        this.video_url = video_url;
        this.timestamp = timestamp;
        this.sent_timestamp = sent_timestamp;
        this.received_timestamp = received_timestamp;
        this.seen_timestamp = seen_timestamp;
        this.availability = availability;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public SingleMessage(String message_body, String type, String delivery_status, String sender, String receiver, String image_url, long timestamp, long sent_timestamp, long received_timestamp, long seen_timestamp, String availability) {
        this.message_body = message_body;
        this.type = type;
        this.delivery_status = delivery_status;
        this.sender = sender;
        this.receiver = receiver;
        this.image_url = image_url;
        this.timestamp = timestamp;
        this.sent_timestamp = sent_timestamp;
        this.received_timestamp = received_timestamp;
        this.seen_timestamp = seen_timestamp;
        this.availability = availability;   // 0 means both.. 1 means delete only sender ... 2 means none(delete  ref)....
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }



    public SingleMessage(String message_body, String type, String delivery_status, String sender, String receiver, long timestamp, long sent_timestamp, long received_timestamp, long seen_timestamp, String availability) {
        this.message_body = message_body;
        this.type = type;
        this.delivery_status = delivery_status;
        this.sender = sender;
        this.receiver = receiver;
        this.timestamp = timestamp;
        this.sent_timestamp = sent_timestamp;
        this.received_timestamp = received_timestamp;
        this.seen_timestamp = seen_timestamp;
        this.availability = availability;
    }

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
