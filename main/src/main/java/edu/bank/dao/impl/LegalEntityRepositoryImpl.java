package edu.bank.dao.impl;

import edu.bank.dao.LegalEntityRepository;
import edu.bank.exeption.DAOException;
import edu.bank.model.entity.LegalEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            throw new DAOException();
        }
        return legalEntity;
    }

    @Override
    public List<LegalEntity> getAll() {
        List<LegalEntity> legalEntities = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LegalEntity legalEntity = doGetMapping(resultSet);
                if (legalEntity != null) legalEntities.add(legalEntity);
            }
            return legalEntities;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public LegalEntity get(long id) {
        return getByAnyParamIfPresent("SELECT * FROM users WHERE id=?", id);
    }

    @Override
    public LegalEntity getByPhone(String phone) {
        return getByAnyParamIfPresent("SELECT * FROM users WHERE phone=?", phone);
    }

    @Override
    public void update(long id, LegalEntity legalEntity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET first_name=?, " +
                "phone=? WHERE id=?")) {
            preparedStatement.setString(1, legalEntity.getName());
            preparedStatement.setString(2, legalEntity.getPhone());
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    private LegalEntity getByAnyParamIfPresent(String sql, Object criteria) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            if (criteria instanceof Long) preparedStatement.setLong(1, (long) criteria);
            if (criteria instanceof String) preparedStatement.setString(1, (String) criteria);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return doGetMapping(resultSet);
        } catch (Exception e) {
            return null;
        }
    }

    private LegalEntity doGetMapping(ResultSet resultSet) {
        try {
            LegalEntity legalEntity = null;
            if (identifyLegaEntity(resultSet)) {
                legalEntity = new LegalEntity();
                legalEntity.setId(resultSet.getLong("id"));
                legalEntity.setName(resultSet.getString("first_name"));
                legalEntity.setPhone(resultSet.getString("phone"));
            }
            return legalEntity;
        } catch (Exception e) {
            throw new DAOException();
        }
    }

    private boolean identifyLegaEntity(ResultSet resultSet) throws SQLException {
        return resultSet.getString("last_name") == null || resultSet.getString("patronymic") == null;
    }
}
