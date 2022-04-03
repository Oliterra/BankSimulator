package edu.bank.service.impl;

import edu.bank.dao.*;
import edu.bank.dao.impl.*;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.model.dto.IndividualFullInfoDTO;
import edu.bank.model.dto.LegalEntityFullInfoDTO;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Individual;
import edu.bank.model.entity.LegalEntity;
import edu.bank.model.entity.User;
import edu.bank.service.AccountService;
import edu.bank.service.CommandManager;
import edu.bank.service.UserService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

import static edu.bank.model.enm.CommandParam.*;

public class UserServiceImpl implements UserService, CommandManager {

    private final UserRepository userRepository = new UserRepositoryImpl();
    private final BankRepository bankRepository = new BankRepositoryImpl();
    private final IndividualRepository individualRepository = new IndividualRepositoryImpl();
    private final LegalEntityRepository legalEntityRepository = new LegalEntityRepositoryImpl();
    private final AccountRepository accountRepository = new AccountRepositoryImpl();
    private final TransactionRepository transactionRepository = new TransactionRepositoryImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private static final Logger log = Logger.getLogger(UserServiceImpl.class);
    private static final String BANK_ID_PARAM = BANK_ID.getParamName();
    private static final String USER_ID_PARAM = USER_ID.getParamName();
    private static final String LEGAL_ENTITY_NAME_PARAM = LEGAL_ENTITY_USER_NAME.getParamName();
    private static final String INDIVIDUAL_FIRST_NAME_PARAM = INDIVIDUAL_USER_FIRST_NAME.getParamName();
    private static final String INDIVIDUAL_LAST_NAME_PARAM = INDIVIDUAL_USER_LAST_NAME.getParamName();
    private static final String INDIVIDUAL_PATRONYMIC_PARAM = INDIVIDUAL_USER_PATRONYMIC.getParamName();
    private static final String PHONE_PARAM = USER_PHONE.getParamName();

