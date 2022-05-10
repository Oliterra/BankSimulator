package edu.bank.result.mapper;

import edu.bank.model.command.CommandParamInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.CommandResultMapper;

public class CommandParamsInfoDTOResultMapper implements CommandResultMapper<CommandParamInfo> {

    @Override
    public void showInfo(CommandParamInfo commandParamInfo) {
        System.out.println(ConsoleColor.YELLOW_BOLD + "Command param:");
        System.out.println(ConsoleColor.GREEN_BOLD + "Name: " + ConsoleColor.RESET + commandParamInfo.getName());
        System.out.println(ConsoleColor.GREEN_BOLD + "Content: " + ConsoleColor.RESET + commandParamInfo.getContent());
        System.out.println(ConsoleColor.GREEN_BOLD + "Min length: " + ConsoleColor.RESET + commandParamInfo.getMinLength());
        System.out.println(ConsoleColor.GREEN_BOLD + "Max length: " + ConsoleColor.RESET + commandParamInfo.getMaxLength());
        System.out.println(ConsoleColor.GREEN_BOLD + "Min value " + ConsoleColor.RESET + commandParamInfo.getMinValue());
        System.out.println(ConsoleColor.GREEN_BOLD + "Max value " + ConsoleColor.RESET + commandParamInfo.getMaxValue());
    }
}
