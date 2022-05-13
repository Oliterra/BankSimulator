package edu.bank.console;

import edu.bank.command.CommandExceptionHandler;
import edu.bank.command.CommandExecutor;
import edu.bank.exeption.ExitRequest;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.ErrorMessage;
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
    private final ConsoleCommandResultViewer commandResultViewer;
    private final CommandExceptionHandler commandExceptionHandler;
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void interpretConsoleInput() {
        for (int i = 0; ; i++) {
            try {
                CommandDescription commandDescription;
                if (i == 0) {
                    commandDescription = new CommandDescription();
                    commandDescription.setName("welcome");
                } else {
                    String consoleInput = reader.readLine();
                    commandDescription = consoleLineParser.getCommandDescriptorFromConsoleInput(consoleInput);
                }
                CommandResult<?> commandResult = commandExecutor.executeCommand(commandDescription);
                commandResultViewer.showResult(commandResult);
            } catch (ExitRequest e) {
                CommandResult<ErrorMessage> commandResult = commandExceptionHandler.handleException(e);
                commandResultViewer.showResult(commandResult);
                break;
            } catch (Exception e) {
                CommandResult<ErrorMessage> commandResult = commandExceptionHandler.handleException(e);
                commandResultViewer.showResult(commandResult);
            }
        }
    }
}
