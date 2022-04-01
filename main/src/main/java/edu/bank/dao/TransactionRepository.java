package edu.bank.dao;

import edu.bank.entity.Transaction;

public interface TransactionRepository {

    Transaction create(Transaction transaction);
}
