package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.IndividualFullInfoDTO;
import edu.bank.model.enm.ConsoleColor;

public class IndividualFullInfoDTOResultMapper implements CommandResultMapper<IndividualFullInfoDTO> {

    @Override
    public void showInfo(IndividualFullInfoDTO individualFullInfoDTO) {
        System.out.println(ConsoleColor.CYAN_BOLD + "Full information about the individual:");
        System.out.println(ConsoleColor.MAGENTA_BOLD + "ID: " + ConsoleColor.RESET + individualFullInfoDTO.getId());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "First name: " + ConsoleColor.RESET + individualFullInfoDTO.getName());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Last name: " + ConsoleColor.RESET + individualFullInfoDTO.getLastName());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Patronymic: " + ConsoleColor.RESET + individualFullInfoDTO.getPatronymic());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Phone: " + ConsoleColor.RESET + individualFullInfoDTO.getPhone());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "All accounts: " + ConsoleColor.RESET + individualFullInfoDTO.getAccounts());
    }
}
