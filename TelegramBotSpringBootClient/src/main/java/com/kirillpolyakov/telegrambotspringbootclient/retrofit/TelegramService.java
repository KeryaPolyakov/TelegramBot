package com.kirillpolyakov.telegrambotspringbootclient.retrofit;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.*;


public interface TelegramService {
    @POST("telegram/message/{chatId}")
    Call<Void> sendMessage(@Path("chatId") long id, @Query("message") String message);

    @Multipart
    @POST("telegram/photo/{chatId}")
    Call<Void> sendPhoto(@Path("chatId") long chatId, @Part MultipartBody.Part file);
    @Multipart
    @POST("telegram/document/{chatId}")
    Call<Void> sendDocument(@Path("chatId") long chatId, @Part MultipartBody.Part file);


}
