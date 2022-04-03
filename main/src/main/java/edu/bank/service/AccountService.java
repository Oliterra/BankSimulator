package edu.bank.service;

import edu.bank.model.dto.AccountMainInfoDTO;
import edu.bank.model.entity.Account;

import java.util.List;
import java.util.Map;

public interface AccountService {

    void createNewAccount(Map<String, String> accountInfo);

    void getAllByUser(Map<String, String> info);

    List<AccountMainInfoDTO> getAllByUser(long id);

    Account getDefaultAccountForNewUser(long bankId, long userId);

    void deleteAccount(String iban);

    void deleteAllUserAccountsOfSpecificBank(long userId, long bankId);

    void transferMoney(Map<String, String> transferInfo);
}
