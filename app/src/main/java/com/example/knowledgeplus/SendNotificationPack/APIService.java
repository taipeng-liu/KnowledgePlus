package com.example.knowledgeplus.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers (
        {
            "Content-Type:application/json",
            "Authorization:key=AAAAVoX4LkA:APA91bH771jkVN4jojE1iy8l9uOs1Rbh2vVAD9mymJ3XpZFx8AyM-J8wr-NA8ucXS5ZMpt2Ppg-KARsf1B5YNdhkxopWXW5Mw7w56g6SBJ_heHUxworykCLwYq4638vrMMsZXMGaE-7U"
        }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
