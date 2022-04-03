package edu.bank.service.impl;

import edu.bank.dao.AccountRepository;
import edu.bank.dao.BankRepository;
import edu.bank.dao.UserRepository;
import edu.bank.dao.impl.AccountRepositoryImpl;
import edu.bank.dao.impl.BankRepositoryImpl;
import edu.bank.dao.impl.UserRepositoryImpl;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.model.dto.BankFullInfoDTO;
import edu.bank.model.entity.Bank;
import edu.bank.model.entity.User;
import edu.bank.service.AccountService;
import edu.bank.service.BankService;
import edu.bank.service.CommandManager;
import edu.bank.service.UserService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

import static edu.bank.model.enm.CommandParam.*;

public class BankServiceImpl implements BankService, CommandManager {

    private final BankRepository bankRepository = new BankRepositoryImpl();
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final AccountRepository accountRepository = new AccountRepositoryImpl();
    private final UserService userService = new UserServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private static final Logger log = Logger.getLogger(BankServiceImpl.class);
    private static final String ID_PARAM = BANK_ID.getParamName();
    private static final String USER_ID_PARAM = USER_ID.getParamName();
    private static final String NAME_PARAM = BANK_NAME.getParamName();
    private static final String IBAN_PREFIX_PARAM = IBAN_PREFIX.getParamName();
    private static final String INDIVIDUALS_FEE_PARAM = INDIVIDUALS_FEE.getParamName();
    private static final String LEGAL_ENTITIES_FEE_PARAM = LEGAL_ENTITIES_FEE.getParamName();

    @Override
    public void createBank(Map<String, String> bankInfo) {
        if (!areAllParamsPresent(new String[]{NAME_PARAM, IBAN_PREFIX_PARAM, INDIVIDUALS_FEE_PARAM, LEGAL_ENTITIES_FEE_PARAM},
                bankInfo)) throw new BusinessLogicException("There is not enough information to create a bank");
        String name = bankInfo.get(NAME_PARAM);
        String ibanPrefix = bankInfo.get(IBAN_PREFIX_PARAM);
        if (ibanPrefix.length() != 4) throw new BusinessLogicException("The IBAN prefix must contain 4 characters");
        checkIfBankExistsByName(name, "create");
        checkIfBankExistsByIbanPrefix(ibanPrefix, "create");
        double individualsFee = Double.parseDouble(bankInfo.get(INDIVIDUALS_FEE_PARAM));
        double legalEntitiesFee = Double.parseDouble(bankInfo.get(LEGAL_ENTITIES_FEE_PARAM));
        if (individualsFee >= 1 || legalEntitiesFee >= 1)
            throw new BusinessLogicException("The fee must be less than 1");
        Bank newBank = new Bank();
        newBank.setName(name);
        newBank.setIbanPrefix(ibanPrefix);
        newBank.setIndividualsFee(individualsFee);
        newBank.setLegalEntitiesFee(legalEntitiesFee);
        bankRepository.create(newBank);
        System.out.println(String.format("Bank named %s has been successfully created", name));
        log.info(newBank + " has been successfully created");
    }

    @Override
    public void getAllBanks(Map<String, String> bankInfo) {
        List<Bank> banks = bankRepository.getAll();
        if (banks == null || banks.isEmpty()) throw new BusinessLogicException("Not found");
        System.out.println("All banks: ");
        banks.stream().map(b -> mapFromBankToBankFullInfoDTO(b.getId())).forEach(System.out::println);
    }

    @Override
    public void getBank(Map<String, String> bankInfo) {
        if (!areAllParamsPresent(new String[]{ID_PARAM}, bankInfo))
            throw new BusinessLogicException("Bank ID not specified");
        long id = Long.parseLong(bankInfo.get(ID_PARAM));
        if (!bankRepository.isExists(id)) throw new BusinessLogicException("There is no bank with this ID");
        else System.out.println(mapFromBankToBankFullInfoDTO(id));
    }

