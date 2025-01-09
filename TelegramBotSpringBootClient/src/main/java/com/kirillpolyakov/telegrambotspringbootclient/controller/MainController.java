package com.kirillpolyakov.telegrambotspringbootclient.controller;

import com.kirillpolyakov.telegrambotspringbootclient.App;
import com.kirillpolyakov.telegrambotspringbootclient.model.Step;
import com.kirillpolyakov.telegrambotspringbootclient.model.UserTelegram;
import com.kirillpolyakov.telegrambotspringbootclient.retrofit.UserTelegramRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class MainController {
    @FXML
    public TableView<UserTelegram> tableColumnUsers;

    private UserTelegramRepository userTelegramRepository;

    public void initialize() {
        userTelegramRepository = new UserTelegramRepository();
        TableColumn<UserTelegram, Long> id = new TableColumn<>("id");
        TableColumn<UserTelegram, Long> chatId = new TableColumn<>("chat_id");
        TableColumn<UserTelegram, String> userName = new TableColumn<>("username");
        TableColumn<UserTelegram, String> name = new TableColumn<>("name");
        TableColumn<UserTelegram, Integer> age = new TableColumn<>("age");
        TableColumn<UserTelegram, Step> step = new TableColumn<>("step");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        chatId.setCellValueFactory(new PropertyValueFactory<>("chatId"));
        userName.setCellValueFactory(new PropertyValueFactory<>("telegramUserName"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        step.setCellValueFactory(new PropertyValueFactory<>("step"));
        tableColumnUsers.getColumns().addAll(id, chatId, userName, name, age, step);
        try {
            tableColumnUsers.setItems(FXCollections.observableList(userTelegramRepository.getAll()));
        } catch (IOException e) {
            e.printStackTrace();
            App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
        tableColumnUsers.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                UserTelegram userTelegram = tableColumnUsers.getSelectionModel().getSelectedItem();
                try {
                    if (userTelegram != null) {
                        App.openWindowAndWaitModal("user.fxml", null, userTelegram);
                        tableColumnUsers.setItems(FXCollections.observableList(userTelegramRepository.getAll()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    public void buttonRefresh(ActionEvent actionEvent) {
        try {
            tableColumnUsers.setItems(FXCollections.observableList(userTelegramRepository.getAll()));
        } catch (IOException e) {
            e.printStackTrace();
            App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
