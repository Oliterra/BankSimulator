package edu.bank.result.mapper;

import edu.bank.model.dto.CommandFullInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.CommandResultMapper;

public class CommandFullInfoResultMapper implements CommandResultMapper<CommandFullInfo> {

    @Override
    public String mapResult(CommandFullInfo commandFullInfo) {
        CommandParamsInfoSetResultMapper commandParamsInfoSetResultMapper = new CommandParamsInfoSetResultMapper();
        return ConsoleColor.YELLOW_BOLD + "\nFull command info:"
                + ConsoleColor.MAGENTA_BOLD + "\nName: " + ConsoleColor.RESET + commandFullInfo.getName()
                + ConsoleColor.MAGENTA_BOLD + "\nDescription: " + ConsoleColor.RESET + commandFullInfo.getDescription()
                + ConsoleColor.MAGENTA_BOLD + "\nCommand params: " + ConsoleColor.RESET
                + "\n" + commandParamsInfoSetResultMapper.mapResult(commandFullInfo.getParams());
    }
}
