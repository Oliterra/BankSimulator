package edu.bank.command;

import java.io.IOException;

public interface CommandDescriptor {
    void showDescription(String consoleInput) throws IOException;

    void showAllCommandsDescriptions();

    void showCommandDescription(String name) throws IOException;
}
