package edu.bank.command;

import edu.bank.exeption.BusinessLogicException;
import edu.bank.exeption.InternalError;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandParamInfo;
import edu.bank.model.command.CommandsInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandParamsValidator {

    private final CommandsInfoStorage commandsInfoStorage;
    private static final String CONTAINS_LETTERS_ONLY = "letters";
    private static final String CONTAINS_DIGITS_ONLY = "digits";
    private static final String CONTAINS_LETTERS_AND_DIGITS = "letters and digits";
    private static final String CONTAINS_DATE = "date";

    public void validateCommandParams(CommandDescription commandDescription) throws IOException {
        String commandName = commandDescription.getName();
        Set<CommandParamInfo> commandParamsInfo = getCommandParamsInfoByName(commandName);
        Map<String, String> commandRawParams = commandDescription.getParams();
        if (commandParamsInfo != null) {
            if (!areAllRequiredParamsPresent(commandParamsInfo, commandRawParams))
                throw new BusinessLogicException("Not all required parameters are present");
            commandRawParams.keySet().forEach(p -> checkInputCommandParam(commandRawParams.get(p), getCommandParamInfoByName(commandParamsInfo, p)));
        }
    }

    private boolean areAllRequiredParamsPresent(Set<CommandParamInfo> commandParamsInfo, Map<String, String> commandRawParams) {
        Set<String> requiredParamsNames = getAllRequiredParamsNames(commandParamsInfo);
        long requiredParamsCount = requiredParamsNames.size();
        long filteredParamsCount = commandRawParams.keySet().stream().filter(requiredParamsNames::contains).count();
        return requiredParamsCount == filteredParamsCount;
    }

    private void checkInputCommandParam(String paramValue, CommandParamInfo commandParamInfo) {
        switch (commandParamInfo.getContent()) {
            case CONTAINS_LETTERS_ONLY -> {
                if (!paramValue.matches("^[a-zA-Z]*$"))
                    throw new BusinessLogicException("The parameter value should contain only letters: " + paramValue);
                checkIfStringInputParamIsValid(paramValue, commandParamInfo);
            }
            case CONTAINS_DIGITS_ONLY -> {
                if (!paramValue.matches("^(?!,$)[\\d,.]+$"))
                    throw new BusinessLogicException("The parameter value should contain only digits, dot or comma: " + paramValue);
                checkIfNumberInputParamIsValid(paramValue, commandParamInfo);
            }
            case CONTAINS_LETTERS_AND_DIGITS -> {
                if (!paramValue.matches("[A-Z\\d]+"))
                    throw new BusinessLogicException("The parameter value should contain only letters and digits: " + paramValue);
                checkIfStringInputParamIsValid(paramValue, commandParamInfo);
            }
            case CONTAINS_DATE -> {
                if (!paramValue.matches("^\\d{4}-\\d{2}-\\d{2}$"))
                    throw new BusinessLogicException("Invalid date format: " + paramValue);
                checkIfDateInputParamIsValid(paramValue);
            }
            default -> throw new InternalError("Invalid description of the parameter content: " + paramValue);
        }
    }

    private void checkIfNumberInputParamIsValid(String paramValue, CommandParamInfo commandParamInfo) {
        double doubleInputParam = Double.parseDouble(paramValue);
        double paramMinValueParamInfo = commandParamInfo.getMinValue();
        double paramMaxValueParamInfo = commandParamInfo.getMaxValue();
        if (paramMaxValueParamInfo == 0) paramMaxValueParamInfo = Double.MAX_VALUE;
        if (doubleInputParam < paramMinValueParamInfo || doubleInputParam > paramMaxValueParamInfo)
            throw new BusinessLogicException(String.format("The value %f is invalid. The minimum allowed value is %f. Maximum allowed value is %f", doubleInputParam, paramMinValueParamInfo, paramMaxValueParamInfo));
    }

    private void checkIfStringInputParamIsValid(String paramValue, CommandParamInfo commandParamInfo) {
        int paramMinLengthParamInfo = commandParamInfo.getMinLength();
        int paramMaxLengthParamInfo = commandParamInfo.getMaxLength();
        if (paramValue.length() < paramMinLengthParamInfo || paramValue.length() > paramMaxLengthParamInfo)
            throw new BusinessLogicException(String.format("The value %s is invalid. The minimum allowed value length is %d. Maximum allowed value length is %d", paramValue, paramMinLengthParamInfo, paramMaxLengthParamInfo));
    }

    private void checkIfDateInputParamIsValid(String paramValue) {
        try {
            LocalDate date = LocalDate.parse(paramValue);
        } catch (DateTimeParseException e) {
            throw new BusinessLogicException("Invalid date format: " + paramValue);
        }
    }

    private Set<String> getAllRequiredParamsNames(Set<CommandParamInfo> commandParamsInfo) {
        Set<String> requiredParamsNames = new HashSet<>();
        commandParamsInfo.stream().filter(p -> !p.isNullable()).forEach(p -> requiredParamsNames.add(p.getName()));
        return requiredParamsNames;
    }

    private Set<CommandParamInfo> getCommandParamsInfoByName(String name) throws IOException {
        CommandsInfo commandsInfo = commandsInfoStorage.getCommandsInfo();
        return commandsInfo.getCommandsInfo().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IOException("There is no such command"))
                .getParams();
    }

    private CommandParamInfo getCommandParamInfoByName(Set<CommandParamInfo> commandParamsInfo, String paramName) {
        return commandParamsInfo.stream()
                .filter(p -> p.getName().equals(paramName))
                .findFirst()
                .orElseThrow(() -> new BusinessLogicException("Invalid parameter name"));
    }
}
