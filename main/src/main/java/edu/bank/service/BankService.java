package edu.bank.service;

import edu.bank.command.model.CommandParam;

import java.util.Set;

public interface BankService {

    void createBank(Set<CommandParam> bankInfo);

    void getAllBanks(Set<CommandParam> bankInfo);

    void getBank(Set<CommandParam> bankInfo);

    void updateBank(Set<CommandParam> bankInfo);

    void deleteBank(Set<CommandParam> bankInfo);

    void addExistingUser(Set<CommandParam> info);
}
