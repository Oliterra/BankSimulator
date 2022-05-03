package edu.bank.console;

import edu.bank.command.CommandInspector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ConsoleLineParserImpl implements ConsoleLineParser {

    private final CommandInspector commandInspector;

    @Override
    public String[] parseConsoleInput(String consoleInput) throws IOException {
        if (commandInspector.isConsoleInputAnEmptyString(consoleInput)) throw new IOException();
        return consoleInput.split(" ");
    }

    @Override
    public String getCommandNameFromConsoleInput(String consoleInput) throws IOException {
        if (commandInspector.isConsoleInputAnEmptyString(consoleInput)) throw new IOException();
        String[] splittedConsoleInput = parseConsoleInput(consoleInput);
        return splittedConsoleInput[0];
    }
}
