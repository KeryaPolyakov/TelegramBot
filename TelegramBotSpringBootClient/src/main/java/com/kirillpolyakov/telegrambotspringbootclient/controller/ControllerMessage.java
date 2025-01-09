package com.kirillpolyakov.telegrambotspringbootclient.controller;

import com.kirillpolyakov.telegrambotspringbootclient.model.UserTelegram;
import com.kirillpolyakov.telegrambotspringbootclient.retrofit.TelegramRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Arrays;

public class ControllerMessage implements ControllerData<UserTelegram> {
    @FXML
    public TextField textFieldMessage;

    private UserTelegram userTelegram;

    @Override
    public void initData(UserTelegram data) {
        userTelegram = data;
    }

    public void buttonSend(ActionEvent actionEvent) {
        String text = textFieldMessage.getText();
        try {
            if (!text.isEmpty()
                    && !isSpace(text)) {
                new TelegramRepository().sendMessage(userTelegram.getChatId(), text);
                textFieldMessage.setText("");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isSpace(String s) {
        return Arrays.stream(s.split("")).allMatch(x -> x.equals(" "));
    }
}
