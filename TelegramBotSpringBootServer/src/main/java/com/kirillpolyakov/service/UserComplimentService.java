package com.kirillpolyakov.service;

import com.kirillpolyakov.model.UserCompliment;

import java.util.List;

public interface UserComplimentService {

    void add(UserCompliment userCompliment, long userTelegramId);

    UserCompliment get(long id);

    List<UserCompliment> getByUserId(long id);

    void deleteAllByUserTelegramId(long id);
}
