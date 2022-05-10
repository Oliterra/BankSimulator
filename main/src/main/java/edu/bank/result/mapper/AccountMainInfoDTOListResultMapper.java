package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.AccountMainInfoDTO;
import edu.bank.model.enm.ConsoleColor;

import java.util.List;

public class AccountMainInfoDTOListResultMapper implements CommandResultMapper<List<AccountMainInfoDTO>> {

    @Override
    public void showInfo(List<AccountMainInfoDTO> accountMainInfoDTOS) {
        if (accountMainInfoDTOS.isEmpty())
            System.out.println(ConsoleColor.RED_BOLD + "No banks found" + ConsoleColor.RESET);
        else {
            AccountMainInfoDTOResultMapper accountMainInfoDTOResultMapper = new AccountMainInfoDTOResultMapper();
            System.out.println(ConsoleColor.YELLOW_BOLD + "List of all accounts:");
            accountMainInfoDTOS.forEach(accountMainInfoDTOResultMapper::showInfo);
        }
    }
}
