package edu.bank.service;

import java.util.Map;

public interface UserService {

    void createIndividual(Map<String, String> userInfo);

    void createLegalEntity(Map<String, String> userInfo);

    void getAllIndividuals(Map<String, String> userInfo);

    void getAllLegalEntities(Map<String, String> userInfo);

    void getIndividual(Map<String, String> userInfo);

    void getLegalEntity(Map<String, String> userInfo);

    void updateIndividual(Map<String, String> userInfo);

    void updateLegalEntity(Map<String, String> userInfo);

    void deleteUser(Map<String, String> userInfo);

    void deleteUser(long id);
}
