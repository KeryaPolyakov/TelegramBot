package com.kirillpolyakov.telegrambotspringbootclient.retrofit;


import com.kirillpolyakov.telegrambotspringbootclient.util.Constants;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TelegramRepository {
    private final TelegramService service;

    public TelegramRepository() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .client(client)
                .build();
        this.service = retrofit.create(TelegramService.class);
    }

    public void sendMessage(long chatId, String message) throws IOException {
        Call<Void> call = service.sendMessage(chatId, message);
        sendRequest(call);
    }

    public void sendPhoto(long chatId, File file) throws IOException {
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(Files.probeContentType(file.toPath())),
                file
        );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Call<Void> call = service.sendPhoto(chatId, body);
        sendRequest(call);

    }

    public void sendDocument(long chatId, File file) throws IOException {
        RequestBody requestFile = RequestBody.create(
                MediaType.parse(Files.probeContentType(file.toPath())),
                file
        );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Call<Void> call = service.sendDocument(chatId, body);
        sendRequest(call);
    }

    private void sendRequest(Call<Void> call) {
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("message has been sent");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}