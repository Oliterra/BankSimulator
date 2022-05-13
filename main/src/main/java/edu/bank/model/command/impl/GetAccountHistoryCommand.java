package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.TransactionFullInfo;
import edu.bank.model.dto.TransactionHistoryInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.TransactionFullInfoListResultMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class GetAccountHistoryCommand extends BaseCommand {

    @Override
    public CommandResult<List<TransactionFullInfo>> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        TransactionHistoryInfo transactionHistoryInfo = getTransactionHistoryInfo(commandDescription);
        CommandResult<List<TransactionFullInfo>> commandResult = super.executeAnyCommand(commandDescription, transactionHistoryInfo);
        commandResult.setCommandResultMapper(new TransactionFullInfoListResultMapper());
        return commandResult;
    }

    private TransactionHistoryInfo getTransactionHistoryInfo(CommandDescription commandDescription) {
        String accountIban = getParamValueByNameOrReturnNull(commandDescription, "accountIban");
        String fromDateString = getParamValueByNameOrReturnNull(commandDescription, "fromDate");
        LocalDate fromDate = null;
        if (fromDateString != null) fromDate = LocalDate.parse(fromDateString);
        String toDateString = getParamValueByNameOrReturnNull(commandDescription, "toDate");
        LocalDate toDate = null;
        if (toDateString != null) toDate = LocalDate.parse(toDateString);
        TransactionHistoryInfo transactionHistoryInfo = new TransactionHistoryInfo();
        transactionHistoryInfo.setAccountIban(accountIban);
        transactionHistoryInfo.setFromDate(fromDate);
        transactionHistoryInfo.setToDate(toDate);
        return transactionHistoryInfo;
    }
}
