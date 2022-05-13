package edu.bank.command;

import edu.bank.model.command.Command;
import edu.bank.model.command.CommandDescription;
import edu.bank.result.CommandResult;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommandExecutor {

    private final CommandFactory commandFactory;

    @SneakyThrows
    public <T> CommandResult<T> executeCommand(CommandDescription commandDescription) {
        Command createdCommand = commandFactory.createCommand(commandDescription);
        createdCommand.validateCommand(commandDescription);
        return createdCommand.executeCommand(commandDescription);
    }
}
