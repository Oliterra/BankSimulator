package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.result.mapper.LegalEntityFullInfoDTOResultMapper;
import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandExecutionInfo;
import edu.bank.model.command.CommandInfo;
import edu.bank.model.dto.LegalEntityFullInfoDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class GetLegalEntityCommand extends BaseCommand<Long, LegalEntityFullInfoDTO> {

    @Override
    public CommandExecutionInfo<Long> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        CommandExecutionInfo<Long> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setMethodParamInstance(id);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<LegalEntityFullInfoDTO> executeCommand(CommandExecutionInfo<Long> commandExecutionInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CommandResult<LegalEntityFullInfoDTO> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new LegalEntityFullInfoDTOResultMapper());
        return commandResult;
    }
}
