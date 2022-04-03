package edu.bank.dao;

import edu.bank.model.enm.Currency;
import edu.bank.model.entity.Account;

import java.util.List;

public interface AccountRepository {

    void create(Account account);

    Account get(String iban);

    List<Account> getAll();

    List<Account> getAllByUserId(long userId);

    List<Account> getAllByUserIdAndCurrency(long userId, Currency currency);

    double getBalanceByIban(String iban);

    void delete(String iban);

    boolean isExists(String iban);

    void transferMoney(String fromAccountIban, String toAccountIban, double newFromAccountBalance, double newToAccountBalance);
}