    @Override
    public void updateBank(Map<String, String> bankInfo) {
        if (!areAllParamsPresent(new String[]{ID_PARAM}, bankInfo))
            throw new BusinessLogicException("Bank ID not specified");
        if (!isAnyParamPresent(new String[]{NAME_PARAM, IBAN_PREFIX_PARAM, INDIVIDUALS_FEE_PARAM,
                LEGAL_ENTITIES_FEE_PARAM}, bankInfo)) throw new BusinessLogicException("New data is not specified");
        long id = Long.parseLong(bankInfo.get(ID_PARAM));
        Bank bankToUpdate = bankRepository.get(id);
        if (bankToUpdate == null) throw new BusinessLogicException("There is no bank with this ID");
        if (bankInfo.containsKey(NAME_PARAM)) {
            String name = bankInfo.get(NAME_PARAM);
            checkIfBankExistsByName(name, "update");
            bankToUpdate.setName(name);
        }
        if (bankInfo.containsKey(IBAN_PREFIX_PARAM)) {
            String ibanPrefix = bankInfo.get(IBAN_PREFIX_PARAM);
            if (ibanPrefix.length() != 4) throw new BusinessLogicException("The IBAN prefix must contain 4 characters");
            checkIfBankExistsByIbanPrefix(ibanPrefix, "update");
            bankToUpdate.setIbanPrefix(ibanPrefix);
        }
        if (bankInfo.containsKey(INDIVIDUALS_FEE_PARAM)) {
            double individualsFee = Double.parseDouble(bankInfo.get(INDIVIDUALS_FEE_PARAM));
            if (individualsFee >= 1) throw new BusinessLogicException("The fee must be less than 1");
            bankToUpdate.setIndividualsFee(individualsFee);
        }
        if (bankInfo.containsKey(LEGAL_ENTITIES_FEE_PARAM)) {
            double legalEntitiesFee = Double.parseDouble(bankInfo.get(LEGAL_ENTITIES_FEE_PARAM));
            if (legalEntitiesFee >= 1) throw new BusinessLogicException("The fee must be less than 1");
            bankToUpdate.setLegalEntitiesFee(legalEntitiesFee);
        }
        bankRepository.update(id, bankToUpdate);
        System.out.println(String.format("Bank with id %d has been successfully updated", id));
        log.info(bankToUpdate + " has been successfully updated");
    }

    @Override
    public void deleteBank(Map<String, String> bankInfo) {
        if (!areAllParamsPresent(new String[]{ID_PARAM}, bankInfo))
            throw new BusinessLogicException("Bank ID not specified");
        long id = Long.parseLong(bankInfo.get(ID_PARAM));
        Bank bankToDelete = bankRepository.get(id);
        if (bankToDelete == null) throw new BusinessLogicException("There is no bank with this ID");
        List<User> bankUsers = userRepository.getAllByTheBank(id);
        bankUsers.stream().filter(u -> userRepository.getBanksCount(u.getId()) == 1).forEach(u -> userService.deleteUser(u.getId()));
        bankUsers.forEach(u -> accountService.deleteAllUserAccountsOfSpecificBank(u.getId(), id));
        bankRepository.delete(id);
        System.out.println(String.format("Bank with id %d has been successfully deleted", id));
        log.info(bankToDelete + " has been successfully deleted from the system");
    }

    @Override
    public void addExistingUser(Map<String, String> info) {
        if (!areAllParamsPresent(new String[]{ID_PARAM, USER_ID_PARAM}, info))
            throw new BusinessLogicException("Bank or user ID not specified");
        long bankId = Long.parseLong(info.get(ID_PARAM));
        if (!bankRepository.isExists(bankId)) throw new BusinessLogicException("There is no bank with this ID");
        long userId = Long.parseLong(info.get(USER_ID_PARAM));
        if (!userRepository.isExists(userId)) throw new BusinessLogicException("There is no user with this ID");
        userRepository.createBankUser(bankId, userId);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, userId));
        String logInfo = String.format("Now the user(id = %d) is a customer of the bank(id = %d)", userId, bankId);
        System.out.println(logInfo);
        log.info(logInfo);
    }

    private void checkIfBankExistsByName(String name, String operation) {
        if (bankRepository.getByName(name) != null) {
            log.info(String.format("Failed to %s a bank named %s because it already exists", operation, name));
            throw new BusinessLogicException("A bank with that name already exists");
        }
    }

    private void checkIfBankExistsByIbanPrefix(String ibanPrefix, String operation) {
        if (bankRepository.getByIbanPrefix(ibanPrefix) != null) {
            log.info(String.format("Failed to %s a bank with iban prefix %s because it already exists", operation, ibanPrefix));
            throw new BusinessLogicException("A bank with that iban prefix already exists");
        }
    }

    private BankFullInfoDTO mapFromBankToBankFullInfoDTO(long id) {
        Bank bank = bankRepository.get(id);
        BankFullInfoDTO bankFullInfoDTO = new BankFullInfoDTO();
        bankFullInfoDTO.setName(bank.getName());
        bankFullInfoDTO.setIbanPrefix(bank.getIbanPrefix());
        bankFullInfoDTO.setIndividualsFee(bank.getIndividualsFee());
        bankFullInfoDTO.setLegalEntitiesFee(bank.getLegalEntitiesFee());
        bankFullInfoDTO.setUsersCount(bankRepository.getUsersCount(id));
        return bankFullInfoDTO;
    }
}
