package com.example.refugees.NotificationPackage;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA-VPPnxw:APA91bGofH4Ql_LwUyaS84QUDp3xW_TgUcOy7TLlc-o9xbtldY5Gyq1peH6Ed-CXG_4EPsqIgHkmtQexN6WX1ELAa00BsajN68isSjrObBuEX6N0N5osSER-tFvz8cOwbx8YBp3JVSBV"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}