    @Override
    public void createIndividual(Map<String, String> userInfo) {
        if (!areAllParamsPresent(new String[]{BANK_ID_PARAM, INDIVIDUAL_FIRST_NAME_PARAM, INDIVIDUAL_LAST_NAME_PARAM,
                INDIVIDUAL_PATRONYMIC_PARAM, PHONE_PARAM}, userInfo))
            throw new BusinessLogicException("There is not enough information to create an individual");
        String phone = userInfo.get(PHONE_PARAM);
        checkUserExistsByPhone(phone, "create");
        long bankId = Long.parseLong(userInfo.get(BANK_ID_PARAM));
        if (bankRepository.get(bankId) == null) throw new BusinessLogicException("There is no bank with this ID");
        String firstName = userInfo.get(INDIVIDUAL_FIRST_NAME_PARAM);
        String lastName = userInfo.get(INDIVIDUAL_LAST_NAME_PARAM);
        String patronymic = userInfo.get(INDIVIDUAL_PATRONYMIC_PARAM);
        Individual individual = new Individual();
        individual.setName(firstName);
        individual.setLastName(lastName);
        individual.setPatronymic(patronymic);
        individual.setPhone(phone);
        User createdUser = userRepository.create(bankId, individual);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, createdUser.getId()));
        String info = String.format("Individual %s %s %s has been successfully created", lastName, firstName, patronymic);
        System.out.println(String.format(info));
        log.info(String.format(info));
    }

    @Override
    public void createLegalEntity(Map<String, String> userInfo) {
        if (!areAllParamsPresent(new String[]{BANK_ID_PARAM, LEGAL_ENTITY_NAME_PARAM, PHONE_PARAM}, userInfo))
            throw new BusinessLogicException("There is not enough information to create a legal entity");
        String phone = userInfo.get(PHONE_PARAM);
        checkUserExistsByPhone(phone, "create");
        long bankId = Long.parseLong(userInfo.get(BANK_ID_PARAM));
        if (bankRepository.get(bankId) == null) throw new BusinessLogicException("There is no bank with this ID");
        String name = userInfo.get(LEGAL_ENTITY_NAME_PARAM);
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setName(name);
        legalEntity.setPhone(phone);
        User createdUser = userRepository.create(bankId, legalEntity);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, createdUser.getId()));
        String info = String.format("Legal entity %s has been successfully created", name);
        System.out.println(String.format(info));
        log.info(String.format(info));
    }

    @Override
    public void getAllIndividuals(Map<String, String> userInfo) {
        List<Individual> individuals = individualRepository.getAll();
        if (individuals == null || individuals.isEmpty()) throw new BusinessLogicException("Not found");
        System.out.println("All individuals: ");
        individuals.stream().map(i -> mapFromIndividualToIndividualFullInfoDTO(i.getId())).forEach(System.out::println);
    }

    @Override
    public void getAllLegalEntities(Map<String, String> userInfo) {
        List<LegalEntity> legalEntities = legalEntityRepository.getAll();
        if (legalEntities == null || legalEntities.isEmpty()) throw new BusinessLogicException("Not found");
        System.out.println("All legal entities: ");
        legalEntities.stream().map(l -> mapFromLegalEntityToLegalEntityFullInfoDTO(l.getId())).forEach(System.out::println);
    }

    @Override
    public void getIndividual(Map<String, String> userInfo) {
        if (!areAllParamsPresent(new String[]{USER_ID_PARAM}, userInfo))
            throw new BusinessLogicException("Individual ID not specified");
        long id = Long.parseLong(userInfo.get(USER_ID_PARAM));
        if (!userRepository.isExists(id) || !userRepository.isIndividual(id))
            throw new BusinessLogicException("There is no individual with this ID");
        else System.out.println(mapFromIndividualToIndividualFullInfoDTO(id));
    }

    @Override
    public void getLegalEntity(Map<String, String> userInfo) {
        if (!areAllParamsPresent(new String[]{USER_ID_PARAM}, userInfo))
            throw new BusinessLogicException("Legal entity ID not specified");
        long id = Long.parseLong(userInfo.get(USER_ID_PARAM));
        if (!userRepository.isExists(id) || userRepository.isIndividual(id))
            throw new BusinessLogicException("There is no legal entity with this ID");
        else System.out.println(mapFromLegalEntityToLegalEntityFullInfoDTO(id));
    }

    @Override
    public void updateIndividual(Map<String, String> userInfo) {
        if (!areAllParamsPresent(new String[]{USER_ID_PARAM}, userInfo))
            throw new BusinessLogicException("Individual ID not specified");
        if (!isAnyParamPresent(new String[]{INDIVIDUAL_FIRST_NAME_PARAM, INDIVIDUAL_LAST_NAME_PARAM, INDIVIDUAL_PATRONYMIC_PARAM,
                PHONE_PARAM}, userInfo)) throw new BusinessLogicException("New data is not specified");
        long id = Long.parseLong(userInfo.get(USER_ID_PARAM));
        Individual individualToUpdate = individualRepository.get(id);
        if (individualToUpdate == null) throw new BusinessLogicException("There is no individual with this ID");
        if (userInfo.containsKey(INDIVIDUAL_FIRST_NAME_PARAM)) {
            String firstName = userInfo.get(INDIVIDUAL_FIRST_NAME_PARAM);
            individualToUpdate.setName(firstName);
        }
        if (userInfo.containsKey(INDIVIDUAL_LAST_NAME_PARAM)) {
            String lastName = userInfo.get(INDIVIDUAL_LAST_NAME_PARAM);
            individualToUpdate.setLastName(lastName);
        }
        if (userInfo.containsKey(INDIVIDUAL_PATRONYMIC_PARAM)) {
            String patronymic = userInfo.get(INDIVIDUAL_PATRONYMIC_PARAM);
            individualToUpdate.setPatronymic(patronymic);
        }
        if (userInfo.containsKey(PHONE_PARAM)) {
            String phone = userInfo.get(PHONE_PARAM);
            checkUserExistsByPhone(phone, "update");
            individualToUpdate.setPhone(phone);
        }
        individualRepository.update(id, individualToUpdate);
        System.out.println(String.format("Individual with id %d has been successfully updated", id));
        log.info(individualToUpdate + " has been successfully updated");
    }

    @Override
    public void updateLegalEntity(Map<String, String> userInfo) {
        if (!areAllParamsPresent(new String[]{USER_ID_PARAM}, userInfo))
            throw new BusinessLogicException("Legal entity ID not specified");
        if (!isAnyParamPresent(new String[]{LEGAL_ENTITY_NAME_PARAM, PHONE_PARAM}, userInfo))
            throw new BusinessLogicException("New data is not specified");
        long id = Long.parseLong(userInfo.get(USER_ID_PARAM));
        LegalEntity legalEntityToUpdate = legalEntityRepository.get(id);
        if (legalEntityToUpdate == null) throw new BusinessLogicException("There is no legal entity with this ID");
        if (userInfo.containsKey(LEGAL_ENTITY_NAME_PARAM)) {
            String name = userInfo.get(LEGAL_ENTITY_NAME_PARAM);
            legalEntityToUpdate.setName(name);
        }
        if (userInfo.containsKey(PHONE_PARAM)) {
            String phone = userInfo.get(PHONE_PARAM);
            checkUserExistsByPhone(phone, "update");
            legalEntityToUpdate.setPhone(phone);
        }
        legalEntityRepository.update(id, legalEntityToUpdate);
        System.out.println(String.format("Legal entity with id %d has been successfully updated", id));
        log.info(legalEntityToUpdate + " has been successfully updated");
    }

    @Override
    public void deleteUser(Map<String, String> userInfo) {
        if (!areAllParamsPresent(new String[]{USER_ID_PARAM}, userInfo))
            throw new BusinessLogicException("User ID not specified");
        long id = Long.parseLong(userInfo.get(USER_ID_PARAM));
        if (userRepository.get(id) == null) throw new BusinessLogicException("There is no user with this ID");
        else deleteUser(id);
    }

    @Override
    public void deleteUser(long id) {
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

    private IndividualFullInfoDTO mapFromIndividualToIndividualFullInfoDTO(long id) {
        Individual individual = individualRepository.get(id);
        IndividualFullInfoDTO individualFullInfoDTO = new IndividualFullInfoDTO();
        individualFullInfoDTO.setFirstName(individual.getName());
        individualFullInfoDTO.setLastName(individual.getLastName());
        individualFullInfoDTO.setPatronymic(individual.getPatronymic());
        individualFullInfoDTO.setPhone(individual.getPhone());
        individualFullInfoDTO.setAccounts(accountService.getAllByUser(id));
        return individualFullInfoDTO;
    }

    private LegalEntityFullInfoDTO mapFromLegalEntityToLegalEntityFullInfoDTO(long id) {
        LegalEntity legalEntity = legalEntityRepository.get(id);
        LegalEntityFullInfoDTO legalEntityFullInfoDTO = new LegalEntityFullInfoDTO();
        legalEntityFullInfoDTO.setName(legalEntity.getName());
        legalEntityFullInfoDTO.setPhone(legalEntity.getPhone());
        legalEntityFullInfoDTO.setAccounts(accountService.getAllByUser(id));
        return legalEntityFullInfoDTO;
    }
}
