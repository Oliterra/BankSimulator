package edu.bank.service;

import java.io.IOException;
import java.util.Map;

public interface BankService {

    void createBank(Map<String, String> bankInfo) throws IOException;

    void updateBank(Map<String, String> newBankInfo) throws IOException;
}
