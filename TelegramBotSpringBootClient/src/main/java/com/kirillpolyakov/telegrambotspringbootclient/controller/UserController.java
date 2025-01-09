package com.kirillpolyakov.telegrambotspringbootclient.controller;

import com.kirillpolyakov.telegrambotspringbootclient.App;
import com.kirillpolyakov.telegrambotspringbootclient.model.MyMessage;
import com.kirillpolyakov.telegrambotspringbootclient.model.UserTelegram;
import com.kirillpolyakov.telegrambotspringbootclient.retrofit.MyMessageRepository;
import com.kirillpolyakov.telegrambotspringbootclient.retrofit.TelegramRepository;
import com.kirillpolyakov.telegrambotspringbootclient.retrofit.UserTelegramRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

public class UserController implements ControllerData<UserTelegram> {

    @FXML
    public TableView<MyMessage> tableColumnMessages;
    @FXML
    public Button buttonDeleteUser;
    @FXML
    public Text labelUserName;

    private MyMessageRepository messageRepository;

    private TelegramRepository telegramRepository;

    private UserTelegram userTelegram;

    private Thread thread;

    @Override
    public void initData(UserTelegram data) {
        this.userTelegram = data;
        this.messageRepository = new MyMessageRepository();
        this.telegramRepository = new TelegramRepository();
        this.labelUserName.setText(userTelegram.getTelegramUserName());
        TableColumn<MyMessage, Long> id = new TableColumn<>("id");
        TableColumn<MyMessage, String> text = new TableColumn<>("text");
        TableColumn<MyMessage, String> time = new TableColumn<>("time");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        text.setCellValueFactory(new PropertyValueFactory<>("text"));
        time.setCellValueFactory(new PropertyValueFactory<>("localDateTime"));
        tableColumnMessages.getColumns().addAll(id, text, time);
        try {
            tableColumnMessages.setItems(
                    FXCollections.observableList(messageRepository.getAllByUserId(userTelegram.getId())));
            tableColumnMessages.scrollTo(tableColumnMessages.getItems().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void buttonDeleteUser(ActionEvent actionEvent) {
        try {
            new UserTelegramRepository().delete(userTelegram.getId());
            Stage stage = (Stage) buttonDeleteUser.getScene().getWindow();
            stage.close();
            App.showInfo("Info", "User has been successfully deleted", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            e.printStackTrace();
            App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void buttonSendText(ActionEvent actionEvent) {
        try {
            App.openWindowAndWaitModal("message.fxml", "message", userTelegram);
            tableColumnMessages.setItems(
                    FXCollections.observableList(messageRepository.getAllByUserId(userTelegram.getId())));
            tableColumnMessages.scrollTo(tableColumnMessages.getItems().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void buttonSendDocument(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Docx files (*.docx)", "*.docx");
        FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter("Xml files (*.fxml)", "*.xml");
        FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter("Txt files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll(filter, filter1, filter2);
        File file = fileChooser.showOpenDialog(null);
        try {
            if (file != null) {
                telegramRepository.sendDocument(userTelegram.getChatId(), file);
                tableColumnMessages.setItems(
                        FXCollections.observableList(messageRepository.getAllByUserId(userTelegram.getId())));
                tableColumnMessages.scrollTo(tableColumnMessages.getItems().size() - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void buttonSendImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Jpg files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(null);
        try {
            if (file != null) {
                telegramRepository.sendPhoto(userTelegram.getChatId(), file);
                tableColumnMessages.setItems(
                        FXCollections.observableList(messageRepository.getAllByUserId(userTelegram.getId())));
                tableColumnMessages.scrollTo(tableColumnMessages.getItems().size() - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void buttonUserActions(ActionEvent actionEvent) {
        try {
            App.openWindowAndWait("history.fxml", "history", userTelegram.getId());
        } catch (IOException e) {
            e.printStackTrace();
            App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void buttonUpdateMessage(ActionEvent actionEvent) {
        try {
            tableColumnMessages.setItems(
                    FXCollections.observableList(messageRepository.getAllByUserId(userTelegram.getId())));
            tableColumnMessages.scrollTo(tableColumnMessages.getItems().size() - 1);
        } catch (IOException e) {
            e.printStackTrace();
            App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
