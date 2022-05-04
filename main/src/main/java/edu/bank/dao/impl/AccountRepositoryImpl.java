package edu.bank.dao.impl;

import edu.bank.dao.AccountRepository;
import edu.bank.dao.UserRepository;
import edu.bank.exeption.DAOException;
import edu.bank.model.enm.Currency;
import edu.bank.model.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {
    private final ConnectionHolder connectionHolder;

    private final UserRepository userRepository;

    @Override
    public void create(Account account) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("INSERT INTO accounts(iban, " +
                "user_id, currency, balance, registration_date) VALUES(?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, account.getIban());
            preparedStatement.setLong(2, account.getUser().getId());
            preparedStatement.setString(3, account.getCurrency().toString());
            preparedStatement.setDouble(4, account.getBalance());
            preparedStatement.setDate(5, Date.valueOf(account.getRegistrationDate()));
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    @Override
    public Account get(String iban) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM accounts WHERE iban=?")) {
            preparedStatement.setString(1, iban);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return doGetMapping(resultSet);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM accounts")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = doGetMapping(resultSet);
                accounts.add(account);
            }
            return accounts;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Account> getAllByUserId(long userId) {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM accounts WHERE user_id=?")) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = doGetMapping(resultSet);
                accounts.add(account);
            }
            return accounts;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Account> getAllByUserIdAndCurrency(long userId, Currency currency) {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM accounts WHERE user_id=? AND currency=?")) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, currency.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = doGetMapping(resultSet);
                accounts.add(account);
            }
            return accounts;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Account doGetMapping(ResultSet resultSet) {
        try {
            Account account = new Account();
            account.setIban(resultSet.getString("iban"));
            account.setUser(userRepository.get(resultSet.getLong("user_id")));
            account.setCurrency(Currency.valueOf(resultSet.getString("currency")));
            account.setBalance(resultSet.getDouble("balance"));
            account.setRegistrationDate(resultSet.getDate("registration_date").toLocalDate());
            return account;
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    @Override
    public double getBalanceByIban(String iban) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT balance FROM accounts WHERE iban=?")) {
            preparedStatement.setString(1, iban);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getDouble("balance");
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void delete(String iban) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("DELETE FROM accounts WHERE iban=?")) {
            preparedStatement.setString(1, iban);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isExists(String iban) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT COUNT(*) AS recordCount " +
                "FROM accounts WHERE iban=?")) {
            preparedStatement.setString(1, iban);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int recordCount = resultSet.getInt("recordCount");
            return recordCount > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void transferMoney(String fromAccountIban, String toAccountIban, double newFromAccountBalance, double newToAccountBalance) {
        try {
            transferMoneyTransactional(fromAccountIban, toAccountIban, newFromAccountBalance, newToAccountBalance);
        } catch (SQLException e) {
            throw new DAOException();
        }
    }

    private void transferMoneyTransactional(String fromAccountIban, String toAccountIban, double newFromAccountBalance,
                                            double newToAccountBalance) throws SQLException {
        try {
            connectionHolder.getConnection().setAutoCommit(false);
            updateBalance(fromAccountIban, newFromAccountBalance);
            updateBalance(toAccountIban, newToAccountBalance);
            connectionHolder.getConnection().commit();
        } catch (Exception e) {
            connectionHolder.getConnection().rollback();
        } finally {
            connectionHolder.getConnection().setAutoCommit(true);
        }
    }

    private void updateBalance(String iban, double newBalance) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("UPDATE accounts SET balance=? WHERE iban=?")) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, iban);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }
}
