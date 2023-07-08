package edu.bank.service;

import edu.bank.dao.AccountRepository;
import edu.bank.dao.BankRepository;
import edu.bank.dao.UserRepository;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.exeption.InternalError;
import edu.bank.model.dto.AccountMainInfo;
import edu.bank.model.dto.BankFullInfo;
import edu.bank.model.dto.BankToUpdate;
import edu.bank.model.dto.CreateBankClient;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Bank;
import edu.bank.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankService {

    private final BankRepository bankRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    public BankFullInfo createBank(Bank bank) throws BusinessLogicException {
        String name = bank.getName();
        String ibanPrefix = bank.getIbanPrefix();
        checkIfBankExistsByName(name, "create");
        checkIfBankExistsByIbanPrefix(ibanPrefix, "create");
        Bank createdBank = bankRepository.create(bank);
        log.info(bank + " has been successfully created");
        return modelMapper.map(createdBank, BankFullInfo.class);
    }

    public List<BankFullInfo> getAllBanks() {
        List<Bank> banks = bankRepository.getAll();
        if (banks == null || banks.isEmpty()) throw new InternalError("Not found");
        return banks.stream().map(this::mapFromBankToBankFullInfoDTO).toList();
    }

    public BankFullInfo getBank(Long id) throws BusinessLogicException {
        if (!bankRepository.isExists(id)) throw new BusinessLogicException("There is no bank with this ID");
        Bank bank = bankRepository.get(id);
        return mapFromBankToBankFullInfoDTO(bank);
    }

    public String updateBank(BankToUpdate bank) throws BusinessLogicException {
        long id = bank.getId();
        if (!bankRepository.isExists(id)) throw new BusinessLogicException("There is no bank with this ID");
        Bank bankToUpdate = bankRepository.get(id);
        String name = bank.getBank().getName();
        if (name != null) {
            checkIfBankExistsByName(name, "update");
            bankToUpdate.setName(name);
        }
        String ibanPrefix = bank.getBank().getIbanPrefix();
        if (ibanPrefix != null) {
            checkIfBankExistsByIbanPrefix(ibanPrefix, "update");
            bankToUpdate.setIbanPrefix(ibanPrefix);
        }
        double individualsFee = bank.getBank().getIndividualsFee();
        if (individualsFee != 0) {
            bankToUpdate.setIndividualsFee(individualsFee);
        }
        double legalEntitiesFee = bank.getBank().getLegalEntitiesFee();
        if (legalEntitiesFee != 0) {
            bankToUpdate.setLegalEntitiesFee(legalEntitiesFee);
        }
        bankRepository.update(id, bankToUpdate);
        log.info(bankToUpdate + " has been successfully updated");
        return String.format("Bank with id %d is successfully updated: %s", id, bankToUpdate);
    }

    public String deleteBank(Long id) throws BusinessLogicException {
        if (!bankRepository.isExists(id)) throw new BusinessLogicException("There is no bank with this ID");
        Bank bankToDelete = bankRepository.get(id);
        List<User> bankUsers = userRepository.getAllByTheBank(id);
        for (User user : bankUsers) {
            if (userRepository.getBanksCount(user.getId()) == 1) {
                userService.deleteUser(user.getId());
            } else {
                accountService.deleteAllUserAccountsOfSpecificBank(user.getId(), id);
            }
        }
        bankRepository.delete(id);
        log.info(bankToDelete + " has been successfully deleted from the system");
        return String.format("Bank with id %d is successfully deleted", id);
    }

    public AccountMainInfo addExistingUser(CreateBankClient createBankClient) throws BusinessLogicException {
        long bankId = createBankClient.getBankId();
        if (!bankRepository.isExists(bankId)) throw new BusinessLogicException("There is no bank with this ID");
        long userId = createBankClient.getUserId();
        if (!userRepository.isExists(userId)) throw new BusinessLogicException("There is no user with this ID");
        userRepository.createBankUser(bankId, userId);
        Account newAccount = accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, userId));
        AccountMainInfo newAccountMainInfo = modelMapper.map(newAccount, AccountMainInfo.class);
        newAccountMainInfo.setBankName(accountService.determineBankByIban(newAccount.getIban()).getName());
        log.info(String.format("Now the user(id = %d) is a customer of the bank(id = %d)", userId, bankId));
        return newAccountMainInfo;
    }

    private void checkIfBankExistsByName(String name, String operation) throws BusinessLogicException {
        if (bankRepository.getByName(name) != null) {
            log.info(String.format("Failed to %s a bank named %s because it already exists", operation, name));
            throw new BusinessLogicException("A bank with that name already exists");
        }
    }

    private void checkIfBankExistsByIbanPrefix(String ibanPrefix, String operation) throws BusinessLogicException {
        if (bankRepository.getByIbanPrefix(ibanPrefix) != null) {
            log.info(String.format("Failed to %s a bank with iban prefix %s because it already exists", operation, ibanPrefix));
            throw new BusinessLogicException("A bank with that iban prefix already exists");
        }
    }

    private BankFullInfo mapFromBankToBankFullInfoDTO(Bank bank) {
        BankFullInfo bankFullInfo = modelMapper.map(bank, BankFullInfo.class);
        bankFullInfo.setUsersCount(bankRepository.getUsersCount(bank.getId()));
        return bankFullInfo;
    }
}

