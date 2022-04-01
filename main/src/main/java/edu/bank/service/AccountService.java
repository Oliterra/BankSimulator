package edu.bank.service;

import edu.bank.entity.Account;

import java.io.IOException;
import java.util.Map;

public interface AccountService {

    Account getDefaultAccountForNewUser(long bankId, long userId) throws IOException;

    void getAllByUser(Map<String, String> info) throws IOException;

    void transferMoney(Map<String, String> transferInfo) throws IOException;
}
