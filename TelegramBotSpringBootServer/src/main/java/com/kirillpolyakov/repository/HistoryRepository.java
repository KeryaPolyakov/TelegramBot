package com.kirillpolyakov.repository;

import com.kirillpolyakov.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByUserTelegramId(long id);
}
