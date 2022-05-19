package edu.bank.result.mapper.impl;

import edu.bank.model.dto.LegalEntityFullInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.mapper.CommandResultMapper;

public class LegalEntityFullInfoResultMapper implements CommandResultMapper<LegalEntityFullInfo> {

    @Override
    public String mapResult(LegalEntityFullInfo legalEntityFullInfo) {
        return ConsoleColor.YELLOW_BOLD + "\nFull information about the legal entity:"
                + ConsoleColor.MAGENTA_BOLD + "\nID: " + ConsoleColor.RESET + legalEntityFullInfo.getId()
                + ConsoleColor.MAGENTA_BOLD + "\nFirst name: " + ConsoleColor.RESET + legalEntityFullInfo.getName()
                + ConsoleColor.MAGENTA_BOLD + "\nPhone: " + ConsoleColor.RESET + legalEntityFullInfo.getPhone()
                + ConsoleColor.MAGENTA_BOLD + "\nAll accounts: " + ConsoleColor.RESET + legalEntityFullInfo.getAccounts();
    }
}
