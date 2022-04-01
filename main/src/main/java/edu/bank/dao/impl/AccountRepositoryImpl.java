package edu.bank.dao.impl;

import edu.bank.dao.AccountRepository;
import edu.bank.dao.UserRepository;
import edu.bank.model.enm.Currency;
import edu.bank.model.entity.Account;
import edu.bank.exeption.UnexpectedInternalError;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccountRepositoryImpl extends BaseRepository implements AccountRepository {

    private final UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public void create(Account account) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO accounts(iban, " +
                "user_id, currency, balance, registration_date) VALUES(?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, account.getIban());
            preparedStatement.setLong(2, account.getUser().getId());
            preparedStatement.setString(3, account.getCurrency().toString());
            preparedStatement.setDouble(4, account.getBalance());
            preparedStatement.setDate(5, Date.valueOf(account.getRegistrationDate()));
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
    }

    @Override
    public Account get(String iban) {
        Account account = new Account();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE iban=?")) {
            preparedStatement.setString(1, iban);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            account.setIban(resultSet.getString("iban"));
            account.setUser(userRepository.get(resultSet.getLong("user_id")));
            account.setCurrency(Currency.valueOf(resultSet.getString("currency")));
            account.setBalance(resultSet.getDouble("balance"));
            account.setRegistrationDate(resultSet.getDate("registration_date").toLocalDate());
            return account;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                account.setIban(resultSet.getString("iban"));
                account.setUser(userRepository.get(resultSet.getLong("user_id")));
                account.setCurrency(Currency.valueOf(resultSet.getString("currency")));
                account.setBalance(resultSet.getDouble("balance"));
                Date registrationDate = resultSet.getDate("registration_date");
                LocalDate localRegistrationDate = Instant.ofEpochMilli(registrationDate.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                account.setRegistrationDate(localRegistrationDate);
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
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE user_id=?")) {
            preparedStatement.setLong(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                account.setIban(resultSet.getString("iban"));
                account.setUser(userRepository.get(resultSet.getLong("user_id")));
                account.setCurrency(Currency.valueOf(resultSet.getString("currency")));
                account.setBalance(resultSet.getDouble("balance"));
                Date registrationDate = resultSet.getDate("registration_date");
                LocalDate localRegistrationDate = Instant.ofEpochMilli(registrationDate.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                account.setRegistrationDate(localRegistrationDate);
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
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE user_id=? AND currency=?")) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setString(2, currency.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Account account = new Account();
                account.setIban(resultSet.getString("iban"));
                account.setUser(userRepository.get(resultSet.getLong("user_id")));
                account.setCurrency(Currency.valueOf(resultSet.getString("currency")));
                account.setBalance(resultSet.getDouble("balance"));
                Date registrationDate = resultSet.getDate("registration_date");
                LocalDate localRegistrationDate = Instant.ofEpochMilli(registrationDate.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                account.setRegistrationDate(localRegistrationDate);
                accounts.add(account);
            }
            return accounts;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public double getBalanceByIban(String iban) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT balance FROM accounts WHERE iban=?")) {
            preparedStatement.setString(1, iban);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getDouble("balance");
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void transferMoney(String fromAccountIban, String toAccountIban, double newFromAccountBalance, double newToAccountBalance) {
        try {
            try {
                connection.setAutoCommit(false);
                updateBalance(fromAccountIban, newFromAccountBalance);
                updateBalance(toAccountIban, newToAccountBalance);
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new UnexpectedInternalError();
        }
    }

    private void updateBalance(String iban, double newBalance) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE accounts SET balance=? WHERE iban=?")) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, iban);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
    }
}
