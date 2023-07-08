package edu.bank.command.lifecycle;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.Command;
import edu.bank.command.info.CommandDescription;
import edu.bank.result.CommandResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandExecutor {

    private final CommandFactory commandFactory;

    public <T> CommandResult<T> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        Command<T> createdCommand = commandFactory.createCommand(commandDescription);
        createdCommand.validateCommand(commandDescription);
        return createdCommand.executeCommand(commandDescription);
    }
}
