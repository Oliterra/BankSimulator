package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.CommandFullInfoDTO;
import edu.bank.model.enm.ConsoleColor;

public class CommandFullInfoDTOResultMapper implements CommandResultMapper<CommandFullInfoDTO> {

    @Override
    public void showInfo(CommandFullInfoDTO commandFullInfoDTO) {
        System.out.println(ConsoleColor.CYAN_BOLD + "Full command info:");
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Name: " + ConsoleColor.RESET + commandFullInfoDTO.getName());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Description: " + ConsoleColor.RESET + commandFullInfoDTO.getDescription());
        CommandParamsInfoDTOSetResultMapper commandParamsInfoDTOSetResultMapper = new CommandParamsInfoDTOSetResultMapper();
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Command params: " + ConsoleColor.RESET);
        commandParamsInfoDTOSetResultMapper.showInfo(commandFullInfoDTO.getParams());
        System.out.println("\n");
    }
}
