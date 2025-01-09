package com.kirillpolyakov.telegrambotspringbootclient.retrofit;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.telegrambotspringbootclient.dto.ResponseResult;
import com.kirillpolyakov.telegrambotspringbootclient.model.UserTelegram;
import com.kirillpolyakov.telegrambotspringbootclient.util.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class UserTelegramRepository {
    private final ObjectMapper objectMapper;
    private UserTelegramService service;

    public UserTelegramRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(UserTelegramService.class);
    }

    private <T> T getData(Response<ResponseResult<T>> execute) throws IOException {
        if (execute.code() != 200) {
            String message = objectMapper.readValue(execute.errorBody().string(),
                    new TypeReference<ResponseResult<T>>() {
                    }).getMessage();
            throw new IllegalArgumentException(message);
        }
        return execute.body().getData();
    }

    public List<UserTelegram> getAll() throws IOException {
        return service.getAll().execute().body().getData();
    }

    public UserTelegram delete(long id) throws IOException {
        Response<ResponseResult<UserTelegram>> execute = service.delete(id).execute();
        return getData(execute);
    }
}