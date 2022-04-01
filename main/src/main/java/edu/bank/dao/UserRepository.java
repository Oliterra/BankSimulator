package edu.bank.dao;

import edu.bank.model.entity.User;

public interface UserRepository {

    <T extends User> T create(long bankId, T user);

    User get(long id);

    boolean isIndividual(long id);

    boolean isUserBankClient(long bankId, long userId);
}
