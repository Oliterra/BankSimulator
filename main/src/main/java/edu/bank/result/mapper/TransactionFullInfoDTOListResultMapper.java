package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.TransactionFullInfoDTO;
import edu.bank.model.enm.ConsoleColor;

import java.util.List;

public class TransactionFullInfoDTOListResultMapper implements CommandResultMapper<List<TransactionFullInfoDTO>> {

    @Override
    public void showInfo(List<TransactionFullInfoDTO> transactionFullInfoDTOList) {
        if (transactionFullInfoDTOList.isEmpty())
            System.out.println(ConsoleColor.RED_BOLD + "No transactions found" + ConsoleColor.RESET);
        else {
            TransactionFullInfoDTOResultMapper transactionFullInfoDTOResultMapper = new TransactionFullInfoDTOResultMapper();
            System.out.println(ConsoleColor.YELLOW_BOLD + "List of all user transactions:");
            transactionFullInfoDTOList.forEach(transactionFullInfoDTOResultMapper::showInfo);
        }
    }
}
