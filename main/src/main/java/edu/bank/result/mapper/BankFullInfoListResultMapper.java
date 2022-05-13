package edu.bank.result.mapper;

import edu.bank.model.dto.BankFullInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.CommandResultMapper;

import java.util.List;

public class BankFullInfoListResultMapper implements CommandResultMapper<List<BankFullInfo>> {

    @Override
    public String mapResult(List<BankFullInfo> bankFullInfos) {
        if (bankFullInfos.isEmpty())
            return ConsoleColor.YELLOW_BOLD + "No banks found" + ConsoleColor.RESET;
        else {
            BankFullInfoResultMapper bankFullInfoResultMapper = new BankFullInfoResultMapper();
            StringBuilder resultString = new StringBuilder();
            resultString.append(ConsoleColor.YELLOW_BOLD + "List of all banks:");
            bankFullInfos.forEach(i -> resultString.append(bankFullInfoResultMapper.mapResult(i)));
            return resultString.toString();
        }
    }
}
