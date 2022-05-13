package edu.bank.model.command;

import edu.bank.result.CommandResult;

import java.io.IOException;

public interface Command {

    void validateCommand(CommandDescription commandDescription) throws IOException;

    <T> CommandResult<T> executeCommand(CommandDescription commandDescription) throws IOException, ReflectiveOperationException;
}
