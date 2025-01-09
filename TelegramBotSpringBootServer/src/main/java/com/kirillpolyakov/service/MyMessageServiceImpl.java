package com.kirillpolyakov.service;

import com.kirillpolyakov.model.MyMessage;
import com.kirillpolyakov.model.UserTelegram;
import com.kirillpolyakov.repository.MyMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyMessageServiceImpl implements MyMessageService{

    private MyMessageRepository messageRepository;

    private UserService userService;

    @Autowired
    public void setMessageRepository(MyMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void add(MyMessage myMessage, long userId) {
        UserTelegram userTelegram = this.userService.getById(userId);
        myMessage.setUserTelegram(userTelegram);
        this.messageRepository.save(myMessage);
    }

    @Override
    public List<MyMessage> getAllByUserTelegramId(long id) {
        return this.messageRepository.findAllByUserTelegramId(id);
    }

}
