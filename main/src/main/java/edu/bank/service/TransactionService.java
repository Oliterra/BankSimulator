package edu.bank.service;

import edu.bank.model.entity.Account;
import edu.bank.model.entity.Transaction;

import java.util.Map;

public interface TransactionService {

    Transaction createTransaction(Account fromAccountIban, Account toAccountIban, double sum, double fee);

    void printReceipt(Transaction transaction);

    void getTransactionHistory(Map<String, String> transactionsInfo);
}
