package com.kirillpolyakov.controller;

import com.kirillpolyakov.service.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/telegram")
public class TelegramController {
    private TelegramService telegramService;

    @Autowired
    public void setTelegramService(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @PostMapping("/message/{chatId}")
    public void send(@PathVariable long chatId, @RequestParam String message) {
        this.telegramService.sendMessage(chatId, message);
    }

    @PostMapping(value = "/photo/{chatId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void sendImage(@PathVariable long chatId, @RequestPart MultipartFile file) {
        this.telegramService.sendImage(chatId, file);
    }

    @PostMapping(value = "/document/{chatId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void sendDocument(@PathVariable long chatId, @RequestPart MultipartFile file) {
        this.telegramService.sendDocument(chatId, file);
    }

    @DeleteMapping(path = "/{chatId}/{messageId}")
    public void delete(@PathVariable long chatId, @PathVariable int messageId) {
        this.telegramService.deleteMessage(chatId, messageId);
    }
}
