package edu.bank.result.mapper;

import edu.bank.model.dto.TransactionFullInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.CommandResultMapper;

public class TransactionFullInfoResultMapper implements CommandResultMapper<TransactionFullInfo> {

    @Override
    public String mapResult(TransactionFullInfo transactionFullInfo) {
        return ConsoleColor.YELLOW_BOLD + "\nReceipt:"
                + ConsoleColor.MAGENTA_BOLD + "\nID: " + ConsoleColor.RESET + transactionFullInfo.getId()
                + ConsoleColor.MAGENTA_BOLD + "\nBank : " + ConsoleColor.RESET + transactionFullInfo.getRecipientBankName()
                + ConsoleColor.MAGENTA_BOLD + "\nDate : " + ConsoleColor.RESET + transactionFullInfo.getDate()
                + ConsoleColor.MAGENTA_BOLD + "\nTime : " + ConsoleColor.RESET + transactionFullInfo.getTime()
                + ConsoleColor.MAGENTA_BOLD + "\nSender : " + ConsoleColor.RESET + transactionFullInfo.getSenderIban()
                + ConsoleColor.MAGENTA_BOLD + "\nRecipient : " + ConsoleColor.RESET + transactionFullInfo.getRecipientIban()
                + ConsoleColor.MAGENTA_BOLD + "\nCurrency : " + ConsoleColor.RESET + transactionFullInfo.getCurrency()
                + ConsoleColor.MAGENTA_BOLD + "\nSum without fee : " + ConsoleColor.RESET + transactionFullInfo.getSum()
                + ConsoleColor.MAGENTA_BOLD + "\nFee : " + ConsoleColor.RESET + transactionFullInfo.getFee()
                + ConsoleColor.MAGENTA_BOLD + "\nFull sum : " + ConsoleColor.RESET + transactionFullInfo.getFullSum();
    }
}
