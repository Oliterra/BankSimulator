package edu.bank.dao.impl;

import edu.bank.dao.IndividualRepository;
import edu.bank.dao.LegalEntityRepository;
import edu.bank.dao.UserRepository;
import edu.bank.exeption.DAOException;
import edu.bank.model.entity.Individual;
import edu.bank.model.entity.LegalEntity;
import edu.bank.model.entity.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                if (createdUser == null || bankId == 0) throw new DAOException();
                createBankUser(bankId, createdUser.getId());
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (Exception e) {
            throw new DAOException();
        }
        return (T) createdUser;
    }

    @Override
    public void createBankUser(long bankId, long userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO banks_users (bank_id," +
                " user_id) VALUES(?, ?)")) {
            preparedStatement.setLong(1, bankId);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    @Override
    public List<User> getAllByTheBank(long bankId) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT user_id FROM banks_users WHERE bank_id=?")) {
            preparedStatement.setLong(1, bankId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("user_id");
                users.add(get(id));
            }
            return users;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public User get(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return doGetMapping(resultSet);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void update(boolean isUserIndividual, long id, User user) {
        if (isUserIndividual) individualRepository.update(id, (Individual) user);
        else legalEntityRepository.update(id, (LegalEntity) user);
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
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS recordCount " +
                "FROM users WHERE id=?")) {
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
    public boolean isIndividual(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("last_name") != null && resultSet.getString("patronymic") != null;
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    @Override
    public boolean isBankClient(long bankId, long userId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS recordCount " +
                "FROM banks_users WHERE bank_id=? AND user_id=?")) {
            preparedStatement.setLong(1, bankId);
            preparedStatement.setLong(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int recordCount = resultSet.getInt("recordCount");
            return recordCount > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getBanksCount(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) AS recordCount " +
                "FROM banks_users WHERE user_id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("recordCount");
        } catch (Exception e) {
            return 0;
        }
    }

    private void deleteCascade(long id) throws SQLException {
        try {
            connection.setAutoCommit(false);
            deleteUserFromBanks(id);
            deleteUserFromUsers(id);
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private void deleteUserFromUsers(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id=?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    private void deleteUserFromBanks(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM banks_users WHERE user_id=?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    private User doGetMapping(ResultSet resultSet) {
        try {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("first_name"));
            user.setPhone(resultSet.getString("phone"));
            return user;
        } catch (Exception e) {
            throw new DAOException();
        }
    }
}
