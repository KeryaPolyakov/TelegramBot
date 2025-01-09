package com.kirillpolyakov.repository;

import com.kirillpolyakov.model.MyMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyMessageRepository extends JpaRepository<MyMessage, Long> {

    List<MyMessage> findAllByUserTelegramId(long id);
}
