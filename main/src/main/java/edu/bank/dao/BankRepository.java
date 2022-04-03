package edu.bank.dao;

import edu.bank.model.entity.Bank;

import java.util.List;

public interface BankRepository {

    void create(Bank bank);

    List<Bank> getAll();

    Bank get(long id);

    Bank getByName(String name);

    Bank getByIbanPrefix(String iban);

    String getIbanPrefixById(long id);

    void update(long id, Bank bank);

    void delete(long id);

    boolean isExists(long id);

    int getUsersCount(long id);
}
