package edu.bank.service;

import edu.bank.command.model.CommandParam;
import edu.bank.model.dto.AccountMainInfoDTO;
import edu.bank.model.entity.Account;

import java.util.List;
import java.util.Set;

public interface AccountService {

    void createNewAccount(Set<CommandParam> accountInfo);

    void getAllByUser(Set<CommandParam> info);

    List<AccountMainInfoDTO> getAllByUser(long id);

    Account getDefaultAccountForNewUser(long bankId, long userId);

    void deleteAccount(String iban);

    void deleteAllUserAccountsOfSpecificBank(long userId, long bankId);

    void transferMoney(Set<CommandParam> transferInfo);
}
