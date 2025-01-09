package com.kirillpolyakov.service;

import com.kirillpolyakov.model.UserCompliment;
import com.kirillpolyakov.model.UserTelegram;
import com.kirillpolyakov.repository.UserComplimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserComplimentServiceImpl implements UserComplimentService {

    private UserComplimentRepository userComplimentRepository;

    private ComplimentService complimentService;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setUserComplimentRepository(UserComplimentRepository userComplimentRepository) {
        this.userComplimentRepository = userComplimentRepository;
    }

    @Autowired
    public void setComplimentService(ComplimentService complimentService) {
        this.complimentService = complimentService;
    }

    @Override
    public void add(UserCompliment userCompliment, long userTelegramId) {
        try {
            UserTelegram userTelegram = this.userService.getByChatId(userTelegramId);
            userCompliment.setUserTelegram(userTelegram);
            this.userComplimentRepository.save(userCompliment);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public UserCompliment get(long id) {
        return this.userComplimentRepository.getById(id);
    }

    @Override
    public void deleteAllByUserTelegramId(long id) {
        UserTelegram userTelegram = this.userService.getById(id);
        if (userTelegram != null) {
            this.userComplimentRepository.deleteUserComplimentsByUserTelegramId(id);
        }
    }

    @Override
    public List<UserCompliment> getByUserId(long id) {
        return this.userComplimentRepository.findByUserTelegramChatId(id);
    }
}
