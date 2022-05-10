package edu.bank.model.command;

import edu.bank.result.CommandResult;

import java.lang.reflect.InvocationTargetException;

public interface Command<T, E> {

    void validateCommand(CommandDescription commandDescription, CommandInfo commandInfo);

    CommandExecutionInfo<T> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo);

    CommandResult<E> executeCommand(CommandExecutionInfo<T> enrichedCommandDescriptor) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException;
}
