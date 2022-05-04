package edu.bank.service.impl;

import edu.bank.command.CommandParamInspector;
import edu.bank.command.model.CommandParam;
import edu.bank.console.ConsoleCommandResultViewer;
import edu.bank.dao.AccountRepository;
import edu.bank.dao.BankRepository;
import edu.bank.dao.TransactionRepository;
import edu.bank.dao.UserRepository;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.exeption.DAOException;
import edu.bank.model.dto.AccountMainInfoDTO;
import edu.bank.model.enm.Currency;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Bank;
import edu.bank.model.entity.Transaction;
import edu.bank.service.AccountService;
import edu.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class AccountServiceImpl implements AccountService {

    private final CommandParamInspector commandParamInspector;
    private final ConsoleCommandResultViewer commandResultViewer;
    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private static final String IBAN_START_STRING = "BY";
    private static final String USER_ID_PARAM = "uId";
    private static final String BANK_ID_PARAM = "bId";
    private static final String ACCOUNT_CURRENCY_PARAM = "cur";
    private static final String FROM_ACCOUNT_PARAM = "from";
    private static final String TO_ACCOUNT_PARAM = "to";
    private static final String MONEY_AMOUNT_PARAM = "sum";

    @Override
    public void createNewAccount(Set<CommandParam> accountInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{USER_ID_PARAM, BANK_ID_PARAM}, accountInfo))
            throw new BusinessLogicException("Bank or user id is missing");
        long bankId = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(BANK_ID_PARAM, accountInfo));
        if (!bankRepository.isExists(bankId)) throw new BusinessLogicException("There is no bank with this ID");
        long userId = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(USER_ID_PARAM, accountInfo));
        if (!userRepository.isExists(userId)) throw new BusinessLogicException("There is no user with this ID");
        if (!userRepository.isBankClient(bankId, userId))
            throw new BusinessLogicException("The user is not a customer of the bank");
        Currency currency = Currency.USD;
        if (commandParamInspector.areCommandParamsContainsParam(ACCOUNT_CURRENCY_PARAM, accountInfo))
            currency = getCurrencyByName(accountInfo);
        Account account = new Account();
        account.setBalance(0);
        account.setUser(userRepository.get(userId));
        account.setCurrency(currency);
        account.setRegistrationDate(LocalDate.now());
        String newIban = generateIban(bankId);
        account.setIban(newIban);
        accountRepository.create(account);
        commandResultViewer.showSuccessMessage(String.format("New account for user with id %d has been successfully created. Account IBAN: %s", userId, newIban));
        log.info(account + " has been successfully created");
    }

    @Override
    public void getAllByUser(Set<CommandParam> info) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{USER_ID_PARAM}, info))
            throw new BusinessLogicException("User ID not specified");
        long userId = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(USER_ID_PARAM, info));
        List<Account> accounts;
        if (commandParamInspector.areCommandParamsContainsParam(ACCOUNT_CURRENCY_PARAM, info)) {
            Currency currency = getCurrencyByName(info);
            accounts = accountRepository.getAllByUserIdAndCurrency(userId, currency);
        } else accounts = accountRepository.getAllByUserId(userId);
        List<AccountMainInfoDTO> accountsInfo = new ArrayList<>();
        accounts.forEach(a -> accountsInfo.add(new AccountMainInfoDTO(a.getIban(), a.getCurrency(), a.getBalance(), determineBankByIban(a.getIban()).getName())));
        if (!accountsInfo.isEmpty()) commandResultViewer.showSuccessMessage(accountsInfo.toString());
        else commandResultViewer.showFailureMessage("Not found");
    }

    public List<AccountMainInfoDTO> getAllByUser(long id) {
        if (!userRepository.isExists(id)) throw new BusinessLogicException("There is no user with this ID");
        List<Account> accounts = accountRepository.getAllByUserId(id);
        if (accounts == null || accounts.isEmpty()) throw new BusinessLogicException("The user has no accounts");
        return mapFromAccountsToAccountMainInfoDTO(accounts);
    }

    @Override
    public Account getDefaultAccountForNewUser(long bankId, long userId) {
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
    public void deleteAccount(String iban) {
        if (!accountRepository.isExists(iban)) throw new BusinessLogicException("There is no account with this iban");
        transactionRepository.deleteBySenderAccountIban(iban);
        transactionRepository.deleteByRecipientAccountIban(iban);
        accountRepository.delete(iban);
    }

    @Override
    public void deleteAllUserAccountsOfSpecificBank(long userId, long bankId) {
        if (!bankRepository.isExists(bankId)) throw new BusinessLogicException("There is no bank with this id");
        if (!userRepository.isExists(bankId)) throw new BusinessLogicException("There is no user with this id");
        List<Account> userAccounts = accountRepository.getAllByUserId(userId);
        String bankIbanPrefix = bankRepository.getIbanPrefixById(bankId);
        userAccounts.stream().filter(a -> a.getIban().substring(4, 8).equals(bankIbanPrefix)).forEach(a -> deleteAccount(a.getIban()));
        log.info(String.format("All user(id = %d) accounts of the bank(id =%d) have been deleted", userId, bankId));
    }

    @Override
    public void transferMoney(Set<CommandParam> transferInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{FROM_ACCOUNT_PARAM, TO_ACCOUNT_PARAM, MONEY_AMOUNT_PARAM}, transferInfo))
            throw new BusinessLogicException("Not enough information to translate");
        String fromAccountIban = commandParamInspector.getParamValueByNameIfPresent(FROM_ACCOUNT_PARAM, transferInfo);
        String toAccountIban = commandParamInspector.getParamValueByNameIfPresent(TO_ACCOUNT_PARAM, transferInfo);
        if (!accountRepository.isExists(fromAccountIban) || !accountRepository.isExists(toAccountIban))
            throw new BusinessLogicException("Invalid IBAN");
        double sum = Double.parseDouble(commandParamInspector.getParamValueByNameIfPresent(MONEY_AMOUNT_PARAM, transferInfo));
        Bank fromBank = determineBankByIban(fromAccountIban);
        Bank toBank = determineBankByIban(toAccountIban);
        if (fromBank.equals(toBank)) makeIntrabankTransfer(fromAccountIban, toAccountIban, sum);
        else makeBetweenBanksTransfer(fromBank, fromAccountIban, toAccountIban, sum);
    }

    private void makeIntrabankTransfer(String fromAccountIban, String toAccountIban, double sum) {
        Account fromAccount = accountRepository.get(fromAccountIban);
        Account toAccount = accountRepository.get(toAccountIban);
        if (fromAccount == null || toAccount == null) throw new BusinessLogicException("Invalid IBAN");
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency()))
            throw new BusinessLogicException("The function of transferring between different currencies is still under development");
        double fromAccountBalance = fromAccount.getBalance();
        if (fromAccountBalance < sum)
            throw new BusinessLogicException(String.format("Insufficient funds.\n" + "Transfer amount: %f\nThe balance on the account: %f", sum, fromAccountBalance));
        double toAccountBalance = toAccount.getBalance();
        fromAccountBalance -= sum;
        toAccountBalance += sum;
        accountRepository.transferMoney(fromAccountIban, toAccountIban, fromAccountBalance, toAccountBalance);
        Transaction transaction = transactionService.createTransaction(fromAccount, toAccount, sum, 0);
        commandResultViewer.printReceipt(transaction);
        log.info("A new transaction has been made: " + transaction);
    }

    private void makeBetweenBanksTransfer(Bank bank, String fromAccountIban, String toAccountIban, double sum) {
        Account fromAccount = accountRepository.get(fromAccountIban);
        Account toAccount = accountRepository.get(toAccountIban);
        if (fromAccount == null || toAccount == null) throw new BusinessLogicException("Invalid IBAN");
        double fee = (userRepository.isIndividual(fromAccount.getUser().getId())) ? bank.getIndividualsFee() : bank.getLegalEntitiesFee();
        if (fee <= 0 || fee > 1) throw new DAOException();
        double sumWithFee = (sum * fee) + sum;
        double fromAccountBalance = fromAccount.getBalance();
        if (fromAccountBalance < sumWithFee)
            throw new BusinessLogicException(String.format("Insufficient funds.\nTransfer sum: %f\nFee: %f\nTransfer sum + fee: %f\n" + "\nThe balance on the account: %f", sum, fee, sumWithFee, fromAccountBalance));
        double toAccountBalance = toAccount.getBalance();
        fromAccountBalance -= sumWithFee;
        toAccountBalance += sum;
        accountRepository.transferMoney(fromAccountIban, toAccountIban, fromAccountBalance, toAccountBalance);
        Transaction transaction = transactionService.createTransaction(fromAccount, toAccount, sum, fee);
        commandResultViewer.printReceipt(transaction);
        log.info("A new transaction has been made: " + transaction);
    }

    private List<AccountMainInfoDTO> mapFromAccountsToAccountMainInfoDTO(List<Account> accounts) {
        List<AccountMainInfoDTO> accountsInfo = new ArrayList<>();
        accounts.forEach(a -> accountsInfo.add(new AccountMainInfoDTO(a.getIban(), a.getCurrency(), a.getBalance(), determineBankByIban(a.getIban()).getName())));
        return accountsInfo;
    }

    private Currency getCurrencyByName(Set<CommandParam> accountInfo) {
        try {
            return Currency.valueOf(commandParamInspector.getParamValueByNameIfPresent(ACCOUNT_CURRENCY_PARAM, accountInfo));
        } catch (IllegalArgumentException e) {
            throw new BusinessLogicException("Invalid currency name");
        }
    }

    private String generateIban(long bankId) {
        String ibanPrefix = bankRepository.getIbanPrefixById(bankId);
        if (ibanPrefix == null) throw new BusinessLogicException("Missing IBAN");
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
