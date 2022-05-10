package edu.bank.service;

import edu.bank.dao.AccountRepository;
import edu.bank.dao.BankRepository;
import edu.bank.dao.TransactionRepository;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.model.dto.TransactionFullInfoDTO;
import edu.bank.model.dto.TransactionHistoryInfoDTO;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Bank;
import edu.bank.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    public List<TransactionFullInfoDTO> getTransactionHistory(TransactionHistoryInfoDTO transactionHistoryInfoDTO) {
        String accountIban = transactionHistoryInfoDTO.getAccountIban();
        if (!accountRepository.isExists(accountIban)) throw new BusinessLogicException("Invalid IBAN");
        Account account = accountRepository.get(accountIban);
        LocalDate fromDate = transactionHistoryInfoDTO.getFromDate();
        if (fromDate == null) fromDate = account.getRegistrationDate();
        LocalDate toDate = transactionHistoryInfoDTO.getToDate();
        if (toDate == null) toDate = LocalDate.now();
        List<Transaction> transactions = transactionRepository.getAllBetweenDates(fromDate, toDate);
        return transactions.stream().map(this::getReceipt).toList();
    }

    public TransactionFullInfoDTO getReceipt(Transaction transaction) {
        TransactionFullInfoDTO transactionFullInfoDTO = new TransactionFullInfoDTO();
        String recipientIban = transaction.getRecipientAccount().getIban();
        Bank recipientBank = bankRepository.getByIbanPrefix(recipientIban.substring(4, 8));
        transactionFullInfoDTO.setRecipientIban(recipientIban);
        transactionFullInfoDTO.setRecipientBankName(recipientBank.getName());
        transactionFullInfoDTO.setId(transaction.getId());
        transactionFullInfoDTO.setSenderIban(transaction.getSenderAccount().getIban());
        transactionFullInfoDTO.setCurrency(transaction.getSenderAccount().getCurrency().toString());
        transactionFullInfoDTO.setSum(transaction.getFullSum());
        transactionFullInfoDTO.setFee(transaction.getFee());
        transactionFullInfoDTO.setFullSum(transaction.getFullSum() + transaction.getFullSum() * transaction.getFee());
        transactionFullInfoDTO.setDate(transaction.getDate());
        transactionFullInfoDTO.setTime(transaction.getTime());
        return transactionFullInfoDTO;
    }
}



