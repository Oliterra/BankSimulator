package edu.bank.result.mapper.impl;

import edu.bank.model.dto.BankFullInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.mapper.CommandResultMapper;

public class BankFullInfoResultMapper implements CommandResultMapper<BankFullInfo> {

    @Override
    public String mapResult(BankFullInfo bankDTO) {
        return ConsoleColor.YELLOW_BOLD + "\nFull information about the bank:"
                + ConsoleColor.MAGENTA_BOLD + "\nID: " + ConsoleColor.RESET + bankDTO.getId()
                + ConsoleColor.MAGENTA_BOLD + "\nName: " + ConsoleColor.RESET + bankDTO.getName()
                + ConsoleColor.MAGENTA_BOLD + "\nIBAN prefix: " + ConsoleColor.RESET + bankDTO.getIbanPrefix()
                + ConsoleColor.MAGENTA_BOLD + "\nFee for individuals: " + ConsoleColor.RESET + bankDTO.getIndividualsFee()
                + ConsoleColor.MAGENTA_BOLD + "\nCount of users: " + ConsoleColor.RESET + bankDTO.getUsersCount();
    }
}
