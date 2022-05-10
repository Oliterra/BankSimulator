package edu.bank.result.mapper;

import edu.bank.model.command.CommandParamInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.CommandResultMapper;

import java.util.Set;

public class CommandParamsInfoDTOSetResultMapper implements CommandResultMapper<Set<CommandParamInfo>> {

    @Override
    public void showInfo(Set<CommandParamInfo> commandParamInfos) {
        if (commandParamInfos == null)
            System.out.println(ConsoleColor.RED_BOLD + "Command has not params" + ConsoleColor.RESET);
        else {
            CommandParamsInfoDTOResultMapper commandParamsInfoDTOResultMapper = new CommandParamsInfoDTOResultMapper();
            System.out.println(ConsoleColor.YELLOW_BOLD + "List of all command params:");
            commandParamInfos.forEach(commandParamsInfoDTOResultMapper::showInfo);
        }
    }
}
