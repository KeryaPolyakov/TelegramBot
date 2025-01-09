package com.kirillpolyakov.telegrambotspringbootclient.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class UserTelegram {

    private long id;

    @NonNull
    private long chatId;

    @NonNull
    private String telegramUserName;

    private String login;

    private String name;

    private int age;

    private Step step;

    private List<UserCompliment> userCompliments = new ArrayList<>();

    private List<History> histories = new ArrayList<>();

    private List<MyMessage> myMessages = new ArrayList<>();

    public UserTelegram(long id, @NonNull long chatId, @NonNull String telegramUserName, Step step) {
        this.id = id;
        this.chatId = chatId;
        this.telegramUserName = telegramUserName;
        this.step = step;
    }

    public UserTelegram(long id, @NonNull long chatId, @NonNull String telegramUserName) {
        this.id = id;
        this.chatId = chatId;
        this.telegramUserName = telegramUserName;
    }

    public UserTelegram(long chatId, String telegramUserName, String login, Step step) {
        this.chatId = chatId;
        this.telegramUserName = telegramUserName;
        this.login = login;
        this.step = step;
    }

    public UserTelegram(long chatId, String telegramUserName, Step step) {
        this.chatId = chatId;
        this.telegramUserName = telegramUserName;
        this.step = step;
    }

    public UserTelegram(long chatId, String telegramUserName, String login, String name, Step step) {
        this.chatId = chatId;
        this.telegramUserName = telegramUserName;
        this.login = login;
        this.name = name;
        this.step = step;
    }

    public UserTelegram(long chatId, String telegramUserName, String login, String name, int age, Step step) {
        this.chatId = chatId;
        this.telegramUserName = telegramUserName;
        this.login = login;
        this.name = name;
        this.age = age;
        this.step = step;
    }
}
