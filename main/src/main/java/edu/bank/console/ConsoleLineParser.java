package edu.bank.console;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConsoleLineParser {

    private static final String INVALID_PARAMS_FORMAT_MESSAGE = "Invalid console command params format. Please use -{param}={value}";

    public CommandDescription parseConsoleInput(String consoleInput) throws ValidationException {
        if (consoleInput == null || consoleInput.isEmpty()) throw new ValidationException("Console input is empty");
        String[] splitConsoleInput = consoleInput.split(" ");
        String commandName = splitConsoleInput[0];
        String[] inputCommandParams = Arrays.stream(splitConsoleInput).skip(1).toArray(String[]::new);
        Map<String, String> commandParams = createCommandParamsMapFromSplitConsoleParams(inputCommandParams);
        CommandDescription commandDescription = new CommandDescription();
        commandDescription.setName(commandName);
        commandDescription.setParams(commandParams);
        return commandDescription;
    }

    private Map<String, String> createCommandParamsMapFromSplitConsoleParams(String[] inputCommandParams) throws ValidationException {
        Map<String, String> paramsMap = new HashMap<>();
        for (String inputParamAndValue : inputCommandParams) {
            if (!isConsoleParamAndValueValid(inputParamAndValue))
                throw new ValidationException(INVALID_PARAMS_FORMAT_MESSAGE);
            String[] splitParamAndValue = inputParamAndValue.split("=");
            if (splitParamAndValue.length != 2) throw new ValidationException(INVALID_PARAMS_FORMAT_MESSAGE);
            String param = splitParamAndValue[0].substring(1);
            String value = splitParamAndValue[1];
            if (!paramsMap.containsKey(param)) paramsMap.put(param, value);
            else throw new ValidationException(INVALID_PARAMS_FORMAT_MESSAGE);
        }
        return paramsMap;
    }

    private boolean isConsoleParamAndValueValid(String inputParamAndValue) {
        return inputParamAndValue.startsWith("-") || inputParamAndValue.contains("=") || !inputParamAndValue.endsWith("=");
    }
}

