package edu.bank.service;

import edu.bank.command.model.CommandParam;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Transaction;

import java.util.Set;

public interface TransactionService {

    Transaction createTransaction(Account fromAccountIban, Account toAccountIban, double sum, double fee);

    void getTransactionHistory(Set<CommandParam> transactionsInfo);
}
