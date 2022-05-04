package edu.bank.service.impl;

import edu.bank.command.CommandParamInspector;
import edu.bank.command.model.CommandParam;
import edu.bank.console.ConsoleCommandResultViewer;
import edu.bank.dao.*;
import edu.bank.exeption.BusinessLogicException;
import edu.bank.model.dto.IndividualFullInfoDTO;
import edu.bank.model.dto.LegalEntityFullInfoDTO;
import edu.bank.model.entity.Account;
import edu.bank.model.entity.Individual;
import edu.bank.model.entity.LegalEntity;
import edu.bank.model.entity.User;
import edu.bank.service.AccountService;
import edu.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final CommandParamInspector commandParamInspector;

    private final ConsoleCommandResultViewer commandResultViewer;
    private final UserRepository userRepository;
    private final BankRepository bankRepository;
    private final IndividualRepository individualRepository;
    private final LegalEntityRepository legalEntityRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private static final Logger log = Logger.getLogger(UserServiceImpl.class);
    private static final String BANK_ID_PARAM = "bId";
    private static final String USER_ID_PARAM = "uId";
    private static final String LEGAL_ENTITY_NAME_PARAM = "leName";
    private static final String INDIVIDUAL_FIRST_NAME_PARAM = "fName";
    private static final String INDIVIDUAL_LAST_NAME_PARAM = "lName";
    private static final String INDIVIDUAL_PATRONYMIC_PARAM = "patr";
    private static final String PHONE_PARAM = "phone";

    @Override
    public void createIndividual(Set<CommandParam> userInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{BANK_ID_PARAM, INDIVIDUAL_FIRST_NAME_PARAM, INDIVIDUAL_LAST_NAME_PARAM,
                INDIVIDUAL_PATRONYMIC_PARAM, PHONE_PARAM}, userInfo))
            throw new BusinessLogicException("There is not enough information to create an individual");
        String phone = commandParamInspector.getParamValueByNameIfPresent(PHONE_PARAM, userInfo);
        checkUserExistsByPhone(phone, "create");
        long bankId = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(BANK_ID_PARAM, userInfo));
        if (bankRepository.get(bankId) == null) throw new BusinessLogicException("There is no bank with this ID");
        String firstName = commandParamInspector.getParamValueByNameIfPresent(INDIVIDUAL_FIRST_NAME_PARAM, userInfo);
        String lastName = commandParamInspector.getParamValueByNameIfPresent(INDIVIDUAL_LAST_NAME_PARAM, userInfo);
        String patronymic = commandParamInspector.getParamValueByNameIfPresent(INDIVIDUAL_PATRONYMIC_PARAM, userInfo);
        Individual individual = new Individual();
        individual.setName(firstName);
        individual.setLastName(lastName);
        individual.setPatronymic(patronymic);
        individual.setPhone(phone);
        User createdUser = userRepository.create(bankId, individual);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, createdUser.getId()));
        String info = String.format("Individual %s %s %s has been successfully created", lastName, firstName, patronymic);
        commandResultViewer.showSuccessMessage(info);
        log.info(String.format(info));
    }

    @Override
    public void createLegalEntity(Set<CommandParam> userInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{BANK_ID_PARAM, LEGAL_ENTITY_NAME_PARAM, PHONE_PARAM}, userInfo))
            throw new BusinessLogicException("There is not enough information to create a legal entity");
        String phone = commandParamInspector.getParamValueByNameIfPresent(PHONE_PARAM, userInfo);
        checkUserExistsByPhone(phone, "create");
        long bankId = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(BANK_ID_PARAM, userInfo));
        if (bankRepository.get(bankId) == null) throw new BusinessLogicException("There is no bank with this ID");
        String name = commandParamInspector.getParamValueByNameIfPresent(LEGAL_ENTITY_NAME_PARAM, userInfo);
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setName(name);
        legalEntity.setPhone(phone);
        User createdUser = userRepository.create(bankId, legalEntity);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, createdUser.getId()));
        String info = String.format("Legal entity %s has been successfully created", name);
        commandResultViewer.showSuccessMessage(info);
        log.info(String.format(info));
    }

    @Override
    public void getAllIndividuals(Set<CommandParam> userInfo) {
        List<Individual> individuals = individualRepository.getAll();
        if (individuals == null || individuals.isEmpty()) throw new BusinessLogicException("Not found");
        commandResultViewer.showSuccessMessage("All individuals: ");
        individuals.stream().map(i -> mapFromIndividualToIndividualFullInfoDTO(i.getId())).forEach(System.out::println);
    }

    @Override
    public void getAllLegalEntities(Set<CommandParam> userInfo) {
        List<LegalEntity> legalEntities = legalEntityRepository.getAll();
        if (legalEntities == null || legalEntities.isEmpty()) throw new BusinessLogicException("Not found");
        commandResultViewer.showSuccessMessage("All legal entities: ");
        legalEntities.stream().map(l -> mapFromLegalEntityToLegalEntityFullInfoDTO(l.getId())).forEach(System.out::println);
    }

    @Override
    public void getIndividual(Set<CommandParam> userInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{USER_ID_PARAM}, userInfo))
            throw new BusinessLogicException("Individual ID not specified");
        long id = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(USER_ID_PARAM, userInfo));
        if (!userRepository.isExists(id) || !userRepository.isIndividual(id))
            throw new BusinessLogicException("There is no individual with this ID");
        else commandResultViewer.showSuccessMessage(mapFromIndividualToIndividualFullInfoDTO(id).toString());
    }

    @Override
    public void getLegalEntity(Set<CommandParam> userInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{USER_ID_PARAM}, userInfo))
            throw new BusinessLogicException("Legal entity ID not specified");
        long id = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(USER_ID_PARAM, userInfo));
        if (!userRepository.isExists(id) || userRepository.isIndividual(id))
            throw new BusinessLogicException("There is no legal entity with this ID");
        else commandResultViewer.showSuccessMessage(mapFromLegalEntityToLegalEntityFullInfoDTO(id).toString());
    }

    @Override
    public void updateIndividual(Set<CommandParam> userInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{USER_ID_PARAM}, userInfo))
            throw new BusinessLogicException("Individual ID not specified");
        if (!commandParamInspector.isAnyCommandParamPresent(new String[]{INDIVIDUAL_FIRST_NAME_PARAM, INDIVIDUAL_LAST_NAME_PARAM, INDIVIDUAL_PATRONYMIC_PARAM,
                PHONE_PARAM}, userInfo)) throw new BusinessLogicException("New data is not specified");
        long id = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(USER_ID_PARAM, userInfo));
        Individual individualToUpdate = individualRepository.get(id);
        if (individualToUpdate == null) throw new BusinessLogicException("There is no individual with this ID");
        if (commandParamInspector.areCommandParamsContainsParam(INDIVIDUAL_FIRST_NAME_PARAM, userInfo)) {
            String firstName = commandParamInspector.getParamValueByNameIfPresent(INDIVIDUAL_FIRST_NAME_PARAM, userInfo);
            individualToUpdate.setName(firstName);
        }
        if (commandParamInspector.areCommandParamsContainsParam(INDIVIDUAL_LAST_NAME_PARAM, userInfo)) {
            String lastName = commandParamInspector.getParamValueByNameIfPresent(INDIVIDUAL_LAST_NAME_PARAM, userInfo);
            individualToUpdate.setLastName(lastName);
        }
        if (commandParamInspector.areCommandParamsContainsParam(INDIVIDUAL_PATRONYMIC_PARAM, userInfo)) {
            String patronymic = commandParamInspector.getParamValueByNameIfPresent(INDIVIDUAL_PATRONYMIC_PARAM, userInfo);
            individualToUpdate.setPatronymic(patronymic);
        }
        if (commandParamInspector.areCommandParamsContainsParam(PHONE_PARAM, userInfo)) {
            String phone = commandParamInspector.getParamValueByNameIfPresent(PHONE_PARAM, userInfo);
            checkUserExistsByPhone(phone, "update");
            individualToUpdate.setPhone(phone);
        }
        individualRepository.update(id, individualToUpdate);
        commandResultViewer.showSuccessMessage(String.format("Individual with id %d has been successfully updated", id));
        log.info(individualToUpdate + " has been successfully updated");
    }

    @Override
    public void updateLegalEntity(Set<CommandParam> userInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{USER_ID_PARAM}, userInfo))
            throw new BusinessLogicException("Legal entity ID not specified");
        if (!commandParamInspector.isAnyCommandParamPresent(new String[]{LEGAL_ENTITY_NAME_PARAM, PHONE_PARAM}, userInfo))
            throw new BusinessLogicException("New data is not specified");
        long id = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(USER_ID_PARAM, userInfo));
        LegalEntity legalEntityToUpdate = legalEntityRepository.get(id);
        if (legalEntityToUpdate == null) throw new BusinessLogicException("There is no legal entity with this ID");
        if (commandParamInspector.areCommandParamsContainsParam(LEGAL_ENTITY_NAME_PARAM, userInfo)) {
            String name = commandParamInspector.getParamValueByNameIfPresent(LEGAL_ENTITY_NAME_PARAM, userInfo);
            legalEntityToUpdate.setName(name);
        }
        if (commandParamInspector.areCommandParamsContainsParam(PHONE_PARAM, userInfo)) {
            String phone = commandParamInspector.getParamValueByNameIfPresent(PHONE_PARAM, userInfo);
            checkUserExistsByPhone(phone, "update");
            legalEntityToUpdate.setPhone(phone);
        }
        legalEntityRepository.update(id, legalEntityToUpdate);
        commandResultViewer.showSuccessMessage(String.format("Legal entity with id %d has been successfully updated", id));
        log.info(legalEntityToUpdate + " has been successfully updated");
    }

    @Override
    public void deleteUser(Set<CommandParam> userInfo) {
        if (!commandParamInspector.areAllCommandParamsPresent(new String[]{USER_ID_PARAM}, userInfo))
            throw new BusinessLogicException("User ID not specified");
        long id = Long.parseLong(commandParamInspector.getParamValueByNameIfPresent(USER_ID_PARAM, userInfo));
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
