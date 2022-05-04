package edu.bank.console.impl;

import edu.bank.command.CommandDescriptor;
import edu.bank.command.CommandExecutor;
import edu.bank.command.CommandFactory;
import edu.bank.command.model.Command;
import edu.bank.console.ConsoleCommandResultViewer;
import edu.bank.console.ConsoleInterpreter;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.exeption.DAOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
@Log4j2
public class ConsoleInterpreterImpl implements ConsoleInterpreter {

    private final CommandDescriptor commandDescriptor;
    private final CommandFactory commandFactory;
    private final CommandExecutor commandExecutor;
    private final ConsoleCommandResultViewer commandResultViewer;
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void interpretConsoleInput() {
        System.out.println("Welcome!\nTo view a list of all available commands, type \"help\"." +
                "\nTo exit the application, use \"exit\".");
        while (true) {
            try {
                String consoleInput = reader.readLine();
                if (consoleInput.contains("help")) {
                    commandDescriptor.showDescription(consoleInput);
                    continue;
                }
                if (consoleInput.equals("exit")) break;
                Command command = commandFactory.getFullCommandByConsoleInput(consoleInput);
                commandExecutor.executeCommand(command);
            } catch (IOException e) {
                commandResultViewer.showFailureMessage("Invalid command format. To view all commands use \"help\"");
                log.error("An error occurred while reading the data: " + e.getMessage());
            } catch (NumberFormatException e) {
                commandResultViewer.showFailureMessage("Invalid data format");
                log.error("An error occurred while reading the data: " + e.getMessage());
            } catch (DAOException e) {
                commandResultViewer.showFailureMessage("Sorry, an internal error has occurred. Try again later");
                log.error("An error occurred while working with the database: " + e.getMessage());
            } catch (BusinessLogicException e) {
                String message = e.getMessage();
                commandResultViewer.showFailureMessage(message);
                log.error("A business logic error has occurred: " + message);
            } catch (Exception e) {
                commandResultViewer.showFailureMessage("Sorry, an internal error has occurred. Try again later");
                log.error("A error has occurred: " + e.getMessage());
            }
        }
    }
}
