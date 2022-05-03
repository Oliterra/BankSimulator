package edu.bank.console;

import edu.bank.command.model.CommandResult;
import edu.bank.dao.BankRepository;
import edu.bank.model.dto.TransactionInfoDTO;
import edu.bank.model.enm.CommandResultStatus;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.model.entity.Bank;
import edu.bank.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsoleCommandResultViewerImpl implements ConsoleCommandResultViewer {

    private BankRepository bankRepository;
    @Override
    public void showSuccessMessage(String message) {
        CommandResult commandResult = new CommandResult(message, CommandResultStatus.SUCCESS, ConsoleColor.GREEN_BOLD);
        System.out.println(commandResult);
    }

    @Override
    public void showFailureMessage(String message) {
        CommandResult commandResult = new CommandResult(message, CommandResultStatus.FAILURE, ConsoleColor.RED_BOLD);
        System.out.println(commandResult);
    }

    @Override
    public void printReceipt(Transaction transaction) {
        TransactionInfoDTO transactionInfoDTO = new TransactionInfoDTO();
        String recipientIban = transaction.getRecipientAccount().getIban();
        Bank recipientBank = bankRepository.getByIbanPrefix(recipientIban.substring(4, 8));
        transactionInfoDTO.setRecipientIban(recipientIban);
        transactionInfoDTO.setRecipientBankName(recipientBank.getName());
        transactionInfoDTO.setId(transaction.getId());
        transactionInfoDTO.setSenderIban(transaction.getSenderAccount().getIban());
        transactionInfoDTO.setCurrency(transaction.getSenderAccount().getCurrency().toString());
        transactionInfoDTO.setSum(transaction.getFullSum());
        transactionInfoDTO.setFee(transaction.getFee());
        transactionInfoDTO.setFullSum(transaction.getFullSum() + transaction.getFullSum() * transaction.getFee());
        transactionInfoDTO.setDate(transaction.getDate());
        transactionInfoDTO.setTime(transaction.getTime());
        showSuccessMessage(transactionInfoDTO.toString());
    }
}
