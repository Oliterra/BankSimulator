package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.result.mapper.TransactionFullInfoDTOResultMapper;
import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandExecutionInfo;
import edu.bank.model.command.CommandInfo;
import edu.bank.model.dto.TransactionFullInfoDTO;
import edu.bank.model.dto.TransferMoneyInfoDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class TransferMoneyCommand extends BaseCommand<TransferMoneyInfoDTO, TransactionFullInfoDTO> {

    @Override
    public CommandExecutionInfo<TransferMoneyInfoDTO> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        String fromIban = getParamValueByNameOrReturnNull(commandDescription, "fromIban");
        String toIban = getParamValueByNameOrReturnNull(commandDescription, "toIban");
        double sum = Double.parseDouble(getParamValueByNameOrReturnNull(commandDescription, "sum"));
        CommandExecutionInfo<TransferMoneyInfoDTO> commandExecutionInfo = new CommandExecutionInfo<>();
        TransferMoneyInfoDTO transferMoneyInfoDTO = new TransferMoneyInfoDTO();
        transferMoneyInfoDTO.setFromIban(fromIban);
        transferMoneyInfoDTO.setToIban(toIban);
        transferMoneyInfoDTO.setSum(sum);
        commandExecutionInfo.setMethodParamInstance(transferMoneyInfoDTO);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<TransactionFullInfoDTO> executeCommand(CommandExecutionInfo<TransferMoneyInfoDTO> commandExecutionInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CommandResult<TransactionFullInfoDTO> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new TransactionFullInfoDTOResultMapper());
        return commandResult;
    }
}
