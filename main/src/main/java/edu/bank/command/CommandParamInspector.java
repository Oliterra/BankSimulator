package edu.bank.command;

import edu.bank.command.model.CommandParam;

import java.util.Set;

public interface CommandParamInspector {
    boolean areCommandParamAndValueValid(String paramAndValue);

    boolean areAllCommandParamsPresent(String[] paramsNames, Set<CommandParam> commandParams);

    boolean isAnyCommandParamPresent(String[] paramsNames, Set<CommandParam> commandParams);

    boolean areCommandParamsContainsParam(String name, Set<CommandParam> commandParams);

    String getParamValueByNameIfPresent(String name, Set<CommandParam> commandParams);
}
