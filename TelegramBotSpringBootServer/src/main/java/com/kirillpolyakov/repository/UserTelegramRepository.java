package com.kirillpolyakov.repository;

import com.kirillpolyakov.model.UserTelegram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserTelegramRepository extends JpaRepository<UserTelegram, Long> {

    UserTelegram findUserTelegramByChatId(long chatId);

}
