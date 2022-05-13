package edu.bank.result.mapper;

import edu.bank.model.dto.IndividualFullInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.CommandResultMapper;

public class IndividualFullInfoResultMapper implements CommandResultMapper<IndividualFullInfo> {

    @Override
    public String mapResult(IndividualFullInfo individualFullInfo) {
        return ConsoleColor.YELLOW_BOLD + "\nFull information about the individual:"
                + ConsoleColor.MAGENTA_BOLD + "\nID: " + ConsoleColor.RESET + individualFullInfo.getId()
                + ConsoleColor.MAGENTA_BOLD + "\nFirst name: " + ConsoleColor.RESET + individualFullInfo.getName()
                + ConsoleColor.MAGENTA_BOLD + "\nLast name: " + ConsoleColor.RESET + individualFullInfo.getLastName()
                + ConsoleColor.MAGENTA_BOLD + "\nPatronymic: " + ConsoleColor.RESET + individualFullInfo.getPatronymic()
                + ConsoleColor.MAGENTA_BOLD + "\nPhone: " + ConsoleColor.RESET + individualFullInfo.getPhone()
                + ConsoleColor.MAGENTA_BOLD + "\nAll accounts: " + ConsoleColor.RESET + individualFullInfo.getAccounts();
    }
}
