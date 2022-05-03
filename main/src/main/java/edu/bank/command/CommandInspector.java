package edu.bank.console;

import edu.bank.model.command.Command;
import edu.bank.model.command.CommandParam;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CommandInspector {

    public boolean isConsoleInputAnEmptyString(String consoleInput) {
        return consoleInput.isEmpty();
    }

    public boolean isAllCommandInfoPresents(Command command) {
        return command.getName() != null || command.getDescription() != null;
    }
}
