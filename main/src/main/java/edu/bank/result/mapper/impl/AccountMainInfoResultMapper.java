package edu.bank.result.mapper.impl;

import edu.bank.model.dto.AccountMainInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.mapper.CommandResultMapper;

public class AccountMainInfoResultMapper implements CommandResultMapper<AccountMainInfo> {

    @Override
    public String mapResult(AccountMainInfo accountMainInfo) {
        return ConsoleColor.YELLOW_BOLD + "\nFull account info:"
                + ConsoleColor.MAGENTA_BOLD + "\nIBAN: " + ConsoleColor.RESET + accountMainInfo.getIban()
                + ConsoleColor.MAGENTA_BOLD + "\nBank: " + ConsoleColor.RESET + accountMainInfo.getBankName()
                + ConsoleColor.MAGENTA_BOLD + "\nCurrency: " + ConsoleColor.RESET + accountMainInfo.getCurrency()
                + ConsoleColor.MAGENTA_BOLD + "\nBalance: " + ConsoleColor.RESET + accountMainInfo.getBalance();
    }
}
