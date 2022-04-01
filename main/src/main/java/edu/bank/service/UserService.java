package edu.bank.service;

import java.io.IOException;
import java.util.Map;

public interface UserService {

    void createIndividual(Map<String, String> userInfo) throws IOException;

    void createLegalEntity(Map<String, String> userInfo) throws IOException;
}
