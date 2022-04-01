package edu.bank.dao.impl;

import edu.bank.dao.TransactionRepository;
import edu.bank.entity.Transaction;
import edu.bank.exeption.UnexpectedInternalError;

import java.sql.*;

public class TransactionRepositoryImpl extends BaseRepository implements TransactionRepository {

    @Override
    public Transaction create(Transaction transaction) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO transactions(recipient_account, " +
                "sender_account, full_sum, fee, date, time) VALUES(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, transaction.getRecipientAccount().getIban());
            preparedStatement.setString(2, transaction.getSenderAccount().getIban());
            preparedStatement.setDouble(3, transaction.getFullSum());
            preparedStatement.setDouble(4, transaction.getFee());
            preparedStatement.setDate(5, Date.valueOf(transaction.getDate()));
            preparedStatement.setTime(6, Time.valueOf(transaction.getTime()));
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) transaction.setId(resultSet.getLong(1));
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
        return transaction;
    }
}
