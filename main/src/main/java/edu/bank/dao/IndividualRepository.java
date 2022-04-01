package edu.bank.dao;

import edu.bank.entity.Individual;

public interface IndividualRepository {

    Individual create(Individual individual);

    Individual get(long id);

    Individual getByPhone(String phone);
}
