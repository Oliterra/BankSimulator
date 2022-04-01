package edu.bank.service.impl;

import edu.bank.dao.TransactionRepository;
import edu.bank.dao.impl.TransactionRepositoryImpl;
import edu.bank.entity.Account;
import edu.bank.entity.Transaction;
import edu.bank.service.TransactionService;

import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository = new TransactionRepositoryImpl();

    @Override
    public Transaction createTransaction(Account fromAccount, Account toAccount, double sum, double fee) {
        Transaction transaction = new Transaction();
        transaction.setSenderAccount(fromAccount);
        transaction.setRecipientAccount(toAccount);
        transaction.setFullSum(sum);
        transaction.setFee(fee);
        transaction.setDate(LocalDate.now());
        transaction.setTime(LocalTime.now());
        return transactionRepository.create(transaction);
    }

    @Override
    public void printReceipt(Transaction transaction) {

    }
}
