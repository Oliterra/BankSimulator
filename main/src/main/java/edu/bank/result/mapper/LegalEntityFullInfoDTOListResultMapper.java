package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.LegalEntityFullInfoDTO;
import edu.bank.model.enm.ConsoleColor;

import java.util.List;

public class LegalEntityFullInfoDTOListResultMapper implements CommandResultMapper<List<LegalEntityFullInfoDTO>> {

    @Override
    public void showInfo(List<LegalEntityFullInfoDTO> legalEntityFullInfoDTOS) {
        if (legalEntityFullInfoDTOS.isEmpty())
            System.out.println(ConsoleColor.RED_BOLD + "No legal entities found" + ConsoleColor.RESET);
        else {
            LegalEntityFullInfoDTOResultMapper legalEntityFullInfoDTOResultMapper = new LegalEntityFullInfoDTOResultMapper();
            System.out.println(ConsoleColor.YELLOW_BOLD + "List of all legal entities:");
            legalEntityFullInfoDTOS.forEach(legalEntityFullInfoDTOResultMapper::showInfo);
        }
    }
}
