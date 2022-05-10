package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.BankFullInfoDTO;
import edu.bank.model.enm.ConsoleColor;

public class BankFullInfoDTOResultMapper implements CommandResultMapper<BankFullInfoDTO> {

    @Override
    public void showInfo(BankFullInfoDTO bankDTO) {
        System.out.println(ConsoleColor.CYAN_BOLD + "Full information about the bank:");
        System.out.println(ConsoleColor.MAGENTA_BOLD + "ID: " + ConsoleColor.RESET + bankDTO.getId());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Name: " + ConsoleColor.RESET + bankDTO.getName());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "IBAN prefix: " + ConsoleColor.RESET + bankDTO.getIbanPrefix());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Fee for individuals: " + ConsoleColor.RESET + bankDTO.getIndividualsFee());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Count of users: " + ConsoleColor.RESET + bankDTO.getUsersCount());
    }
}
