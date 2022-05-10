package edu.bank.console;

import edu.bank.model.command.CommandDescription;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConsoleLineParser {

    public CommandDescription getCommandDescriptorFromConsoleInput(String consoleInput) throws IOException {
        if (consoleInput == null || consoleInput.isEmpty()) throw new IOException();
        String[] splitConsoleInput = consoleInput.split(" ");
        String commandName = splitConsoleInput[0];
        String[] inputCommandParams = Arrays.stream(splitConsoleInput).skip(1).toArray(String[]::new);
        Map<String, String> commandParams = createCommandParamsMapFromSplitConsoleParams(inputCommandParams);
        CommandDescription commandDescription = new CommandDescription();
        commandDescription.setName(commandName);
        commandDescription.setParams(commandParams);
        return commandDescription;
    }

    private Map<String, String> createCommandParamsMapFromSplitConsoleParams(String[] inputCommandParams) throws IOException {
        Map<String, String> paramsMap = new HashMap<>();
        for (int i = 0; i < inputCommandParams.length; i++) {
            String inputParamAndValue = inputCommandParams[i];
            if (!isConsoleParamAndValueValid(inputParamAndValue)) throw new IOException();
            String[] splitParamAndValue = inputParamAndValue.split("=");
            if (splitParamAndValue.length != 2) throw new IOException();
            String param = splitParamAndValue[0].substring(1);
            String value = splitParamAndValue[1];
            if (!paramsMap.containsKey(param)) paramsMap.put(param, value);
            else throw new IOException();
        }
        return paramsMap;
    }

    private boolean isConsoleParamAndValueValid(String inputParamAndValue) {
        return inputParamAndValue.startsWith("-") || inputParamAndValue.contains("=") || !inputParamAndValue.endsWith("=");
    }
}

