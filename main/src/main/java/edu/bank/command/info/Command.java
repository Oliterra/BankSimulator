package edu.bank.command.info;

import edu.bank.exeption.ValidationException;
import edu.bank.result.CommandResult;

public interface Command<T> {

    void validateCommand(CommandDescription commandDescription) throws ValidationException;

    CommandResult<T> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException;
}
