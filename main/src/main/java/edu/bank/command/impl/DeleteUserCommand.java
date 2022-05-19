package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.result.CommandResult;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserCommand extends BaseCommand<String> {

    @Override
    public CommandResult<String> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        return executeAnyCommand(commandDescription, id);
    }
}
