package com.kirillpolyakov.service;

import com.kirillpolyakov.config.Compliment;

import java.util.List;

public interface ComplimentService {

    void add(Compliment compliment);

    Compliment get(long id);

    List<Compliment> getAll();

    void addNotInDB();
}
