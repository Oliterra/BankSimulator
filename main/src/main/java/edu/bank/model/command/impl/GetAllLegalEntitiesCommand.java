package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.result.mapper.LegalEntityFullInfoDTOListResultMapper;
import edu.bank.model.command.*;
import edu.bank.model.dto.LegalEntityFullInfoDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Component
public class GetAllLegalEntitiesCommand extends BaseCommand<Void, List<LegalEntityFullInfoDTO>> {

    @Override
    public CommandExecutionInfo<Void> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        CommandExecutionInfo<Void> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<List<LegalEntityFullInfoDTO>> executeCommand(CommandExecutionInfo<Void> commandExecutionInfo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        CommandResult<List<LegalEntityFullInfoDTO>> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new LegalEntityFullInfoDTOListResultMapper());
        return commandResult;
    }
}
