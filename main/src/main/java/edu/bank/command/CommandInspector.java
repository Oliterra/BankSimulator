package edu.bank.command;

import edu.bank.command.model.Command;

public interface CommandInspector {
    boolean isConsoleInputAnEmptyString(String consoleInput);

    boolean isAllCommandInfoPresents(Command command);
}
