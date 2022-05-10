package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.result.mapper.IndividualFullInfoDTOListResultMapper;
import edu.bank.model.command.*;
import edu.bank.model.dto.IndividualFullInfoDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Component
public class GetAllIndividualsCommand extends BaseCommand<Void, List<IndividualFullInfoDTO>> {

    @Override
    public CommandExecutionInfo<Void> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        CommandExecutionInfo<Void> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<List<IndividualFullInfoDTO>> executeCommand(CommandExecutionInfo<Void> commandExecutionInfo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        CommandResult<List<IndividualFullInfoDTO>> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new IndividualFullInfoDTOListResultMapper());
        return commandResult;
    }
}
