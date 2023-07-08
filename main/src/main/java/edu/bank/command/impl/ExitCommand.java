package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.result.CommandResult;
import org.springframework.stereotype.Component;

@Component
public class ExitCommand extends BaseCommand<String> {

    @Override
    public CommandResult<String> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        CommandResult<String> commandResult = super.executeCommand(commandDescription);
        commandResult.setCycleInterrupted(true);
        return commandResult;
    }
}
