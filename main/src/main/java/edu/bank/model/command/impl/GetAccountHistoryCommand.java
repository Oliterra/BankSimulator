package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.result.mapper.TransactionFullInfoDTOListResultMapper;
import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandExecutionInfo;
import edu.bank.model.command.CommandInfo;
import edu.bank.model.dto.TransactionFullInfoDTO;
import edu.bank.model.dto.TransactionHistoryInfoDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;

@Component
public class GetAccountHistoryCommand extends BaseCommand<TransactionHistoryInfoDTO, List<TransactionFullInfoDTO>> {

    @Override
    public CommandExecutionInfo<TransactionHistoryInfoDTO> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        String accountIban = getParamValueByNameOrReturnNull(commandDescription, "accountIban");
        String fromDateString = getParamValueByNameOrReturnNull(commandDescription, "fromDate");
        LocalDate fromDate = null;
        if (fromDateString != null) fromDate = LocalDate.parse(fromDateString);
        String toDateString = getParamValueByNameOrReturnNull(commandDescription, "toDate");
        LocalDate toDate = null;
        if (toDateString != null) toDate = LocalDate.parse(toDateString);
        CommandExecutionInfo<TransactionHistoryInfoDTO> commandExecutionInfo = new CommandExecutionInfo<>();
        TransactionHistoryInfoDTO transactionHistoryInfoDTO = new TransactionHistoryInfoDTO();
        transactionHistoryInfoDTO.setAccountIban(accountIban);
        transactionHistoryInfoDTO.setFromDate(fromDate);
        transactionHistoryInfoDTO.setToDate(toDate);
        commandExecutionInfo.setMethodParamInstance(transactionHistoryInfoDTO);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<List<TransactionFullInfoDTO>> executeCommand(CommandExecutionInfo<TransactionHistoryInfoDTO> commandExecutionInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CommandResult<List<TransactionFullInfoDTO>> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new TransactionFullInfoDTOListResultMapper());
        return commandResult;
    }
}
