package edu.bank.service.impl;

import edu.bank.dao.AccountRepository;
import edu.bank.dao.IndividualRepository;
import edu.bank.dao.LegalEntityRepository;
import edu.bank.dao.UserRepository;
import edu.bank.dao.impl.AccountRepositoryImpl;
import edu.bank.dao.impl.IndividualRepositoryImpl;
import edu.bank.dao.impl.LegalEntityRepositoryImpl;
import edu.bank.dao.impl.UserRepositoryImpl;
import edu.bank.model.entity.Individual;
import edu.bank.model.entity.LegalEntity;
import edu.bank.model.entity.User;
import edu.bank.service.AccountService;
import edu.bank.service.UserService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

import static edu.bank.model.enm.CommandParam.*;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository = new UserRepositoryImpl();
    private final IndividualRepository individualRepository = new IndividualRepositoryImpl();
    private final LegalEntityRepository legalEntityRepository = new LegalEntityRepositoryImpl();
    private final AccountRepository accountRepository = new AccountRepositoryImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private static final Logger log = Logger.getLogger(UserServiceImpl.class);
    private static final String BANK_ID_PARAM = BANK_ID.getParamName();
    private static final String LEGAL_ENTITY_NAME_PARAM = LEGAL_ENTITY_USER_NAME.getParamName();
    private static final String INDIVIDUAL_FIRST_NAME_PARAM = INDIVIDUAL_USER_FIRST_NAME.getParamName();
    private static final String INDIVIDUAL_LAST_NAME_PARAM = INDIVIDUAL_USER_LAST_NAME.getParamName();
    private static final String INDIVIDUAL_PATRONYMIC_PARAM = INDIVIDUAL_USER_PATRONYMIC.getParamName();
    private static final String PHONE_PARAM = USER_PHONE.getParamName();

    @Override
    public void createIndividual(Map<String, String> userInfo) throws IOException {
        if (!userInfo.containsKey(BANK_ID_PARAM) || !userInfo.containsKey(INDIVIDUAL_FIRST_NAME_PARAM)
                || !userInfo.containsKey(INDIVIDUAL_LAST_NAME_PARAM)
                || !userInfo.containsKey(INDIVIDUAL_PATRONYMIC_PARAM)) throw new IOException();
        if (isUserExistsByPhone(userInfo, "create")) return;
        long bankId = Long.parseLong(userInfo.get(BANK_ID_PARAM));
        String firstName = userInfo.get(INDIVIDUAL_FIRST_NAME_PARAM);
        String lastName = userInfo.get(INDIVIDUAL_LAST_NAME_PARAM);
        String patronymic = userInfo.get(INDIVIDUAL_PATRONYMIC_PARAM);
        String phone = userInfo.get(PHONE_PARAM);
        Individual individual = new Individual();
        individual.setName(firstName);
        individual.setLastName(lastName);
        individual.setPatronymic(patronymic);
        individual.setPhone(phone);
        User createdUser = userRepository.create(bankId, individual);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, createdUser.getId()));
    }

    @Override
    public void createLegalEntity(Map<String, String> userInfo) throws IOException {
        if (!userInfo.containsKey(BANK_ID_PARAM) || !userInfo.containsKey(LEGAL_ENTITY_NAME_PARAM))
            throw new IOException();
        if (isUserExistsByPhone(userInfo, "create")) return;
        long bankId = Long.parseLong(userInfo.get(BANK_ID_PARAM));
        String name = userInfo.get(LEGAL_ENTITY_NAME_PARAM);
        String phone = userInfo.get(PHONE_PARAM);
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setName(name);
        legalEntity.setPhone(phone);
        User createdUser = userRepository.create(bankId, legalEntity);
        accountRepository.create(accountService.getDefaultAccountForNewUser(bankId, createdUser.getId()));
    }

    private boolean isUserExistsByPhone(Map<String, String> userInfo, String operation) throws IOException {
        if (!userInfo.containsKey(PHONE_PARAM)) throw new IOException();
        String phone = userInfo.get(PHONE_PARAM);
        if (individualRepository.getByPhone(phone) != null || legalEntityRepository.getByPhone(phone) != null) {
            System.out.println("A user with that phone already exists");
            log.info(String.format("Failed to %s a user with phone %s because it already exists", operation, phone));
            return true;
        }
        return false;
    }
}
