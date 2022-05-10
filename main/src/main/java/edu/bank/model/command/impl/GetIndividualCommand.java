package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.result.mapper.IndividualFullInfoDTOResultMapper;
import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandExecutionInfo;
import edu.bank.model.command.CommandInfo;
import edu.bank.model.dto.IndividualFullInfoDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class GetIndividualCommand extends BaseCommand<Long, IndividualFullInfoDTO> {

    @Override
    public CommandExecutionInfo<Long> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        CommandExecutionInfo<Long> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setMethodParamInstance(id);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<IndividualFullInfoDTO> executeCommand(CommandExecutionInfo<Long> commandExecutionInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CommandResult<IndividualFullInfoDTO> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new IndividualFullInfoDTOResultMapper());
        return commandResult;
    }
}
