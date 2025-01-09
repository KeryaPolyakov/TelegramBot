package com.kirillpolyakov.repository;

import com.kirillpolyakov.model.UserCompliment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface UserComplimentRepository extends JpaRepository<UserCompliment, Long> {

    List<UserCompliment> findByUserTelegramChatId(long chatId);

    void deleteUserComplimentsByUserTelegramId(long id);

}
