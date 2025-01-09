package com.kirillpolyakov.controller;

import com.kirillpolyakov.dto.ResponseResult;
import com.kirillpolyakov.model.MyMessage;
import com.kirillpolyakov.service.MyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("myMessage")
public class MyMessageController {

    private MyMessageService myMessageService;

    @Autowired
    public void setMyMessageService(MyMessageService myMessageService) {
        this.myMessageService = myMessageService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponseResult<List<MyMessage>>> getAll(@PathVariable long id) {
        return new ResponseEntity<>(
                new ResponseResult<>(null, this.myMessageService.getAllByUserTelegramId(id)), HttpStatus.OK);
    }
}
