package edu.bank.service.impl;

import edu.bank.command.CommandParamInspector;
import edu.bank.command.model.CommandParam;
import edu.bank.console.ConsoleCommandResultViewer;
import edu.bank.dao.AccountRepository;
import edu.bank.dao.BankRepository;
import edu.bank.dao.UserRepository;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.model.dto.BankFullInfoDTO;
import edu.bank.model.entity.Bank;
import edu.bank.model.entity.User;
import edu.bank.service.AccountService;
import edu.bank.service.BankService;
import edu.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final CommandParamInspector commandParamInspector;

    private final ConsoleCommandResultViewer commandResultViewer;
    private final BankRepository bankRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AccountService accountService;
    private static final Logger log = Logger.getLogger(BankServiceImpl.class);
    private static final String BANK_ID_PARAM = "bId";
    private static final String USER_ID_PARAM = "uId";
    private static final String NAME_PARAM = "name";
    private static final String IBAN_PREFIX_PARAM = "ibanP";
    private static final String INDIVIDUALS_FEE_PARAM = "indFee";
    private static final String LEGAL_ENTITIES_FEE_PARAM = "legFee";

    @Override
    public void createBank(Set<CommandParam> bankInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{NAME_PARAM, IBAN_PREFIX_PARAM, INDIVIDUALS_FEE_PARAM, LEGAL_ENTITIES_FEE_PARAM},
                bankInfo)) throw new BusinessLogicException("There is not enough information to create a bank");
        String name = commandParamInspector.getParamValueByNameIfPresent(NAME_PARAM, bankInfo);
        String ibanPrefix = commandParamInspector.getParamValueByNameIfPresent(IBAN_PREFIX_PARAM, bankInfo);
        if (ibanPrefix.length() != 4) throw new BusinessLogicException("The IBAN prefix must contain 4 characters");
        checkIfBankExistsByName(name, "create");
        checkIfBankExistsByIbanPrefix(ibanPrefix, "create");
        double individualsFee = Double.parseDouble(commandParamInspector.getParamValueByNameIfPresent(INDIVIDUALS_FEE_PARAM, bankInfo));
        double legalEntitiesFee = Double.parseDouble(commandParamInspector.getParamValueByNameIfPresent(LEGAL_ENTITIES_FEE_PARAM, bankInfo));
        if (individualsFee >= 1 || legalEntitiesFee >= 1)
            throw new BusinessLogicException("The fee must be less than 1");
        Bank newBank = new Bank();
        newBank.setName(name);
        newBank.setIbanPrefix(ibanPrefix);
        newBank.setIndividualsFee(individualsFee);
        newBank.setLegalEntitiesFee(legalEntitiesFee);
        bankRepository.create(newBank);
        commandResultViewer.showSuccessMessage(String.format("Bank named %s has been successfully created", name));
        log.info(newBank + " has been successfully created");
    }

    @Override
    public void getAllBanks(Set<CommandParam> bankInfo) {
        List<Bank> banks = bankRepository.getAll();
        if (banks == null || banks.isEmpty()) throw new BusinessLogicException("Not found");
        System.out.println("All banks: ");
        banks.stream().map(b -> mapFromBankToBankFullInfoDTO(b.getId())).forEach(System.out::println);
    }

    @Override
    public void getBank(Set<CommandParam> bankInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{BANK_ID_PARAM}, bankInfo))
            throw new BusinessLogicException("Bank ID not specified");
        long id = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(BANK_ID_PARAM, bankInfo));
        if (!bankRepository.isExists(id)) throw new BusinessLogicException("There is no bank with this ID");
        else commandResultViewer.showSuccessMessage(mapFromBankToBankFullInfoDTO(id).toString());
    }

    @Override
    public void updateBank(Set<CommandParam> bankInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{BANK_ID_PARAM}, bankInfo))
            throw new BusinessLogicException("Bank ID not specified");
        if (!commandParamInspector.isAnyCommandParamPresent(new String[]{NAME_PARAM, IBAN_PREFIX_PARAM, INDIVIDUALS_FEE_PARAM,
                LEGAL_ENTITIES_FEE_PARAM}, bankInfo)) throw new BusinessLogicException("New data is not specified");
        long id = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(BANK_ID_PARAM, bankInfo));
        Bank bankToUpdate = bankRepository.get(id);
        if (bankToUpdate == null) throw new BusinessLogicException("There is no bank with this ID");
        if (commandParamInspector.areCommandParamsContainsParam(NAME_PARAM, bankInfo)) {
            String name = commandParamInspector.getParamValueByNameIfPresent(NAME_PARAM, bankInfo);
            checkIfBankExistsByName(name, "update");
            bankToUpdate.setName(name);
        }
        if (commandParamInspector.areCommandParamsContainsParam(IBAN_PREFIX_PARAM, bankInfo)) {
            String ibanPrefix = commandParamInspector.getParamValueByNameIfPresent(IBAN_PREFIX_PARAM, bankInfo);
            if (ibanPrefix.length() != 4) throw new BusinessLogicException("The IBAN prefix must contain 4 characters");
            checkIfBankExistsByIbanPrefix(ibanPrefix, "update");
            bankToUpdate.setIbanPrefix(ibanPrefix);
        }
        if (commandParamInspector.areCommandParamsContainsParam(INDIVIDUALS_FEE_PARAM, bankInfo)) {
            double individualsFee = Double.parseDouble(commandParamInspector.getParamValueByNameIfPresent(INDIVIDUALS_FEE_PARAM, bankInfo));
            if (individualsFee >= 1) throw new BusinessLogicException("The fee must be less than 1");
            bankToUpdate.setIndividualsFee(individualsFee);
        }
        if (commandParamInspector.areCommandParamsContainsParam(LEGAL_ENTITIES_FEE_PARAM, bankInfo)) {
            double legalEntitiesFee = Double.parseDouble(commandParamInspector.getParamValueByNameIfPresent(LEGAL_ENTITIES_FEE_PARAM, bankInfo));
            if (legalEntitiesFee >= 1) throw new BusinessLogicException("The fee must be less than 1");
            bankToUpdate.setLegalEntitiesFee(legalEntitiesFee);
        }
        bankRepository.update(id, bankToUpdate);
        commandResultViewer.showSuccessMessage(String.format("Bank with id %d has been successfully updated", id));
        log.info(bankToUpdate + " has been successfully updated");
    }

    @Override
    public void deleteBank(Set<CommandParam> bankInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{BANK_ID_PARAM}, bankInfo))
            throw new BusinessLogicException("Bank ID not specified");
        long id = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(BANK_ID_PARAM, bankInfo));
        Bank bankToDelete = bankRepository.get(id);
        if (bankToDelete == null) throw new BusinessLogicException("There is no bank with this ID");
        List<User> bankUsers = userRepository.getAllByTheBank(id);
        bankUsers.stream().filter(u -> userRepository.getBanksCount(u.getId()) == 1).forEach(u -> userService.deleteUser(u.getId()));
        bankUsers.forEach(u -> accountService.deleteAllUserAccountsOfSpecificBank(u.getId(), id));
        bankRepository.delete(id);
        commandResultViewer.showSuccessMessage(String.format("Bank with id %d has been successfully deleted", id));
        log.info(bankToDelete + " has been successfully deleted from the system");
    }

    @Override
    public void addExistingUser(Set<CommandParam> info) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{BANK_ID_PARAM, USER_ID_PARAM}, info))
            throw new BusinessLogicException("Bank or user ID not specified");
        long bankId = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(BANK_ID_PARAM, info));
        if (!bankRepository.isExists(bankId)) throw new BusinessLogicException("There is no bank with this ID");
        long userId = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(USER_ID_PARAM, info));
        if (!userRepository.isExists(userId)) throw new BusinessLogicException("There is no user with this ID");
        userRepository.createBankUser(bankId, userId);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, userId));
        String logInfo = String.format("Now the user(id = %d) is a customer of the bank(id = %d)", userId, bankId);
        commandResultViewer.showSuccessMessage(logInfo);
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
