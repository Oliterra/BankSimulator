package edu.bank.dao;

import edu.bank.exeption.DAOException;
import edu.bank.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransactionRepository {
    private final ConnectionHolder connectionHolder;

    private final AccountRepository accountRepository;

    public Transaction create(Transaction transaction) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("INSERT INTO transactions(recipient_account, " +
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
            throw new DAOException();
        }
        return transaction;
    }

    public List<Transaction> getAllBetweenDates(LocalDate fromDate, LocalDate toDate) {
        List<Transaction> transactions = new ArrayList<>();
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM transactions WHERE " +
                "date BETWEEN ? AND ?")) {
            preparedStatement.setDate(1, Date.valueOf(fromDate));
            preparedStatement.setDate(2, Date.valueOf(toDate));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Transaction transaction = doGetMapping(resultSet);
                transactions.add(transaction);
            }
            return transactions;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void deleteByRecipientAccountIban(String iban) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("DELETE FROM transactions WHERE recipient_account=?")) {
            preparedStatement.setString(1, iban);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException();
        }
    }

    public void deleteBySenderAccountIban(String iban) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("DELETE FROM transactions WHERE sender_account=?")) {
            preparedStatement.setString(1, iban);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    private Transaction doGetMapping(ResultSet resultSet) {
        try {
            Transaction transaction = new Transaction();
            transaction.setId(resultSet.getLong("id"));
            transaction.setRecipientAccount(accountRepository.get(resultSet.getString("recipient_account")));
            transaction.setSenderAccount(accountRepository.get(resultSet.getString("sender_account")));
            transaction.setFee(resultSet.getDouble("fee"));
            transaction.setDate(resultSet.getDate("date").toLocalDate());
            transaction.setFullSum(resultSet.getDouble("full_sum"));
            transaction.setTime(resultSet.getTime("time").toLocalTime());
            return transaction;
        } catch (Exception e) {
            throw new DAOException();
        }
    }
}
