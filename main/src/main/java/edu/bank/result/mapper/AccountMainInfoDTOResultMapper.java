package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.AccountMainInfoDTO;
import edu.bank.model.enm.ConsoleColor;

public class AccountMainInfoDTOResultMapper implements CommandResultMapper<AccountMainInfoDTO> {

    @Override
    public void showInfo(AccountMainInfoDTO accountMainInfoDTO) {
        System.out.println(ConsoleColor.CYAN_BOLD + "Full account info:");
        System.out.println(ConsoleColor.MAGENTA_BOLD + "IBAN: " + ConsoleColor.RESET + accountMainInfoDTO.getIban());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Bank: " + ConsoleColor.RESET + accountMainInfoDTO.getBankName());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Currency: " + ConsoleColor.RESET + accountMainInfoDTO.getCurrency());
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Balance: " + ConsoleColor.RESET + accountMainInfoDTO.getBalance());
    }
}
