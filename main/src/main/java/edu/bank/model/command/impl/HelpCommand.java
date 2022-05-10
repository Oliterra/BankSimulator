package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.result.mapper.CommandFullInfoDTOListResultMapper;
import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandExecutionInfo;
import edu.bank.model.command.CommandInfo;
import edu.bank.model.dto.CommandFullInfoDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

@Component
public class HelpCommand extends BaseCommand<Void, Set<CommandFullInfoDTO>> {

    @Override
    public CommandExecutionInfo<Void> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        CommandExecutionInfo<Void> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<Set<CommandFullInfoDTO>> executeCommand(CommandExecutionInfo<Void> commandExecutionInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CommandResult<Set<CommandFullInfoDTO>> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new CommandFullInfoDTOListResultMapper());
        return commandResult;
    }
}
