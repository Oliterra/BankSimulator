package edu.bank.dao;

import edu.bank.model.entity.LegalEntity;

public interface LegalEntityRepository {

    LegalEntity create(LegalEntity legalEntity);

    LegalEntity get(long id);

    LegalEntity getByPhone(String phone);
}
