package edu.bank.service;

import edu.bank.model.enm.Command;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public interface CommandManager {

    default void showAllCommands() {
        Arrays.stream(Command.values()).forEach(System.out::println);
    }

    default Command getCommandByName(String commandName) throws IOException {
        return Arrays.stream(Command.values())
                .filter(c -> c.getCommandName().equals(commandName))
                .findFirst()
                .orElseThrow(IOException::new);
    }

    default Map<String, String> getCommandParams(Command command, String[] commandParts) throws IOException {
        Map<String, String> params = new HashMap<>();
        for (int i = 1; i < commandParts.length; i++) {
            String paramAndValue = commandParts[i];
            if (!paramAndValue.startsWith("-") || !paramAndValue.contains("=") || paramAndValue.endsWith("="))
                throw new IOException();
            String[] splittedParamAndValue = paramAndValue.split("=");
            if (splittedParamAndValue.length != 2) throw new IOException();
            String param = splittedParamAndValue[0].substring(1);
            String value = splittedParamAndValue[1];
            if (!isCommandParamsValid(command, param)) throw new IOException();
            if (!params.containsKey(param)) params.put(param, value);
            else throw new IOException();
        }
        return params;
    }

    default boolean isCommandParamsValid(Command command, String commandParamName) {
        return Arrays.stream(command.getCommandParams())
                .anyMatch(p -> p.getParamName().equals(commandParamName));
    }

    default boolean areAllParamsPresent(String[] paramsNames, Map<String, String> bankInfo) {
        int initialLength = paramsNames.length;
        int filteredLength = (int) Arrays.stream(paramsNames).filter(bankInfo::containsKey).count();
        return initialLength == filteredLength;
    }

    default boolean isAnyParamPresent(String[] paramsNames, Map<String, String> bankInfo) {
        return (int) Arrays.stream(paramsNames).filter(bankInfo::containsKey).count() >= 1;
    }
}
