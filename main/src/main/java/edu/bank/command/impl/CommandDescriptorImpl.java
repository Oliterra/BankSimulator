package edu.bank.command.impl;

import edu.bank.command.CommandDescriptor;
import edu.bank.command.CommandInspector;
import edu.bank.command.model.CommandList;
import edu.bank.console.ConsoleLineParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CommandDescriptorImpl implements CommandDescriptor {
    private final CommandFactoryImpl commandFactory;
    private final ConsoleLineParser consoleLineParser;
    private final CommandInspector commandInspector;
    private CommandList commands;

    @PostConstruct
    private void init() {
        commands = commandFactory.getCommandList();
    }

    @Override
    public void showDescription(String consoleInput) throws IOException {
        if (commandInspector.isConsoleInputAnEmptyString(consoleInput)) throw new IOException();
        String[] consoleInputParts = consoleLineParser.parseConsoleInput(consoleInput);
        if (consoleInputParts[0].equals("help") && consoleInputParts.length == 1) showAllCommandsDescriptions();
        else if (consoleInputParts[0].equals("help") && consoleInputParts.length == 2)
            showCommandDescription(consoleInputParts[1]);
        else throw new IOException();
    }

    @Override
    public void showAllCommandsDescriptions() {
        System.out.println(commands);
    }

    @Override
    public void showCommandDescription(String name) throws IOException {
        System.out.println(commandFactory.getCommandByNameOrThrowException(name));
    }
}
