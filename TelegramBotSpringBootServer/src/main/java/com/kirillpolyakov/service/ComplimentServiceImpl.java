package com.kirillpolyakov.service;

import com.kirillpolyakov.config.Compliment;
import com.kirillpolyakov.config.DataList;
import com.kirillpolyakov.repository.ComplimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ComplimentServiceImpl implements ComplimentService {

    private ComplimentRepository complimentRepository;

    private DataList dataList;

    @Autowired
    public void setComplimentRepository(ComplimentRepository complimentRepository) {
        this.complimentRepository = complimentRepository;
    }

    @Autowired
    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    @Override
    public void add(Compliment compliment) {
        try {
            this.complimentRepository.save(compliment);
        } catch (DataIntegrityViolationException ignored) {
        }
    }

    @Override
    public Compliment get(long id) {
        return this.complimentRepository.findById(id);
    }

    @Override
    public List<Compliment> getAll() {
        return this.complimentRepository.findAll();
    }

    @Override
    public void addNotInDB() {
        for (Compliment compliment : this.dataList.getList()) {
            if (this.complimentRepository.findByText(compliment.getText()) == null) {
                this.add(new Compliment(compliment.getText()));
            }
        }
    }
}
