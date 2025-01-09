package com.kirillpolyakov.service;

import com.kirillpolyakov.model.History;

import java.util.List;

public interface HistoryService {

    void add(History history, long userId);


    List<History> getAllByUserId(long id);

}
