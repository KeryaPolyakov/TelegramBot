package com.kirillpolyakov.service;

import com.kirillpolyakov.model.MyMessage;
import com.kirillpolyakov.model.UserTelegram;
import com.kirillpolyakov.repository.UserTelegramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserTelegramRepository userRepository;

    private MyMessageService myMessageService;

    private TelegramService telegramService;

    @Autowired
    public void setTelegramService(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Autowired
    public void setMyMessageService(MyMessageService myMessageService) {
        this.myMessageService = myMessageService;
    }

    @Autowired
    public void setUserRepository(UserTelegramRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(UserTelegram user) {
        try {
            this.userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserTelegram getByChatId(long id) {
        return this.userRepository.findUserTelegramByChatId(id);
    }

    @Override
    public void update(UserTelegram userTelegram) {
        UserTelegram old = this.getByChatId(userTelegram.getChatId());
        old.setLogin(userTelegram.getLogin());
        old.setName(userTelegram.getName());
        old.setAge(userTelegram.getAge());
        old.setStep(userTelegram.getStep());
        this.userRepository.save(old);
    }

    @Override
    public UserTelegram getById(long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public UserTelegram delete(long id) {
        UserTelegram userTelegram = this.getById(id);
        if (userTelegram != null) {
            List<MyMessage> myMessages = this.myMessageService.getAllByUserTelegramId(id);
            this.userRepository.delete(userTelegram);
            Thread thread = new Thread(() -> {
                myMessages.forEach(x -> this.telegramService.deleteMessage(userTelegram.getChatId(), x.getTelegramId()));
            });
            thread.start();
            return userTelegram;
        }
        throw new IllegalArgumentException("User doesn't exist");
    }

    @Override
    public List<UserTelegram> getAll() {
        return this.userRepository.findAll();
    }


}
