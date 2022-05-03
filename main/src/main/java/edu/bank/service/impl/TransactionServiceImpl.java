package edu.bank.service.impl;

import edu.bank.command.CommandParamInspector;
import edu.bank.command.model.CommandParam;
import edu.bank.console.ConsoleCommandResultViewer;
import edu.bank.dao.AccountRepository;
import edu.bank.dao.TransactionRepository;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Transaction;
import edu.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final CommandParamInspector commandParamInspector;
    private final ConsoleCommandResultViewer commandResultViewer;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private static final String FROM_DATE_PARAM = "from";
    private static final String TO_DATE_PARAM = "to";
    private static final String ACCOUNT_IBAN_PARAM = "acc";

    @Override
    public Transaction createTransaction(Account fromAccount, Account toAccount, double sum, double fee) {
        Transaction transaction = new Transaction();
        transaction.setSenderAccount(fromAccount);
        transaction.setRecipientAccount(toAccount);
        transaction.setFullSum(sum);
        transaction.setFee(fee);
        transaction.setDate(LocalDate.now());
        transaction.setTime(LocalTime.now());
        return transactionRepository.create(transaction);
    }

    @Override
    public void getTransactionHistory(Set<CommandParam> transactionsInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{ACCOUNT_IBAN_PARAM}, transactionsInfo))
            throw new BusinessLogicException("Account number is not specified");
        String accountIban = commandParamInspector.getParamValueByNameIfPresent(ACCOUNT_IBAN_PARAM, transactionsInfo);
        Account account = accountRepository.get(accountIban);
        if (account == null) throw new BusinessLogicException("Invalid IBAN");
        LocalDate fromDate;
        LocalDate toDate;
        try {
            fromDate = (commandParamInspector.areCommandParamsContainsParam(FROM_DATE_PARAM, transactionsInfo)) ? LocalDate.parse(commandParamInspector.getParamValueByNameIfPresent(FROM_DATE_PARAM, transactionsInfo)) :
                    account.getRegistrationDate();
            toDate = (commandParamInspector.areCommandParamsContainsParam(TO_DATE_PARAM, transactionsInfo)) ? LocalDate.parse(commandParamInspector.getParamValueByNameIfPresent(TO_DATE_PARAM, transactionsInfo)) :
                    LocalDate.now();
        } catch (DateTimeParseException e) {
            throw new BusinessLogicException("Invalid date");
        }
        List<Transaction> transactions = transactionRepository.getAllBetweenDates(fromDate, toDate);
        if (transactions.isEmpty()) commandResultViewer.showFailureMessage("No transactions found between the specified numbers");
        else transactions.forEach(commandResultViewer::printReceipt);
    }
}
