package com.kirillpolyakov.telegrambotspringbootclient.retrofit;


import com.kirillpolyakov.telegrambotspringbootclient.dto.ResponseResult;
import com.kirillpolyakov.telegrambotspringbootclient.model.History;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface HistoryService {
    @GET("history/{id}")
    Call<ResponseResult<List<History>>> getAllByUserId(@Path("id") long id);

}
