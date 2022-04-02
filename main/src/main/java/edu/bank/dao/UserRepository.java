package edu.bank.dao;

import edu.bank.model.entity.User;

public interface UserRepository {

    <T extends User> T create(long bankId, T user);

    User get(long id);

    void update(boolean isUserIndividual, long id, User user);

    void delete(long id);

    boolean isUserIndividual(long id);

    boolean isUserBankClient(long bankId, long userId);
}
