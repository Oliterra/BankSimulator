package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.LegalEntityFullInfoDTO;
import edu.bank.model.enm.ConsoleColor;

public class LegalEntityFullInfoDTOResultMapper implements CommandResultMapper<LegalEntityFullInfoDTO> {

    @Override
    public void showInfo(LegalEntityFullInfoDTO legalEntityFullInfoDTO) {
        System.out.println(ConsoleColor.CYAN_BOLD + "Full information about the legal entity:");
        System.out.println(ConsoleColor.MAGENTA_BOLD + "ID: " + ConsoleColor.RESET + legalEntityFullInfoDTO.getId());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "First name: " + ConsoleColor.RESET + legalEntityFullInfoDTO.getName());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Phone: " + ConsoleColor.RESET + legalEntityFullInfoDTO.getPhone());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "All accounts: " + ConsoleColor.RESET + legalEntityFullInfoDTO.getAccounts());
    }
}
