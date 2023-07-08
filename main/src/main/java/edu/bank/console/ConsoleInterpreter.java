package edu.bank.console;

import edu.bank.command.lifecycle.CommandExceptionHandler;
import edu.bank.command.lifecycle.CommandExecutor;
import edu.bank.command.info.CommandDescription;
import edu.bank.result.CommandResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
public class ConsoleInterpreter {

    private final ConsoleLineParser consoleLineParser;
    private final CommandExecutor commandExecutor;
    private final ConsoleResultViewer commandResultViewer;
    private final CommandExceptionHandler commandExceptionHandler;
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void interpretConsoleInput() {
        for (boolean isFirstIteration = true; ; ) {
            CommandDescription commandDescription;
            CommandResult<?> commandResult;
            try {
                if (isFirstIteration) {
                    commandDescription = new CommandDescription();
                    commandDescription.setName("welcome");
                    isFirstIteration = false;
                } else {
                    String consoleInput = reader.readLine();
                    commandDescription = consoleLineParser.parseConsoleInput(consoleInput);
                }
                commandResult = commandExecutor.executeCommand(commandDescription);
            } catch (Exception e) {
                commandResult = commandExceptionHandler.handleException(e);
            }
            commandResultViewer.showResult(commandResult);
            if (commandResult.isCycleInterrupted()) {
                break;
            }
        }
    }
}
