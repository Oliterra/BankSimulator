package edu.bank.dao.impl;

import edu.bank.dao.IndividualRepository;
import edu.bank.entity.Individual;
import edu.bank.exeption.UnexpectedInternalError;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class IndividualRepositoryImpl extends BaseRepository implements IndividualRepository {

    @Override
    public Individual create(Individual individual) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (first_name, " +
                "last_name, patronymic, phone) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, individual.getName());
            preparedStatement.setString(2, individual.getLastName());
            preparedStatement.setString(3, individual.getPatronymic());
            preparedStatement.setString(4, individual.getPhone());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) individual.setId(resultSet.getLong(1));
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
        return individual;
    }

    @Override
    public Individual get(long id) {
        return getByAnyParamIfPresent("SELECT * FROM users WHERE id=?", id);
    }

    @Override
    public Individual getByPhone(String phone) {
        return getByAnyParamIfPresent("SELECT * FROM users WHERE phone=?", phone);
    }


    private Individual getByAnyParamIfPresent(String sql, Object criteria) {
        Individual individual = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (criteria instanceof Long) preparedStatement.setLong(1, (long) criteria);
            if (criteria instanceof String) preparedStatement.setString(1, (String) criteria);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String lastName = resultSet.getString("last_name");
            String patronymic = resultSet.getString("patronymic");
            if (lastName == null || patronymic == null) return null;
            individual = new Individual();
            individual.setId(resultSet.getLong("id"));
            individual.setName(resultSet.getString("first_name"));
            individual.setLastName(lastName);
            individual.setPatronymic(patronymic);
            individual.setPhone(resultSet.getString("phone"));
            return individual;
        } catch (Exception e) {
            return null;
        }
    }
}
