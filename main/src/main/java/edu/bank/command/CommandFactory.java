package edu.bank.command;

import edu.bank.command.model.Command;

import java.io.IOException;

public interface CommandFactory {
    Command getFullCommandByConsoleInput(String consoleInput) throws IOException;

    Command getCommandByNameOrThrowException(String name) throws IOException;
}
