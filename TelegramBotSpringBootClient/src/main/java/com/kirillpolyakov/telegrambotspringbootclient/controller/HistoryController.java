package com.kirillpolyakov.telegrambotspringbootclient.controller;

import com.kirillpolyakov.telegrambotspringbootclient.App;
import com.kirillpolyakov.telegrambotspringbootclient.model.History;
import com.kirillpolyakov.telegrambotspringbootclient.retrofit.HistoryRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class HistoryController implements ControllerData<Long>{
    @FXML
    public TableView<History> tableViewHistory;

    @Override
    public void initData(Long data) {
        TableColumn<History, Long> id = new TableColumn<>("id");
        TableColumn<History, String> text = new TableColumn<>("text");
        TableColumn<History, String> time = new TableColumn<>("time");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        text.setCellValueFactory(new PropertyValueFactory<>("text"));
        time.setCellValueFactory(new PropertyValueFactory<>("localDateTime"));
        tableViewHistory.getColumns().addAll(id, text, time);
        try {
            tableViewHistory.setItems(
                    FXCollections.observableList(new HistoryRepository().getAllByUserId(data)));
        } catch (IOException e) {
            e.printStackTrace();
            App.showInfo("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
