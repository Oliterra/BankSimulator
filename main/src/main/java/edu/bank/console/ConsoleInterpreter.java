package edu.bank.console;

import edu.bank.command.CommandExceptionHandler;
import edu.bank.command.CommandExecutor;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.ErrorDTO;
import edu.bank.result.CommandResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
@Log4j2
public class ConsoleInterpreter {

    private final ConsoleLineParser consoleLineParser;
    private final CommandExecutor commandExecutor;
    private final ConsoleCommandResultViewer commandResultViewer;
    private final CommandExceptionHandler commandExceptionHandler;
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public <T> void interpretConsoleInput() {
        commandResultViewer.showWelcomeMessage();
        while (true) {
            try {
                String consoleInput = reader.readLine();
                if (commandResultViewer.isTheCycleInterrupted(consoleInput)) break;
                CommandDescription commandDescription = consoleLineParser.getCommandDescriptorFromConsoleInput(consoleInput);
                CommandResult<T> commandResult = commandExecutor.executeCommand(commandDescription);
                commandResultViewer.showResult(commandResult);
            } catch (Exception e) {
                CommandResult<ErrorDTO> commandResult = commandExceptionHandler.handleException(e.getCause());
                commandResultViewer.showResult(commandResult);
            }
        }
    }
}
