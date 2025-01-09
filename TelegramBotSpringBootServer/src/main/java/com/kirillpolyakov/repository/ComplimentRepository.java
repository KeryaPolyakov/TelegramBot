package com.kirillpolyakov.repository;

import com.kirillpolyakov.config.Compliment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ComplimentRepository extends JpaRepository<Compliment, Long> {

    Compliment findByText (String text);

    Compliment findById(long id);
}
