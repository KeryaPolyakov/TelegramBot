package com.kirillpolyakov.telegrambotspringbootclient.retrofit;


import com.kirillpolyakov.telegrambotspringbootclient.dto.ResponseResult;
import com.kirillpolyakov.telegrambotspringbootclient.model.History;
import com.kirillpolyakov.telegrambotspringbootclient.model.MyMessage;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface MyMessageService {
    @GET("myMessage/{id}")
    Call<ResponseResult<List<MyMessage>>> getAllByUserId(@Path("id") long id);

}
