package edu.bank.dao;

import edu.bank.model.entity.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository {

    Transaction create(Transaction transaction);

    List<Transaction> getAllBetweenDates(LocalDate fromDate, LocalDate toDate);
}
