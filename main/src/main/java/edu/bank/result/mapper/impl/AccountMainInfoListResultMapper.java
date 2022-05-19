package edu.bank.result.mapper.impl;

import edu.bank.model.dto.AccountMainInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.mapper.CommandResultMapper;

import java.util.List;

public class AccountMainInfoListResultMapper implements CommandResultMapper<List<AccountMainInfo>> {

    @Override
    public String mapResult(List<AccountMainInfo> accountMainInfos) {
        if (accountMainInfos.isEmpty())
            return ConsoleColor.YELLOW_BOLD + "No banks found" + ConsoleColor.RESET;
        else {
            AccountMainInfoResultMapper accountMainInfoResultMapper = new AccountMainInfoResultMapper();
            StringBuilder resultString = new StringBuilder();
            resultString.append(ConsoleColor.YELLOW_BOLD + "List of all accounts:");
            accountMainInfos.forEach(i -> resultString.append(accountMainInfoResultMapper.mapResult(i)));
            return resultString.toString();
        }
    }
}
