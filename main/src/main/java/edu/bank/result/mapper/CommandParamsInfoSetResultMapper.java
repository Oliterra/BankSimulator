package edu.bank.result.mapper;

import edu.bank.model.command.CommandParamInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.CommandResultMapper;

import java.util.Set;

public class CommandParamsInfoSetResultMapper implements CommandResultMapper<Set<CommandParamInfo>> {

    @Override
    public String mapResult(Set<CommandParamInfo> commandParamInfos) {
        if (commandParamInfos == null)
            return ConsoleColor.YELLOW_BOLD + "Command has not params" + ConsoleColor.RESET;
        else {
            CommandParamsInfoResultMapper commandParamsInfoResultMapper = new CommandParamsInfoResultMapper();
            StringBuilder resultString = new StringBuilder();
            resultString.append(ConsoleColor.YELLOW_BOLD + "List of all command params:");
            commandParamInfos.forEach(i -> resultString.append(commandParamsInfoResultMapper.mapResult(i)));
            return resultString.toString();
        }
    }
}
