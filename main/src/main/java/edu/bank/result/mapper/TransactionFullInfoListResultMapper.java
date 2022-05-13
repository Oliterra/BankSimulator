package edu.bank.result.mapper;

import edu.bank.model.dto.TransactionFullInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.CommandResultMapper;

import java.util.List;

public class TransactionFullInfoListResultMapper implements CommandResultMapper<List<TransactionFullInfo>> {

    @Override
    public String mapResult(List<TransactionFullInfo> transactionFullInfoList) {
        if (transactionFullInfoList.isEmpty())
            return ConsoleColor.YELLOW_BOLD + "No transactions found" + ConsoleColor.RESET;
        else {
            TransactionFullInfoResultMapper transactionFullInfoResultMapper = new TransactionFullInfoResultMapper();
            StringBuilder resultString = new StringBuilder();
            resultString.append(ConsoleColor.YELLOW_BOLD + "List of all user transactions:");
            transactionFullInfoList.forEach(i -> resultString.append(transactionFullInfoResultMapper.mapResult(i)));
            return resultString.toString();
        }
    }
}
