package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.BankFullInfoDTO;
import edu.bank.model.enm.ConsoleColor;

import java.util.List;

public class BankFullInfoDTOListResultMapper implements CommandResultMapper<List<BankFullInfoDTO>> {

    @Override
    public void showInfo(List<BankFullInfoDTO> bankFullInfoDTOS) {
        if (bankFullInfoDTOS.isEmpty())
            System.out.println(ConsoleColor.RED_BOLD + "No banks found" + ConsoleColor.RESET);
        else {
            BankFullInfoDTOResultMapper bankFullInfoDTOResultMapper = new BankFullInfoDTOResultMapper();
            System.out.println(ConsoleColor.YELLOW_BOLD + "List of all banks:");
            bankFullInfoDTOS.forEach(bankFullInfoDTOResultMapper::showInfo);
        }
    }
}
