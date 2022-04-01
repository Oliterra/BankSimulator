package edu.bank.service;

import edu.bank.entity.Account;
import edu.bank.entity.Transaction;

public interface TransactionService {

    Transaction createTransaction(Account fromAccountIban, Account toAccountIban, double sum, double fee);

    void printReceipt(Transaction transaction);
}
