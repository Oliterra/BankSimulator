package edu.bank.dao;

import edu.bank.model.entity.User;

import java.util.List;

public interface UserRepository {

    <T extends User> T create(long bankId, T user);

    void createBankUser(long bankId, long userId);

    List<User> getAllByTheBank(long bankId);

    User get(long id);

    void update(boolean isUserIndividual, long id, User user);

    void delete(long id);

    boolean isExists(long id);

    boolean isIndividual(long id);

    boolean isBankClient(long bankId, long userId);

    int getBanksCount(long id);
}
