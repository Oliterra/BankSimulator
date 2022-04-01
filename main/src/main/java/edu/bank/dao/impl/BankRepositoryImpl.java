package edu.bank.dao.impl;

import edu.bank.dao.BankRepository;
import edu.bank.entity.Bank;
import edu.bank.exeption.UnexpectedInternalError;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BankRepositoryImpl extends BaseRepository implements BankRepository {

    @Override
    public void create(Bank bank) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO banks(name, " +
                "iban_prefix, individuals_fee, legal_entities_fee) VALUES(?, ?, ?, ?)")) {
            preparedStatement.setString(1, bank.getName());
            preparedStatement.setString(2, bank.getIbanPrefix());
            preparedStatement.setDouble(3, bank.getIndividualsFee());
            preparedStatement.setDouble(4, bank.getLegalEntitiesFee());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new UnexpectedInternalError();
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
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT iban_prefix FROM banks WHERE id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("iban_prefix");
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
    }

    @Override
    public void update(long id, Bank bank) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE banks SET name=?, " +
                "iban_prefix=?, individuals_fee=?, legal_entities_fee=? WHERE id=?")) {
            preparedStatement.setString(1, bank.getName());
            preparedStatement.setString(2, bank.getIbanPrefix());
            preparedStatement.setDouble(3, bank.getIndividualsFee());
            preparedStatement.setDouble(4, bank.getLegalEntitiesFee());
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
    }

    private Bank getByAnyParamIfPresent(String sql, Object criteria) {
        Bank bank = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (criteria instanceof Long) preparedStatement.setLong(1, (long) criteria);
            if (criteria instanceof String) preparedStatement.setString(1, (String) criteria);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            bank = new Bank();
            bank.setId(resultSet.getLong("id"));
            bank.setName(resultSet.getString("name"));
            bank.setIbanPrefix(resultSet.getString("iban_prefix"));
            bank.setIndividualsFee(resultSet.getDouble("individuals_fee"));
            bank.setLegalEntitiesFee(resultSet.getDouble("legal_entities_fee"));
            return bank;
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
    }
}
