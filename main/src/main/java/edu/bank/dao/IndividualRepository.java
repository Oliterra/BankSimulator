package edu.bank.dao;

import edu.bank.exeption.DAOException;
import edu.bank.model.entity.Individual;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IndividualRepository {
    private final ConnectionHolder connectionHolder;

    public Individual create(Individual individual) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("INSERT INTO users (first_name, " +
                "last_name, patronymic, phone) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, individual.getName());
            preparedStatement.setString(2, individual.getLastName());
            preparedStatement.setString(3, individual.getPatronymic());
            preparedStatement.setString(4, individual.getPhone());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) individual.setId(resultSet.getLong(1));
        } catch (Exception e) {
            throw new DAOException();
        }
        return individual;
    }

    public List<Individual> getAll() {
        List<Individual> individuals = new ArrayList<>();
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("SELECT * FROM users")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Individual individual = doGetMapping(resultSet);
                if (individual != null) individuals.add(individual);
            }
            return individuals;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Individual get(long id) {
        return getByAnyParamIfPresent("SELECT * FROM users WHERE id=?", id);
    }

    public Individual getByPhone(String phone) {
        return getByAnyParamIfPresent("SELECT * FROM users WHERE phone=?", phone);
    }

    public void update(long id, Individual individual) {
        try (PreparedStatement preparedStatement = connectionHolder.getConnection().prepareStatement("UPDATE users SET first_name=?, " +
                "last_name=?, patronymic=?, phone=? WHERE id=?")) {
            preparedStatement.setString(1, individual.getName());
            preparedStatement.setString(2, individual.getLastName());
            preparedStatement.setString(3, individual.getPatronymic());
            preparedStatement.setString(4, individual.getPhone());
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    private Individual getByAnyParamIfPresent(String sql, Object criteria) {
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

    private Individual doGetMapping(ResultSet resultSet) {
        try {
            Individual individual = null;
            if (identifyIndividual(resultSet)) {
                individual = new Individual();
                individual.setId(resultSet.getLong("id"));
                individual.setName(resultSet.getString("first_name"));
                individual.setLastName(resultSet.getString("last_name"));
                individual.setPatronymic(resultSet.getString("patronymic"));
                individual.setPhone(resultSet.getString("phone"));
            }
            return individual;
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    private boolean identifyIndividual(ResultSet resultSet) throws SQLException {
        return resultSet.getString("last_name") != null || resultSet.getString("patronymic") != null;
    }
}
