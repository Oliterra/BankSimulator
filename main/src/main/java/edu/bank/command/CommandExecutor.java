package edu.bank.command;

import edu.bank.result.CommandResult;
import edu.bank.model.command.Command;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandExecutionInfo;
import edu.bank.model.command.CommandInfo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CommandExecutor {

    private final CommandFactory commandFactory;

    @SneakyThrows
    public <T, E> CommandResult<E> executeCommand(CommandDescription commandDescription) throws IOException {
        CommandInfo commandInfo = commandFactory.getCommandInfoByCommandName(commandDescription.getName());
        Command<T, E> createdCommand = commandFactory.createCommandByDescription(commandDescription);
        CommandExecutionInfo<T> commandExecutionInfo = createdCommand.prepareCommand(commandDescription, commandInfo);
        return createdCommand.executeCommand(commandExecutionInfo);
    }
}
