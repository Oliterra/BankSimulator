package edu.bank.console;

import java.io.IOException;

public interface ConsoleLineParser {
    String[] parseConsoleInput(String consoleInput) throws IOException;

    String getCommandNameFromConsoleInput(String consoleInput) throws IOException;
}
