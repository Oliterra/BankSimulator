package edu.bank.dao;

import edu.bank.model.entity.Individual;

import java.util.List;

public interface IndividualRepository {

    Individual create(Individual individual);

    List<Individual> getAll();

    Individual get(long id);

    Individual getByPhone(String phone);

    void update(long id, Individual individual);
}
