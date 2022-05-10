package edu.bank.service;

import edu.bank.dao.*;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.model.dto.*;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Individual;
import edu.bank.model.entity.LegalEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private final IndividualRepository individualRepository;
    private final LegalEntityRepository legalEntityRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    public IndividualFullInfoDTO createIndividual(IndividualToCreateDTO individualToCreateDTO) {
        String phone = individualToCreateDTO.getIndividual().getPhone();
        checkUserExistsByPhone(phone, "create");
        long bankId = individualToCreateDTO.getBankId();
        if (bankRepository.get(bankId) == null) throw new BusinessLogicException("There is no bank with this ID");
        Individual individual = individualToCreateDTO.getIndividual();
        Individual createdIndividual = userRepository.create(bankId, individual);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, createdIndividual.getId()));
        log.info("Individual %s %s %s has been successfully created" + createdIndividual);
        return mapFromIndividualToIndividualFullInfoDTO(createdIndividual);
    }

    public LegalEntityFullInfoDTO createLegalEntity(LegalEntityToCreateDTO legalEntityToCreateDTO) {
        String phone = legalEntityToCreateDTO.getLegalEntity().getPhone();
        checkUserExistsByPhone(phone, "create");
        long bankId = legalEntityToCreateDTO.getBankId();
        if (bankRepository.get(bankId) == null) throw new BusinessLogicException("There is no bank with this ID");
        LegalEntity legalEntity = legalEntityToCreateDTO.getLegalEntity();
        LegalEntity createdLegalEntity = userRepository.create(bankId, legalEntity);
        System.out.println(createdLegalEntity);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, createdLegalEntity.getId()));
        log.info("Legal entity %s has been successfully created" + createdLegalEntity);
        return mapFromLegalEntityToLegalEntityFullInfoDTO(createdLegalEntity);
    }

    public List<IndividualFullInfoDTO> getAllIndividuals() {
        List<Individual> individuals = individualRepository.getAll();
        if (individuals == null || individuals.isEmpty()) throw new BusinessLogicException("Not found");
        return individuals.stream().map(this::mapFromIndividualToIndividualFullInfoDTO).toList();
    }

    public List<LegalEntityFullInfoDTO> getAllLegalEntities() {
        List<LegalEntity> legalEntities = legalEntityRepository.getAll();
        if (legalEntities == null || legalEntities.isEmpty()) throw new BusinessLogicException("Not found");
        return legalEntities.stream().map(this::mapFromLegalEntityToLegalEntityFullInfoDTO).toList();
    }

    public IndividualFullInfoDTO getIndividual(Long id) {
        if (userRepository.isExists(id) || userRepository.isIndividual(id)) {
            Individual individual = individualRepository.get(id);
            return mapFromIndividualToIndividualFullInfoDTO(individual);
        } else throw new BusinessLogicException("There is no individual with this ID");
    }

    public LegalEntityFullInfoDTO getLegalEntity(Long id) {
        if (userRepository.isExists(id) || !userRepository.isIndividual(id)) {
            LegalEntity legalEntity = legalEntityRepository.get(id);
            return mapFromLegalEntityToLegalEntityFullInfoDTO(legalEntity);
        } else throw new BusinessLogicException("There is no legal entity with this ID");
    }

    public void updateIndividual(IndividualToUpdateDTO individualToUpdateDTO) {
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
    }

    public void updateLegalEntity(LegalEntityToUpdateDTO legalEntityToUpdateDTO) {
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
        log.info(legalEntityToUpdate + " has been successfully updated");
    }

    public void deleteUser(Long id) {
        if (!userRepository.isExists(id)) throw new BusinessLogicException("There is no user with this ID");
        List<Account> accounts = accountRepository.getAllByUserId(id);
        accounts.forEach(a -> transactionRepository.deleteByRecipientAccountIban(a.getIban()));
        accounts.forEach(a -> transactionRepository.deleteBySenderAccountIban(a.getIban()));
        accounts.forEach(a -> accountRepository.delete(a.getIban()));
        userRepository.delete(id);
    }

    private void checkUserExistsByPhone(String phone, String operation) {
        if (individualRepository.getByPhone(phone) != null || legalEntityRepository.getByPhone(phone) != null) {
            log.info(String.format("Failed to %s a user with phone %s because she/he already exists", operation, phone));
            throw new BusinessLogicException("A user with that phone already exists");
        }
    }

    private IndividualFullInfoDTO mapFromIndividualToIndividualFullInfoDTO(Individual individual) {
        IndividualFullInfoDTO individualFullInfoDTO = modelMapper.map(individual, IndividualFullInfoDTO.class);
        individualFullInfoDTO.setAccounts(accountService.getAllByUser(individual.getId()));
        return individualFullInfoDTO;
    }

    private LegalEntityFullInfoDTO mapFromLegalEntityToLegalEntityFullInfoDTO(LegalEntity legalEntity) {
        LegalEntityFullInfoDTO legalEntityFullInfoDTO = modelMapper.map(legalEntity, LegalEntityFullInfoDTO.class);
        legalEntityFullInfoDTO.setAccounts(accountService.getAllByUser(legalEntity.getId()));
        return legalEntityFullInfoDTO;
    }
}



