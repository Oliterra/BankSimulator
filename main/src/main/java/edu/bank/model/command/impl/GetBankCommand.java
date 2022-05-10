package edu.bank.model.command.impl;

import edu.bank.result.mapper.BankFullInfoDTOResultMapper;
import edu.bank.result.CommandResult;
import edu.bank.model.command.*;
import edu.bank.model.dto.BankFullInfoDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class GetBankCommand extends BaseCommand<Long, BankFullInfoDTO> {

    @Override
    public void validateCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        super.validateCommand(commandDescription, commandInfo);
    }

    @Override
    public CommandExecutionInfo<Long> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        CommandExecutionInfo<Long> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setMethodParamInstance(id);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<BankFullInfoDTO> executeCommand(CommandExecutionInfo<Long> commandExecutionInfo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        CommandResult<BankFullInfoDTO> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new BankFullInfoDTOResultMapper());
        return commandResult;
    }
}
