package edu.bank.result.mapper.impl;

import edu.bank.command.info.CommandParamInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.mapper.CommandResultMapper;

public class CommandParamsInfoResultMapper implements CommandResultMapper<CommandParamInfo> {

    @Override
    public String mapResult(CommandParamInfo commandParamInfo) {
        return ConsoleColor.YELLOW_BOLD + "\nCommand param:"
                + ConsoleColor.MAGENTA_BOLD + "\nName: " + ConsoleColor.RESET + commandParamInfo.getName()
                + ConsoleColor.MAGENTA_BOLD + "\nContent: " + ConsoleColor.RESET + commandParamInfo.getContent()
                + ConsoleColor.MAGENTA_BOLD + "\nMin length: " + ConsoleColor.RESET + commandParamInfo.getMinLength()
                + ConsoleColor.MAGENTA_BOLD + "\nMax length: " + ConsoleColor.RESET + commandParamInfo.getMaxLength()
                + ConsoleColor.MAGENTA_BOLD + "\nMin value " + ConsoleColor.RESET + commandParamInfo.getMinValue()
                + ConsoleColor.MAGENTA_BOLD + "\nMax value " + ConsoleColor.RESET + commandParamInfo.getMaxValue();
    }
}
