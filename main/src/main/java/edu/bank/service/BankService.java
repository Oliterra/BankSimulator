package edu.bank.service;

import edu.bank.dao.AccountRepository;
import edu.bank.dao.BankRepository;
import edu.bank.dao.UserRepository;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.exeption.InternalError;
import edu.bank.model.dto.AccountMainInfoDTO;
import edu.bank.model.dto.BankFullInfoDTO;
import edu.bank.model.dto.BankToUpdateDTO;
import edu.bank.model.dto.CreateBankClientDTO;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Bank;
import edu.bank.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class BankService {

    private final BankRepository bankRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    public BankFullInfoDTO createBank(Bank bank) {
        String name = bank.getName();
        String ibanPrefix = bank.getIbanPrefix();
        checkIfBankExistsByName(name, "create");
        checkIfBankExistsByIbanPrefix(ibanPrefix, "create");
        Bank createdBank = bankRepository.create(bank);
        log.info(bank + " has been successfully created");
        BankFullInfoDTO bankFullInfoDTO = modelMapper.map(createdBank, BankFullInfoDTO.class);
        return bankFullInfoDTO;
    }

    public List<BankFullInfoDTO> getAllBanks() {
        List<Bank> banks = bankRepository.getAll();
        if (banks == null || banks.isEmpty()) throw new InternalError("Not found");
        return banks.stream().map(this::mapFromBankToBankFullInfoDTO).toList();
    }

    public BankFullInfoDTO getBank(Long id) {
        if (!bankRepository.isExists(id)) throw new BusinessLogicException("There is no bank with this ID");
        Bank bank = bankRepository.get(id);
        return mapFromBankToBankFullInfoDTO(bank);
    }

    public void updateBank(BankToUpdateDTO bank) {
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
    }

    public void deleteBank(Long id) {
        if (!bankRepository.isExists(id)) throw new BusinessLogicException("There is no bank with this ID");
        Bank bankToDelete = bankRepository.get(id);
        List<User> bankUsers = userRepository.getAllByTheBank(id);
        bankUsers.stream().filter(u -> userRepository.getBanksCount(u.getId()) == 1).forEach(u -> userService.deleteUser(u.getId()));
        bankUsers.forEach(u -> accountService.deleteAllUserAccountsOfSpecificBank(u.getId(), id));
        bankRepository.delete(id);
        log.info(bankToDelete + " has been successfully deleted from the system");
    }

    public AccountMainInfoDTO addExistingUser(CreateBankClientDTO createBankClientDTO) {
        long bankId = createBankClientDTO.getBankId();
        if (!bankRepository.isExists(bankId)) throw new BusinessLogicException("There is no bank with this ID");
        long userId = createBankClientDTO.getUserId();
        if (!userRepository.isExists(userId)) throw new BusinessLogicException("There is no user with this ID");
        userRepository.createBankUser(bankId, userId);
        Account newAccount = accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, userId));
        AccountMainInfoDTO newAccountMainInfoDTO = modelMapper.map(newAccount, AccountMainInfoDTO.class);
        newAccountMainInfoDTO.setBankName(accountService.determineBankByIban(newAccount.getIban()).getName());
        log.info(String.format("Now the user(id = %d) is a customer of the bank(id = %d)", userId, bankId));
        return newAccountMainInfoDTO;
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

    private BankFullInfoDTO mapFromBankToBankFullInfoDTO(Bank bank) {
        BankFullInfoDTO bankFullInfoDTO = modelMapper.map(bank, BankFullInfoDTO.class);
        bankFullInfoDTO.setUsersCount(bankRepository.getUsersCount(bank.getId()));
        return bankFullInfoDTO;
    }
}

