package com.kirillpolyakov.service;

import com.kirillpolyakov.model.UserTelegram;

import java.util.List;

public interface UserService {

    void add(UserTelegram user);

    UserTelegram getByChatId(long id);

    void update(UserTelegram userTelegram);

    UserTelegram getById(long id);

    UserTelegram delete(long id);

    List<UserTelegram> getAll();


}
