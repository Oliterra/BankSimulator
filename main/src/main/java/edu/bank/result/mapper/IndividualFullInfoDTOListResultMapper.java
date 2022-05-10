package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.IndividualFullInfoDTO;
import edu.bank.model.enm.ConsoleColor;

import java.util.List;

public class IndividualFullInfoDTOListResultMapper implements CommandResultMapper<List<IndividualFullInfoDTO>> {

    @Override
    public void showInfo(List<IndividualFullInfoDTO> individualFullInfoDTOS) {
        if (individualFullInfoDTOS.isEmpty())
            System.out.println(ConsoleColor.RED_BOLD + "No individuals found" + ConsoleColor.RESET);
        else {
            IndividualFullInfoDTOResultMapper individualFullInfoDTOResultMapper = new IndividualFullInfoDTOResultMapper();
            System.out.println(ConsoleColor.YELLOW_BOLD + "List of all individuals:");
            individualFullInfoDTOS.forEach(individualFullInfoDTOResultMapper::showInfo);
        }
    }
}
