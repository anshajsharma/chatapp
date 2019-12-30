package com.example.chatapp.Fragments;

import com.example.chatapp.Notification.MyResponse;
import com.example.chatapp.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAq51T7bA:APA91bHb4Ju2FOmiuCIIEvEdO5E6BnG8YGD8NwDXyTALCALZu5NbqgmQa6KGIuHHw-csjx_0aYAkIh-tknAL3g7IZkE847PcHr9xdYEZrkp6tOT92oOYPlo45GdJxYlIU3lFviwEOI7-"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
