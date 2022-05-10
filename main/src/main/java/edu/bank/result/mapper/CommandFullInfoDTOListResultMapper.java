package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.CommandFullInfoDTO;
import edu.bank.model.enm.ConsoleColor;

import java.util.Set;

public class CommandFullInfoDTOListResultMapper implements CommandResultMapper<Set<CommandFullInfoDTO>> {

    @Override
    public void showInfo(Set<CommandFullInfoDTO> commandFullInfoDTOS) {
        if (commandFullInfoDTOS.isEmpty())
            System.out.println(ConsoleColor.RED_BOLD + "No individuals found" + ConsoleColor.RESET);
        else {
            CommandFullInfoDTOResultMapper commandFullInfoDTOResultMapper = new CommandFullInfoDTOResultMapper();
            System.out.println(ConsoleColor.YELLOW_BOLD + "List of all commands:");
            commandFullInfoDTOS.forEach(commandFullInfoDTOResultMapper::showInfo);
        }
    }
}
