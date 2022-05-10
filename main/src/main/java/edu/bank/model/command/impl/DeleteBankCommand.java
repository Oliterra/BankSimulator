package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.model.command.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class DeleteBankCommand extends BaseCommand<Long, Void> {

    @Override
    public CommandExecutionInfo<Long> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        CommandExecutionInfo<Long> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setMethodParamInstance(id);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<Void> executeCommand(CommandExecutionInfo<Long> commandExecutionInfo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return super.executeCommand(commandExecutionInfo);
    }
}
