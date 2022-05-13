package edu.bank.service;

import edu.bank.dao.*;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.model.dto.*;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Individual;
import edu.bank.model.entity.LegalEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private final IndividualRepository individualRepository;
    private final LegalEntityRepository legalEntityRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    public IndividualFullInfo createIndividual(IndividualToCreate individualToCreate) {
        String phone = individualToCreate.getIndividual().getPhone();
        checkUserExistsByPhone(phone, "create");
        long bankId = individualToCreate.getBankId();
        if (bankRepository.get(bankId) == null) throw new BusinessLogicException("There is no bank with this ID");
        Individual individual = individualToCreate.getIndividual();
        Individual createdIndividual = userRepository.create(bankId, individual);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, createdIndividual.getId()));
        log.info("Individual %s has been successfully created" + createdIndividual);
        return mapFromIndividualToIndividualFullInfoDTO(createdIndividual);
    }

    public LegalEntityFullInfo createLegalEntity(LegalEntityToCreate legalEntityToCreate) {
        String phone = legalEntityToCreate.getLegalEntity().getPhone();
        checkUserExistsByPhone(phone, "create");
        long bankId = legalEntityToCreate.getBankId();
        if (bankRepository.get(bankId) == null) throw new BusinessLogicException("There is no bank with this ID");
        LegalEntity legalEntity = legalEntityToCreate.getLegalEntity();
        LegalEntity createdLegalEntity = userRepository.create(bankId, legalEntity);
        System.out.println(createdLegalEntity);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, createdLegalEntity.getId()));
        log.info("Legal entity %s has been successfully created" + createdLegalEntity);
        return mapFromLegalEntityToLegalEntityFullInfoDTO(createdLegalEntity);
    }

    public List<IndividualFullInfo> getAllIndividuals() {
        List<Individual> individuals = individualRepository.getAll();
        if (individuals == null || individuals.isEmpty()) throw new BusinessLogicException("Not found");
        return individuals.stream().map(this::mapFromIndividualToIndividualFullInfoDTO).toList();
    }

    public List<LegalEntityFullInfo> getAllLegalEntities() {
        List<LegalEntity> legalEntities = legalEntityRepository.getAll();
        if (legalEntities == null || legalEntities.isEmpty()) throw new BusinessLogicException("Not found");
        return legalEntities.stream().map(this::mapFromLegalEntityToLegalEntityFullInfoDTO).toList();
    }

    public IndividualFullInfo getIndividual(Long id) {
        if (userRepository.isExists(id) || userRepository.isIndividual(id)) {
            Individual individual = individualRepository.get(id);
            return mapFromIndividualToIndividualFullInfoDTO(individual);
        } else throw new BusinessLogicException("There is no individual with this ID");
    }

    public LegalEntityFullInfo getLegalEntity(Long id) {
        if (userRepository.isExists(id) || !userRepository.isIndividual(id)) {
            LegalEntity legalEntity = legalEntityRepository.get(id);
            return mapFromLegalEntityToLegalEntityFullInfoDTO(legalEntity);
        } else throw new BusinessLogicException("There is no legal entity with this ID");
    }

    public String updateIndividual(IndividualToUpdate individualToUpdateDTO) {
        long id = individualToUpdateDTO.getId();
        if (!userRepository.isExists(id) || !userRepository.isIndividual(id))
            throw new BusinessLogicException("There is no individual with this ID");
        Individual updatedIndividual = individualToUpdateDTO.getIndividual();
        Individual individualToUpdate = individualRepository.get(id);
        String firstName = updatedIndividual.getName();
        if (firstName != null) {
            individualToUpdate.setName(firstName);
        }
        String lastName = updatedIndividual.getLastName();
        if (lastName != null) {
            individualToUpdate.setLastName(lastName);
        }
        String patronymic = updatedIndividual.getPatronymic();
        if (patronymic != null) {
            individualToUpdate.setPatronymic(patronymic);
        }
        String phone = updatedIndividual.getPhone();
        if (phone != null) {
            checkUserExistsByPhone(phone, "update");
            individualToUpdate.setPhone(phone);
        }
        individualRepository.update(id, individualToUpdate);
        log.info(individualToUpdate + " has been successfully updated");
        return String.format("Individual with id %d is successfully updated: %s", id, individualToUpdate);
    }

    public String updateLegalEntity(LegalEntityToUpdate legalEntityToUpdateDTO) {
        long id = legalEntityToUpdateDTO.getId();
        if (!userRepository.isExists(id) || userRepository.isIndividual(id))
            throw new BusinessLogicException("There is no individual with this ID");
        LegalEntity updatedLegalEntity = legalEntityToUpdateDTO.getLegalEntity();
        LegalEntity legalEntityToUpdate = legalEntityRepository.get(id);
        String name = updatedLegalEntity.getName();
        if (name != null) {
            legalEntityToUpdate.setName(name);
        }
        String phone = updatedLegalEntity.getPhone();
        if (phone != null) {
            checkUserExistsByPhone(phone, "update");
            legalEntityToUpdate.setPhone(phone);
        }
        legalEntityRepository.update(id, legalEntityToUpdate);
        return String.format("Legal entity with id %d is successfully updated: %s", id, legalEntityToUpdate);
    }

    public String deleteUser(Long id) {
        if (!userRepository.isExists(id)) throw new BusinessLogicException("There is no user with this ID");
        List<Account> accounts = accountRepository.getAllByUserId(id);
        accounts.forEach(a -> transactionRepository.deleteByRecipientAccountIban(a.getIban()));
        accounts.forEach(a -> transactionRepository.deleteBySenderAccountIban(a.getIban()));
        accounts.forEach(a -> accountRepository.delete(a.getIban()));
        userRepository.delete(id);
        return String.format("User with id %d was successfully deleted", id);
    }

    private void checkUserExistsByPhone(String phone, String operation) {
        if (individualRepository.getByPhone(phone) != null || legalEntityRepository.getByPhone(phone) != null) {
            log.info(String.format("Failed to %s a user with phone %s because she/he already exists", operation, phone));
            throw new BusinessLogicException("A user with that phone already exists");
        }
    }

    private IndividualFullInfo mapFromIndividualToIndividualFullInfoDTO(Individual individual) {
        IndividualFullInfo individualFullInfo = modelMapper.map(individual, IndividualFullInfo.class);
        individualFullInfo.setAccounts(accountService.getAllByUser(individual.getId()));
        return individualFullInfo;
    }

    private LegalEntityFullInfo mapFromLegalEntityToLegalEntityFullInfoDTO(LegalEntity legalEntity) {
        LegalEntityFullInfo legalEntityFullInfo = modelMapper.map(legalEntity, LegalEntityFullInfo.class);
        legalEntityFullInfo.setAccounts(accountService.getAllByUser(legalEntity.getId()));
        return legalEntityFullInfo;
    }
}



