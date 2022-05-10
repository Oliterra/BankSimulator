package edu.bank.model.command.impl;

import edu.bank.result.mapper.BankFullInfoDTOListResultMapper;
import edu.bank.result.CommandResult;
import edu.bank.model.command.*;
import edu.bank.model.dto.BankFullInfoDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Component
public class GetAllBanksCommand extends BaseCommand<Void, List<BankFullInfoDTO>> {

    @Override
    public CommandExecutionInfo<Void> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        CommandExecutionInfo<Void> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<List<BankFullInfoDTO>> executeCommand(CommandExecutionInfo<Void> commandExecutionInfo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        CommandResult<List<BankFullInfoDTO>> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new BankFullInfoDTOListResultMapper());
        return commandResult;
    }
}
