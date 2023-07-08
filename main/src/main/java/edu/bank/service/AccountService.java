package edu.bank.service;

import edu.bank.dao.AccountRepository;
import edu.bank.dao.BankRepository;
import edu.bank.dao.TransactionRepository;
import edu.bank.dao.UserRepository;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.model.dto.*;
import edu.bank.model.enm.Currency;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Bank;
import edu.bank.model.entity.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;
    private final ModelMapper modelMapper;
    private static final String IBAN_START_STRING = "BY";

    public AccountMainInfo createNewAccount(CreateAccount createAccount) throws BusinessLogicException {
        long bankId = createAccount.getBankId();
        if (!bankRepository.isExists(bankId)) throw new BusinessLogicException("There is no bank with this ID");
        long userId = createAccount.getUserId();
        if (!userRepository.isExists(userId)) throw new BusinessLogicException("There is no user with this ID");
        if (!userRepository.isBankClient(bankId, userId))
            throw new BusinessLogicException("The user is not a client of the bank");
        Account account = new Account();
        account.setBalance(0);
        account.setUser(userRepository.get(userId));
        Currency currency = Currency.USD;
        String currencyString = createAccount.getCurrency();
        if (currencyString != null) currency = Currency.valueOf(currencyString);
        account.setCurrency(currency);
        account.setRegistrationDate(LocalDate.now());
        String newIban = generateIban(bankId);
        account.setIban(newIban);
        Account createdAccount = accountRepository.create(account);
        AccountMainInfo createdAccountMainInfo = modelMapper.map(createdAccount, AccountMainInfo.class);
        createdAccountMainInfo.setBankName(determineBankByIban(createdAccount.getIban()).getName());
        log.info(account + " has been successfully created");
        return createdAccountMainInfo;
    }

    public List<AccountMainInfo> getAllByUser(UserAndAccountMainInfo userAndAccountMainInfo) {
        long userId = userAndAccountMainInfo.getUserId();
        List<Account> accounts;
        String currencyString = userAndAccountMainInfo.getCurrency();
        if (currencyString != null) {
            Currency currency = Currency.valueOf(userAndAccountMainInfo.getCurrency());
            accounts = accountRepository.getAllByUserIdAndCurrency(userId, currency);
        } else accounts = accountRepository.getAllByUserId(userId);
        List<AccountMainInfo> accountsInfo = new ArrayList<>();
        accounts.forEach(a -> accountsInfo.add(new AccountMainInfo(a.getIban(), a.getCurrency(), a.getBalance(), determineBankByIban(a.getIban()).getName())));
        return accountsInfo;
    }

    public List<AccountMainInfo> getAllByUser(long id) throws BusinessLogicException {
        if (!userRepository.isExists(id)) throw new BusinessLogicException("There is no user with this ID");
        List<Account> accounts = accountRepository.getAllByUserId(id);
        if (accounts == null || accounts.isEmpty()) throw new BusinessLogicException("The user has no accounts");
        return mapFromAccountsToAccountMainInfoDTO(accounts);
    }

    public Account getDefaultAccountForNewUser(long bankId, long userId) throws BusinessLogicException {
        Account defaultAccount = new Account();
        defaultAccount.setBalance(0);
        defaultAccount.setUser(userRepository.get(userId));
        defaultAccount.setCurrency(Currency.USD);
        defaultAccount.setRegistrationDate(LocalDate.now());
        String newIban = generateIban(bankId);
        defaultAccount.setIban(newIban);
        return defaultAccount;
    }

    public void deleteAccount(String iban) throws BusinessLogicException {
        if (!accountRepository.isExists(iban)) throw new BusinessLogicException("There is no account with this iban");
        transactionRepository.deleteBySenderAccountIban(iban);
        transactionRepository.deleteByRecipientAccountIban(iban);
        accountRepository.delete(iban);
    }

    public void deleteAllUserAccountsOfSpecificBank(long userId, long bankId) throws BusinessLogicException {
        if (!bankRepository.isExists(bankId)) throw new BusinessLogicException("There is no bank with this id");
        if (!userRepository.isExists(bankId)) throw new BusinessLogicException("There is no user with this id");
        List<Account> userAccounts = accountRepository.getAllByUserId(userId);
        String bankIbanPrefix = bankRepository.getIbanPrefixById(bankId);
        for (Account account : userAccounts) {
            if (account.getIban().substring(4, 8).equals(bankIbanPrefix)) {
                deleteAccount(account.getIban());
            } else {
                throw new InternalError("Unknown bank IBAN prefix in account IBAN: " + account);
            }
        }
        log.info(String.format("All user(id = %d) accounts of the bank(id =%d) have been deleted", userId, bankId));
    }

    public Bank determineBankByIban(String iban) {
        String ibanPrefix = iban.substring(4, 8);
        return bankRepository.getByIbanPrefix(ibanPrefix);
    }

    public TransactionFullInfo transferMoney(TransferMoneyInfo transferMoneyInfo) throws BusinessLogicException {
        String fromAccountIban = transferMoneyInfo.getFromIban();
        String toAccountIban = transferMoneyInfo.getToIban();
        if (!accountRepository.isExists(fromAccountIban) || !accountRepository.isExists(toAccountIban))
            throw new BusinessLogicException("Invalid IBAN");
        double sum = transferMoneyInfo.getSum();
        Bank fromBank = determineBankByIban(fromAccountIban);
        Bank toBank = determineBankByIban(toAccountIban);
        if (fromBank.equals(toBank)) return makeIntrabankTransfer(fromAccountIban, toAccountIban, sum);
        else return makeBetweenBanksTransfer(fromBank, fromAccountIban, toAccountIban, sum);
    }

    private TransactionFullInfo makeIntrabankTransfer(String fromAccountIban, String toAccountIban, double sum) throws BusinessLogicException {
        Account fromAccount = accountRepository.get(fromAccountIban);
        Account toAccount = accountRepository.get(toAccountIban);
        if (fromAccount == null || toAccount == null) throw new BusinessLogicException("Invalid IBAN");
        if (!fromAccount.getCurrency().equals(toAccount.getCurrency()))
            throw new BusinessLogicException("The function of transferring between different currencies is still under development");
        double fromAccountBalance = fromAccount.getBalance();
        if (fromAccountBalance < sum)
            throw new BusinessLogicException(String.format("""
                    Insufficient funds.
                    Transfer amount: %f
                    The balance on the account: %f""", sum, fromAccountBalance));
        double toAccountBalance = toAccount.getBalance();
        fromAccountBalance -= sum;
        toAccountBalance += sum;
        accountRepository.transferMoney(fromAccountIban, toAccountIban, fromAccountBalance, toAccountBalance);
        Transaction transaction = transactionService.createTransaction(fromAccount, toAccount, sum, 0);
        TransactionFullInfo transactionFullInfo = transactionService.getReceipt(transaction);
        log.info("A new transaction has been made: " + transactionFullInfo);
        return transactionFullInfo;
    }

    private TransactionFullInfo makeBetweenBanksTransfer(Bank bank, String fromAccountIban, String toAccountIban, double sum) throws BusinessLogicException {
        Account fromAccount = accountRepository.get(fromAccountIban);
        Account toAccount = accountRepository.get(toAccountIban);
        if (fromAccount == null || toAccount == null) throw new BusinessLogicException("Invalid IBAN");
        double fee = (userRepository.isIndividual(fromAccount.getUser().getId())) ? bank.getIndividualsFee() : bank.getLegalEntitiesFee();
        double sumWithFee = (sum * fee) + sum;
        double fromAccountBalance = fromAccount.getBalance();
        if (fromAccountBalance < sumWithFee)
            throw new BusinessLogicException(String.format("""
                    Insufficient funds.
                    Transfer sum: %f
                    Fee: %f
                    Transfer sum + fee: %f
                    The balance on the account: %f""", sum, fee, sumWithFee, fromAccountBalance));
        double toAccountBalance = toAccount.getBalance();
        fromAccountBalance -= sumWithFee;
        toAccountBalance += sum;
        accountRepository.transferMoney(fromAccountIban, toAccountIban, fromAccountBalance, toAccountBalance);
        Transaction transaction = transactionService.createTransaction(fromAccount, toAccount, sum, fee);
        TransactionFullInfo transactionFullInfo = transactionService.getReceipt(transaction);
        log.info("A new transaction has been made: " + transactionFullInfo);
        return transactionFullInfo;
    }

    private List<AccountMainInfo> mapFromAccountsToAccountMainInfoDTO(List<Account> accounts) {
        List<AccountMainInfo> accountsInfo = new ArrayList<>();
        accounts.forEach(a -> accountsInfo.add(new AccountMainInfo(a.getIban(), a.getCurrency(), a.getBalance(), determineBankByIban(a.getIban()).getName())));
        return accountsInfo;
    }

    private String generateIban(long bankId) throws BusinessLogicException {
        String ibanPrefix = bankRepository.getIbanPrefixById(bankId);
        if (ibanPrefix == null) throw new BusinessLogicException("Missing IBAN");
        return IBAN_START_STRING + generateRandomNumber(2) + ibanPrefix + generateRandomNumber(20);
    }

    private String generateRandomNumber(int length) {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < length; i++) number.append((int) (Math.random() * 9));
        return number.toString();
    }
}