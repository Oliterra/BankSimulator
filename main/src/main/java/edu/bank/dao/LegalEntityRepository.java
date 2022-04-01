package edu.bank.dao;

import edu.bank.entity.LegalEntity;

public interface LegalEntityRepository {

    LegalEntity create(LegalEntity legalEntity);

    LegalEntity get(long id);

    LegalEntity getByPhone(String phone);
}
