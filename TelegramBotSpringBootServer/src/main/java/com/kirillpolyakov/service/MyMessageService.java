package com.kirillpolyakov.service;

import com.kirillpolyakov.model.MyMessage;

import java.util.List;

public interface MyMessageService {

    void add(MyMessage myMessage, long userId);

    List<MyMessage> getAllByUserTelegramId(long id);

}
