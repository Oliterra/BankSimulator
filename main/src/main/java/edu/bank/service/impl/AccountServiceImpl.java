package edu.bank.service.impl;

import edu.bank.dao.AccountRepository;
import edu.bank.dao.BankRepository;
import edu.bank.dao.UserRepository;
import edu.bank.dao.impl.AccountRepositoryImpl;
import edu.bank.dao.impl.BankRepositoryImpl;
import edu.bank.dao.impl.UserRepositoryImpl;
import edu.bank.model.dto.AccountMainInfoDTO;
import edu.bank.model.enm.Currency;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Bank;
import edu.bank.model.entity.Transaction;
import edu.bank.exeption.UnexpectedInternalError;
import edu.bank.service.AccountService;
import edu.bank.service.TransactionService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.bank.model.enm.CommandParam.*;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository = new AccountRepositoryImpl();
    private final BankRepository bankRepository = new BankRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final TransactionService transactionService = new TransactionServiceImpl();
    private static final String IBAN_START_STRING = "BY";
    private static final String USER_ID_PARAM = USER_ID.getParamName();
    private static final String BANK_ID_PARAM = BANK_ID.getParamName();
    private static final String ACCOUNT_CURRENCY_PARAM = ACCOUNT_CURRENCY.getParamName();
    private static final String FROM_ACCOUNT_PARAM = FROM_ACCOUNT.getParamName();
    private static final String TO_ACCOUNT_PARAM = TO_ACCOUNT.getParamName();
    private static final String MONEY_AMOUNT_PARAM = MONEY_AMOUNT.getParamName();

    @Override
    public void createNewAccount(Map<String, String> accountInfo) throws IOException {
        if (!accountInfo.containsKey(USER_ID_PARAM) || !accountInfo.containsKey(BANK_ID_PARAM)) {
            System.out.println("Bank or user id is missing");
            return;
        }
        long bankId = Long.parseLong(accountInfo.get(BANK_ID_PARAM));
        long userId = Long.parseLong(accountInfo.get(USER_ID_PARAM));
        if (!userRepository.isUserBankClient(bankId, userId)) {
            System.out.println("The user is not a customer of the bank");
            return;
        }
        Currency currency = null;
        if (accountInfo.containsKey(ACCOUNT_CURRENCY_PARAM)) {
            try {
                currency = Currency.valueOf(accountInfo.get(ACCOUNT_CURRENCY_PARAM));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid currency format");
                return;
            }
        }
        Account account = new Account();
        account.setBalance(0);
        account.setUser(userRepository.get(userId));
        account.setCurrency(currency);
        account.setRegistrationDate(LocalDate.now());
        String newIban = generateIban(bankId);
        account.setIban(newIban);
        accountRepository.create(account);
    }

    @Override
    public Account getDefaultAccountForNewUser(long bankId, long userId) throws IOException {
        Account defaultAccount = new Account();
        defaultAccount.setBalance(0);
        defaultAccount.setUser(userRepository.get(userId));
        defaultAccount.setCurrency(Currency.USD);
        defaultAccount.setRegistrationDate(LocalDate.now());
        String newIban = generateIban(bankId);
        defaultAccount.setIban(newIban);
        return defaultAccount;
    }

    @Override
    public void getAllByUser(Map<String, String> info) throws IOException {
        if (!info.containsKey(USER_ID_PARAM)) throw new IOException();
        long userId = Long.parseLong(info.get(USER_ID_PARAM));
        List<Account> accounts;
        if (info.containsKey(ACCOUNT_CURRENCY_PARAM)) {
            Currency currency = Currency.valueOf(info.get(ACCOUNT_CURRENCY_PARAM));
            accounts = accountRepository.getAllByUserIdAndCurrency(userId, currency);
        } else accounts = accountRepository.getAllByUserId(userId);
        List<AccountMainInfoDTO> accountsInfo = new ArrayList<>();
        accounts.forEach(a -> accountsInfo.add(new AccountMainInfoDTO(a.getIban(), a.getCurrency(), a.getBalance(),
                determineBankByIban(a.getIban()).getName())));
        if (!accountsInfo.isEmpty()) accountsInfo.forEach(System.out::println);
        else System.out.println("Not found");
    }

    @Override
    public void transferMoney(Map<String, String> transferInfo) throws IOException {
        if (!transferInfo.containsKey(FROM_ACCOUNT_PARAM) || !transferInfo.containsKey(TO_ACCOUNT_PARAM) ||
                !transferInfo.containsKey(MONEY_AMOUNT_PARAM)) throw new IOException();
        String fromAccountIban = transferInfo.get(FROM_ACCOUNT_PARAM);
        String toAccountIban = transferInfo.get(TO_ACCOUNT_PARAM);
        double sum = Double.parseDouble(transferInfo.get(MONEY_AMOUNT_PARAM));
        Bank fromBank = determineBankByIban(fromAccountIban);
        Bank toBank = determineBankByIban(toAccountIban);
        if (fromBank.equals(toBank)) makeIntrabankTransfer(fromAccountIban, toAccountIban, sum);
        else makeBetweenBanksTransfer(fromBank, fromAccountIban, toAccountIban, sum);
    }

    private void makeIntrabankTransfer(String fromAccountIban, String toAccountIban, double sum) {
        Account fromAccount = accountRepository.get(fromAccountIban);
        Account toAccount = accountRepository.get(toAccountIban);
        if (fromAccount == null || fromAccount == null) {
            System.out.println("Invalid IBAN");
            return;
        }
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            System.out.println("The function of transferring between different currencies is still under development.");
            return;
        }
        double fromAccountBalance = fromAccount.getBalance();
        if (fromAccountBalance < sum) {
            System.out.println(String.format("Insufficient funds.\nTransfer amount: %f\nThe balance on the account: %f",
                    sum, fromAccountBalance));
            return;
        }
        double toAccountBalance = toAccount.getBalance();
        fromAccountBalance -= sum;
        toAccountBalance += sum;
        accountRepository.transferMoney(fromAccountIban, toAccountIban, fromAccountBalance, toAccountBalance);
        Transaction transaction = transactionService.createTransaction(fromAccount, toAccount, sum, 0);
        transactionService.printReceipt(transaction);
    }

    private void makeBetweenBanksTransfer(Bank bank, String fromAccountIban, String toAccountIban, double sum) {
        Account fromAccount = accountRepository.get(fromAccountIban);
        Account toAccount = accountRepository.get(toAccountIban);
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            System.out.println("The function of transferring between different currencies is still under development.");
            return;
        }
        double fee;
        fee = (userRepository.isIndividual(fromAccount.getUser().getId())) ? bank.getIndividualsFee() : bank.getLegalEntitiesFee();
        if (fee <= 0 || fee > 1) throw new UnexpectedInternalError();
        double sumWithFee = (sum * fee) + sum;
        double fromAccountBalance = fromAccount.getBalance();
        if (fromAccountBalance < sumWithFee) {
            System.out.println(String.format("Insufficient funds.\nTransfer sum: %f\nFee: %f\nTransfer sum + fee: %f\n" +
                    "\nThe balance on the account: %f", sum, fee, sumWithFee, fromAccountBalance));
            return;
        }
        double toAccountBalance = toAccount.getBalance();
        fromAccountBalance -= sumWithFee;
        toAccountBalance += sum;
        accountRepository.transferMoney(fromAccountIban, toAccountIban, fromAccountBalance, toAccountBalance);
        Transaction transaction = transactionService.createTransaction(fromAccount, toAccount, sum, fee);
        transactionService.printReceipt(transaction);
    }

    public String generateIban(long bankId) throws IOException {
        String ibanPrefix = bankRepository.getIbanPrefixById(bankId);
        if (ibanPrefix == null) throw new IOException();
        StringBuilder iban = new StringBuilder();
        iban.append(IBAN_START_STRING).append(generateRandomNumber(2)).append(ibanPrefix).append(generateRandomNumber(20));
        return iban.toString();
    }

    private Bank determineBankByIban(String iban) {
        String ibanPrefix = iban.substring(4, 8);
        return bankRepository.getByIbanPrefix(ibanPrefix);
    }

    private String generateRandomNumber(int length) {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < length; i++) number.append((int) (Math.random() * 9));
        return number.toString();
    }
}
