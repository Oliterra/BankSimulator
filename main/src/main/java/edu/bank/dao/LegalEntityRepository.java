package edu.bank.dao;

import edu.bank.model.entity.LegalEntity;

import java.util.List;

public interface LegalEntityRepository {

    LegalEntity create(LegalEntity legalEntity);

    List<LegalEntity> getAll();

    LegalEntity get(long id);

    LegalEntity getByPhone(String phone);

    void update(long id, LegalEntity legalEntity);
}
