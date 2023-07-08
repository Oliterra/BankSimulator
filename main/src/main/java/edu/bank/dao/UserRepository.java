package edu.bank.dao;

import edu.bank.exeption.DAOException;
import edu.bank.model.entity.Individual;
import edu.bank.model.entity.LegalEntity;
import edu.bank.model.entity.User;
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
public class UserRepository {

    private final ConnectionHolder connectionHolder;
    private final IndividualRepository individualRepository;
    private final LegalEntityRepository legalEntityRepository;

    public <T extends User> T create(long bankId, T user) {
        User createdUser = null;
        try {
            try {
                connectionHolder.getConnection().setAutoCommit(false);
                if (user instanceof Individual) createdUser = individualRepository.create((Individual) user);
                if (user instanceof LegalEntity) createdUser = legalEntityRepository.create((LegalEntity) user);
                if (createdUser == null || bankId == 0) throw new DAOException();
                createBankUser(bankId, createdUser.getId());
                connectionHolder.getConnection().commit();
            } catch (Exception e) {
                connectionHolder.getConnection().rollback();
            } finally {
                connectionHolder.getConnection().setAutoCommit(true);
            }
        } catch (Exception e) {
            throw new DAOException();
        }
        return (T) createdUser;
    }

    public void createBankUser(long bankId, long userId) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("INSERT INTO banks_users (bank_id," +
                " user_id) VALUES(?, ?)")) {
            preparedStatement.setLong(1, bankId);
            preparedStatement.setLong(2, userId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    public List<User> getAllByTheBank(long bankId) {
        List<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT user_id FROM banks_users WHERE bank_id=?")) {
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

    public User get(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM users WHERE id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return doGetMapping(resultSet);
        } catch (Exception e) {
            return null;
        }
    }

    public void update(boolean isUserIndividual, long id, User user) {
        if (isUserIndividual) individualRepository.update(id, (Individual) user);
        else legalEntityRepository.update(id, (LegalEntity) user);
    }

    public void delete(long id) {
        try {
            deleteCascade(id);
        } catch (SQLException e) {
            throw new DAOException();
        }
    }

    public boolean isExists(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT COUNT(*) AS recordCount " +
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

    public boolean isIndividual(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM users WHERE id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getString("last_name") != null && resultSet.getString("patronymic") != null;
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    public boolean isBankClient(long bankId, long userId) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT COUNT(*) AS recordCount " +
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

    public int getBanksCount(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT COUNT(*) AS recordCount " +
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
            connectionHolder.getConnection().setAutoCommit(false);
            deleteUserFromBanks(id);
            deleteUserFromUsers(id);
            connectionHolder.getConnection().commit();
        } catch (Exception e) {
            connectionHolder.getConnection().rollback();
        } finally {
            connectionHolder.getConnection().setAutoCommit(true);
        }
    }

    private void deleteUserFromUsers(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("DELETE FROM users WHERE id=?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    private void deleteUserFromBanks(long id) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("DELETE FROM banks_users WHERE user_id=?")) {
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
