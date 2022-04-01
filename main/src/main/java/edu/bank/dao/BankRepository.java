package edu.bank.dao;

import edu.bank.entity.Bank;

public interface BankRepository {

    void create(Bank bank);

    Bank get(long id);

    Bank getByName(String name);

    Bank getByIbanPrefix(String iban);

    String getIbanPrefixById(long id);

    void update(long id, Bank bank);
}
