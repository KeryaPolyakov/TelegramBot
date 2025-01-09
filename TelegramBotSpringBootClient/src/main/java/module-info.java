module com.kirillpolyakov.telegrambotspringbootclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.annotation;
    requires static lombok;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires okhttp3;
    requires retrofit2;
    requires retrofit2.converter.jackson;


    opens com.kirillpolyakov.telegrambotspringbootclient to javafx.fxml;
    exports com.kirillpolyakov.telegrambotspringbootclient;
    exports com.kirillpolyakov.telegrambotspringbootclient.controller to javafx.fxml;
    opens com.kirillpolyakov.telegrambotspringbootclient.controller to javafx.fxml;
    exports com.kirillpolyakov.telegrambotspringbootclient.dto to  com.fasterxml.jackson.databind;
    exports com.kirillpolyakov.telegrambotspringbootclient.model to com.fasterxml.jackson.databind;
    opens com.kirillpolyakov.telegrambotspringbootclient.model to javafx.base;
}