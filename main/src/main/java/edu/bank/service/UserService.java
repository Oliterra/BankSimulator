package edu.bank.service;

import edu.bank.command.model.CommandParam;

import java.util.Set;

public interface UserService {

    void createIndividual(Set<CommandParam> userInfo);

    void createLegalEntity(Set<CommandParam>  userInfo);

    void getAllIndividuals(Set<CommandParam>  userInfo);

    void getAllLegalEntities(Set<CommandParam>  userInfo);

    void getIndividual(Set<CommandParam>  userInfo);

    void getLegalEntity(Set<CommandParam>  userInfo);

    void updateIndividual(Set<CommandParam>  userInfo);

    void updateLegalEntity(Set<CommandParam>  userInfo);

    void deleteUser(Set<CommandParam> userInfo);

    void deleteUser(long id);
}
