package edu.bank.dao.impl;

import edu.bank.dao.BankRepository;
import edu.bank.exeption.DAOException;
import edu.bank.model.entity.Bank;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BankRepositoryImpl implements BankRepository {

    private final ConnectionHolder connectionHolder;
    @Override
    public void create(Bank bank) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("INSERT INTO banks(name, " +
                "iban_prefix, individuals_fee, legal_entities_fee) VALUES(?, ?, ?, ?)")) {
            preparedStatement.setString(1, bank.getName());
            preparedStatement.setString(2, bank.getIbanPrefix());
            preparedStatement.setDouble(3, bank.getIndividualsFee());
            preparedStatement.setDouble(4, bank.getLegalEntitiesFee());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    @Override
    public List<Bank> getAll() {
        List<Bank> banks = new ArrayList<>();
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM banks")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                banks.add(doGetMapping(resultSet));
            }
            return banks;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Bank get(long id) {
        return getByAnyParamIfPresent("SELECT * FROM banks WHERE id=?", id);
    }

    @Override
    public Bank getByName(String name) {
        return getByAnyParamIfPresent("SELECT * FROM banks WHERE name=?", name);
    }

    @Override
    public Bank getByIbanPrefix(String ibanPrefix) {
        return getByAnyParamIfPresent("SELECT * FROM banks WHERE iban_prefix=?", ibanPrefix);
    }

    @Override
    public String getIbanPrefixById(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT iban_prefix FROM banks WHERE id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("iban_prefix");
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    @Override
    public void update(long id, Bank bank) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("UPDATE banks SET name=?, " +
                "iban_prefix=?, individuals_fee=?, legal_entities_fee=? WHERE id=?")) {
            preparedStatement.setString(1, bank.getName());
            preparedStatement.setString(2, bank.getIbanPrefix());
            preparedStatement.setDouble(3, bank.getIndividualsFee());
            preparedStatement.setDouble(4, bank.getLegalEntitiesFee());
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    @Override
    public void delete(long id) {
        try {
            deleteCascade(id);
        } catch (SQLException e) {
            throw new DAOException();
        }
    }

    @Override
    public boolean isExists(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT COUNT(*) AS recordCount " +
                "FROM banks WHERE id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int recordCount = resultSet.getInt("recordCount");
            return recordCount > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getUsersCount(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT COUNT(*) AS recordCount " +
                "FROM banks_users WHERE bank_id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("recordCount");
        } catch (Exception e) {
            return 0;
        }
    }

    private Bank getByAnyParamIfPresent(String sql, Object criteria) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement(sql)) {
            if (criteria instanceof Long) preparedStatement.setLong(1, (long) criteria);
            if (criteria instanceof String) preparedStatement.setString(1, (String) criteria);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return doGetMapping(resultSet);
        } catch (Exception e) {
            return null;
        }
    }

    private void deleteCascade(long id) throws SQLException {
        try {
            connectionHolder.getConnection().setAutoCommit(false);
            deleteBankUsers(id);
            deleteBankFromBanks(id);
            connectionHolder.getConnection().commit();
        } catch (Exception e) {
            connectionHolder.getConnection().rollback();
        } finally {
            connectionHolder.getConnection().setAutoCommit(true);
        }
    }

    private void deleteBankUsers(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("DELETE FROM banks_users WHERE bank_id=?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    private void deleteBankFromBanks(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("DELETE FROM banks WHERE id=?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    private Bank doGetMapping(ResultSet resultSet) {
        try {
            Bank bank = new Bank();
            bank.setId(resultSet.getLong("id"));
            bank.setName(resultSet.getString("name"));
            bank.setIbanPrefix(resultSet.getString("iban_prefix"));
            bank.setIndividualsFee(resultSet.getDouble("individuals_fee"));
            bank.setLegalEntitiesFee(resultSet.getDouble("legal_entities_fee"));
            return bank;
        } catch (Exception e) {
            throw new DAOException();
        }
    }
}
