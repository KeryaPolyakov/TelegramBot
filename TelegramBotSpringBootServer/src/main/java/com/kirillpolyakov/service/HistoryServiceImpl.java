package com.kirillpolyakov.service;

import com.kirillpolyakov.model.History;
import com.kirillpolyakov.model.UserTelegram;
import com.kirillpolyakov.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService{

    private HistoryRepository historyRepository;

    private UserService userService;

    @Autowired
    public void setHistoryRepository(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void add(History history, long userId) {
        UserTelegram userTelegram = this.userService.getById(userId);
        if (userTelegram != null) {
            history.setUserTelegram(userTelegram);
            this.historyRepository.save(history);
        }
    }

    @Override
    public List<History> getAllByUserId(long id) {
        return this.historyRepository.findAllByUserTelegramId(id);
    }
}
