package edu.bank.dao.impl;

import edu.bank.dao.LegalEntityRepository;
import edu.bank.entity.LegalEntity;
import edu.bank.exeption.UnexpectedInternalError;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LegalEntityRepositoryImpl extends BaseRepository implements LegalEntityRepository {

    @Override
    public LegalEntity create(LegalEntity legalEntity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (first_name," +
                " phone) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, legalEntity.getName());
            preparedStatement.setString(2, legalEntity.getPhone());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) legalEntity.setId(resultSet.getLong(1));
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
        return legalEntity;
    }

    @Override
    public LegalEntity get(long id) {
        return getByAnyParamIfPresent("SELECT * FROM users WHERE id=?", id);
    }

    @Override
    public LegalEntity getByPhone(String phone) {
        return getByAnyParamIfPresent("SELECT * FROM users WHERE phone=?", phone);
    }

    private LegalEntity getByAnyParamIfPresent(String sql, Object criteria) {
        LegalEntity legalEntity = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (criteria instanceof Long) preparedStatement.setLong(1, (long) criteria);
            if (criteria instanceof String) preparedStatement.setString(1, (String) criteria);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String lastName = resultSet.getString("last_name");
            String patronymic = resultSet.getString("patronymic");
            if (lastName != null || patronymic != null) return null;
            legalEntity = new LegalEntity();
            legalEntity.setId(resultSet.getLong("id"));
            legalEntity.setName(resultSet.getString("first_name"));
            legalEntity.setPhone(resultSet.getString("phone"));
            return legalEntity;
        } catch (Exception e) {
            throw new UnexpectedInternalError();
        }
    }
}
