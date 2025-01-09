package com.kirillpolyakov.telegrambotspringbootclient.retrofit;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kirillpolyakov.telegrambotspringbootclient.dto.ResponseResult;
import com.kirillpolyakov.telegrambotspringbootclient.model.MyMessage;
import com.kirillpolyakov.telegrambotspringbootclient.util.Constants;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class MyMessageRepository {
    private final ObjectMapper objectMapper;
    private MyMessageService service;

    public MyMessageRepository() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(client)
                .build();
        this.service = retrofit.create(MyMessageService.class);
    }

    public List<MyMessage> getAllByUserId(long id) throws IOException {
        return service.getAllByUserId(id).execute().body().getData();
    }
}