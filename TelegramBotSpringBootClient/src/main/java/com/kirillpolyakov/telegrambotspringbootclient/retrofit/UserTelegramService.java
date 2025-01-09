package com.kirillpolyakov.telegrambotspringbootclient.retrofit;


import com.kirillpolyakov.telegrambotspringbootclient.dto.ResponseResult;
import com.kirillpolyakov.telegrambotspringbootclient.model.UserTelegram;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface UserTelegramService {
    @GET("user")
    Call<ResponseResult<List<UserTelegram>>> getAll();

    @GET("user/{id}")
    Call<ResponseResult<UserTelegram>> get(@Path("id") long id);

    @DELETE("user/{id}")
    Call<ResponseResult<UserTelegram>> delete(@Path("id") long id);

}
