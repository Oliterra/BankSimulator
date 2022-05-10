package edu.bank.result.mapper;

import edu.bank.result.CommandResultMapper;
import edu.bank.model.dto.TransactionFullInfoDTO;
import edu.bank.model.enm.ConsoleColor;

public class TransactionFullInfoDTOResultMapper implements CommandResultMapper<TransactionFullInfoDTO> {

    @Override
    public void showInfo(TransactionFullInfoDTO transactionFullInfoDTO) {
        System.out.println(ConsoleColor.MAGENTA_BOLD + "Receipt:");
        System.out.println(ConsoleColor.YELLOW_BOLD + "ID: " + ConsoleColor.RESET + transactionFullInfoDTO.getId());
        System.out.println(ConsoleColor.YELLOW_BOLD + "Bank : " + ConsoleColor.RESET + transactionFullInfoDTO.getRecipientBankName());
        System.out.println(ConsoleColor.YELLOW_BOLD + "Date : " + ConsoleColor.RESET + transactionFullInfoDTO.getDate());
        System.out.println(ConsoleColor.YELLOW_BOLD + "Time : " + ConsoleColor.RESET + transactionFullInfoDTO.getTime());
        System.out.println(ConsoleColor.YELLOW_BOLD + "Sender : " + ConsoleColor.RESET + transactionFullInfoDTO.getSenderIban());
        System.out.println(ConsoleColor.YELLOW_BOLD + "Recipient : " + ConsoleColor.RESET + transactionFullInfoDTO.getRecipientIban());
        System.out.println(ConsoleColor.YELLOW_BOLD + "Currency : " + ConsoleColor.RESET + transactionFullInfoDTO.getCurrency());
        System.out.println(ConsoleColor.YELLOW_BOLD + "Sum without fee : " + ConsoleColor.RESET + transactionFullInfoDTO.getSum());
        System.out.println(ConsoleColor.YELLOW_BOLD + "Fee : " + ConsoleColor.RESET + transactionFullInfoDTO.getFee());
        System.out.println(ConsoleColor.YELLOW_BOLD + "Full sum : " + ConsoleColor.RESET + transactionFullInfoDTO.getFullSum());
    }
}
