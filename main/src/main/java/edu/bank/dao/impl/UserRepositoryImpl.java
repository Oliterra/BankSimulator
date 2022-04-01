package edu.bank.dao.impl;

import edu.bank.dao.IndividualRepository;
import edu.bank.dao.LegalEntityRepository;
import edu.bank.dao.UserRepository;
import edu.bank.model.entity.Individual;
import edu.bank.model.entity.LegalEntity;
import edu.bank.model.entity.User;
import edu.bank.exeption.UnexpectedInternalError;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepositoryImpl extends BaseRepository implements UserRepository {

    private final IndividualRepository individualRepository = new IndividualRepositoryImpl();
    private final LegalEntityRepository legalEntityRepository = new LegalEntityRepositoryImpl();

    @Override
    public <T extends User> T create(long bankId, T user) {
        User createdUser = null;
        try {
            try {
                connection.setAutoCommit(false);
                if (user instanceof Individual) createdUser = individualRepository.create((Individual) user);
                if (user instanceof LegalEntity) createdUser = legalEntityRepository.create((LegalEntity) user);
                createBankUser(bankId, createdUser.getId());
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
        return (T) createdUser;
    }

    @Override
    public User get(long id) {
        User user = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            user = new User();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("first_name"));
            user.setPhone(resultSet.getString("phone"));
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isIndividual(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.getString("last_name") != null &&
                    resultSet.getString("patronymic") != null) return true;
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
        return false;
    }

    @Override
    public boolean isUserBankClient(long bankId, long userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS recordCount " +
                "FROM banks_users WHERE bank_id=? AND user_id=?")) {
            preparedStatement.setLong(1, bankId);
            preparedStatement.setLong(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int recordCount = resultSet.getInt("recordCount");
            if (recordCount > 0) return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void createBankUser(long bankId, long userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO banks_users (bank_id," +
                " user_id) VALUES(?, ?)")) {
            preparedStatement.setLong(1, bankId);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
    }
}
