package com.kirillpolyakov.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "usersTelegram")
public class UserTelegram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @JoinColumn(unique = true)
    private long chatId;

    @NonNull
    private String telegramUserName;

    private String login;

    private String name;

    private int age;

    private Step step;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userTelegram")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
    private List<UserCompliment> userCompliments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userTelegram", fetch = FetchType.EAGER)
    private List<History> histories = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userTelegram")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE)
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

    public UserTelegram(long chatId, @NotNull String telegramUserName, String login, Step step) {
        this.chatId = chatId;
        this.telegramUserName = telegramUserName;
        this.login = login;
        this.step = step;
    }

    public UserTelegram(long chatId, @NotNull String telegramUserName, Step step) {
        this.chatId = chatId;
        this.telegramUserName = telegramUserName;
        this.step = step;
    }

    public UserTelegram(long chatId, @NotNull String telegramUserName, String login, String name, Step step) {
        this.chatId = chatId;
        this.telegramUserName = telegramUserName;
        this.login = login;
        this.name = name;
        this.step = step;
    }

    public UserTelegram(long chatId, @NotNull String telegramUserName, String login, String name, int age, Step step) {
        this.chatId = chatId;
        this.telegramUserName = telegramUserName;
        this.login = login;
        this.name = name;
        this.age = age;
        this.step = step;
    }
}
