package edu.bank.service.impl;

import edu.bank.dao.AccountRepository;
import edu.bank.dao.BankRepository;
import edu.bank.dao.TransactionRepository;
import edu.bank.dao.impl.AccountRepositoryImpl;
import edu.bank.dao.impl.BankRepositoryImpl;
import edu.bank.dao.impl.TransactionRepositoryImpl;
import edu.bank.model.dto.TransactionInfoDTO;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Bank;
import edu.bank.model.entity.Transaction;
import edu.bank.service.TransactionService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import static edu.bank.model.enm.CommandParam.*;

public class TransactionServiceImpl implements TransactionService {

    private final BankRepository bankRepository = new BankRepositoryImpl();
    private final AccountRepository accountRepository = new AccountRepositoryImpl();
    private final TransactionRepository transactionRepository = new TransactionRepositoryImpl();
    private static final String FROM_DATE_PARAM = FROM_DATE.getParamName();
    private static final String TO_DATE_PARAM = TO_DATE.getParamName();
    private static final String ACCOUNT_IBAN_PARAM = ACCOUNT_IBAN.getParamName();

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
    public void getTransactionHistory(Map<String, String> transactionsInfo) {
        if (!transactionsInfo.containsKey(ACCOUNT_IBAN_PARAM)) {
            System.out.println("Account number is not specified");
            return;
        }
        Account account = accountRepository.get(transactionsInfo.get(ACCOUNT_IBAN_PARAM));
        if (account == null){
            System.out.println("Invalid IBAN");
            return;
        }
        LocalDate fromDate = null;
        LocalDate toDate = null;
        try {
            fromDate = (transactionsInfo.containsKey(FROM_DATE_PARAM)) ? LocalDate.parse(transactionsInfo.get(FROM_DATE_PARAM)) :
                    account.getRegistrationDate();
            toDate = (transactionsInfo.containsKey(TO_DATE_PARAM)) ? LocalDate.parse(transactionsInfo.get(TO_DATE_PARAM)) :
                    LocalDate.now();
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date");
            return;
        }
        List<Transaction> transactions = transactionRepository.getAllBetweenDates(fromDate, toDate);
        if (transactions.isEmpty()) System.out.println("No transactions found between the specified numbers");
        else transactions.forEach(this::printReceipt);
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
        System.out.println(transactionInfoDTO);
    }
}
