package edu.bank.command.impl;

import edu.bank.command.CommandParamInspector;
import edu.bank.command.model.CommandParam;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CommandParamInspectorImpl implements CommandParamInspector {

    @Override
    public boolean areCommandParamAndValueValid(String paramAndValue) {
        return paramAndValue.startsWith("-") || paramAndValue.contains("=") || !paramAndValue.endsWith("=");
    }

    @Override
    public boolean areAllCommandParamsPresent(String[] paramsNames, Set<CommandParam> commandParams) {
        Set<String> commandParamsNames = getCommandParamsNames(commandParams);
        int initialLength = paramsNames.length;
        int filteredLength = (int) Arrays.stream(paramsNames).filter(commandParamsNames::contains).count();
        return initialLength == filteredLength;
    }

    @Override
    public boolean isAnyCommandParamPresent(String[] paramsNames, Set<CommandParam> commandParams) {
        Set<String> commandParamsNames = getCommandParamsNames(commandParams);
        return (int) Arrays.stream(paramsNames).filter(commandParamsNames::contains).count() >= 1;
    }

    @Override
    public boolean areCommandParamsContainsParam(String name, Set<CommandParam> commandParams) {
        return commandParams.stream().anyMatch(p -> p.getName().equals(name));
    }

    @Override
    public String getParamValueByNameIfPresent(String name, Set<CommandParam> commandParams) {
        return commandParams.stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getValue();
    }

    private Set<String> getCommandParamsNames(Set<CommandParam> commandParams) {
        Set<String> commandParamsNames = new HashSet<>();
        commandParams.forEach(p -> commandParamsNames.add(p.getName()));
        return commandParamsNames;
    }
}
