package edu.bank.result.mapper.impl;

import edu.bank.model.dto.CommandFullInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.mapper.CommandResultMapper;

import java.util.Set;

public class CommandFullInfoListResultMapper implements CommandResultMapper<Set<CommandFullInfo>> {

    @Override
    public String mapResult(Set<CommandFullInfo> commandFullInfos) {
        if (commandFullInfos.isEmpty())
            return ConsoleColor.YELLOW_BOLD + "No individuals found" + ConsoleColor.RESET;
        else {
            CommandFullInfoResultMapper commandFullInfoResultMapper = new CommandFullInfoResultMapper();
            StringBuilder resultString = new StringBuilder();
            resultString.append(ConsoleColor.YELLOW_BOLD + "List of all commands:");
            commandFullInfos.forEach(i -> resultString.append(commandFullInfoResultMapper.mapResult(i)));
            return resultString.toString();
        }
    }
}
