package edu.bank.command;

import edu.bank.command.model.Command;
import org.springframework.stereotype.Component;

@Component
public class CommandInspectorImpl implements CommandInspector {

    @Override
    public boolean isConsoleInputAnEmptyString(String consoleInput) {
        return consoleInput.isEmpty();
    }

    @Override
    public boolean isAllCommandInfoPresents(Command command) {
        return command.getName() != null || command.getDescription() != null;
    }
}
