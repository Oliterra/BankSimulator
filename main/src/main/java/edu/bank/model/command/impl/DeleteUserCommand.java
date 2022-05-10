package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandExecutionInfo;
import edu.bank.model.command.CommandInfo;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class DeleteUserCommand extends BaseCommand<Long, Void> {

    @Override
    public CommandExecutionInfo<Long> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        CommandExecutionInfo<Long> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setMethodParamInstance(id);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<Void> executeCommand(CommandExecutionInfo<Long> commandExecutionInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return super.executeCommand(commandExecutionInfo);
    }
}
