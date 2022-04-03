package edu.bank.service;

import java.util.Map;

public interface BankService {

    void createBank(Map<String, String> bankInfo);

    void getAllBanks(Map<String, String> bankInfo);

    void getBank(Map<String, String> bankInfo);

    void updateBank(Map<String, String> bankInfo);

    void deleteBank(Map<String, String> bankInfo);

    void addExistingUser(Map<String, String> info);
}
