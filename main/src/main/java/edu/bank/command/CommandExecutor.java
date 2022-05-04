package edu.bank.command;

import edu.bank.command.model.Command;

public interface CommandExecutor {
    void executeCommand(Command command);
}
