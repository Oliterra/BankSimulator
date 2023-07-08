package edu.bank.service;

import edu.bank.dao.AccountRepository;
import edu.bank.dao.BankRepository;
import edu.bank.dao.TransactionRepository;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.model.dto.TransactionFullInfo;
import edu.bank.model.dto.TransactionHistoryInfo;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Bank;
import edu.bank.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

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

    public List<TransactionFullInfo> getTransactionHistory(TransactionHistoryInfo transactionHistoryInfo) throws BusinessLogicException {
        String accountIban = transactionHistoryInfo.getAccountIban();
        if (!accountRepository.isExists(accountIban)) throw new BusinessLogicException("Invalid IBAN");
        Account account = accountRepository.get(accountIban);
        LocalDate fromDate = transactionHistoryInfo.getFromDate();
        if (fromDate == null) fromDate = account.getRegistrationDate();
        LocalDate toDate = transactionHistoryInfo.getToDate();
        if (toDate == null) toDate = LocalDate.now();
        List<Transaction> transactions = transactionRepository.getAllBetweenDates(fromDate, toDate);
        return transactions.stream().map(this::getReceipt).toList();
    }

    public TransactionFullInfo getReceipt(Transaction transaction) {
        TransactionFullInfo transactionFullInfo = new TransactionFullInfo();
        String recipientIban = transaction.getRecipientAccount().getIban();
        Bank recipientBank = bankRepository.getByIbanPrefix(recipientIban.substring(4, 8));
        transactionFullInfo.setRecipientIban(recipientIban);
        transactionFullInfo.setRecipientBankName(recipientBank.getName());
        transactionFullInfo.setId(transaction.getId());
        transactionFullInfo.setSenderIban(transaction.getSenderAccount().getIban());
        transactionFullInfo.setCurrency(transaction.getSenderAccount().getCurrency().toString());
        transactionFullInfo.setSum(transaction.getFullSum());
        transactionFullInfo.setFee(transaction.getFee());
        transactionFullInfo.setFullSum(transaction.getFullSum() + transaction.getFullSum() * transaction.getFee());
        transactionFullInfo.setDate(transaction.getDate());
        transactionFullInfo.setTime(transaction.getTime());
        return transactionFullInfo;
    }
}



