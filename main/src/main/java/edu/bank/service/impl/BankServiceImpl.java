package edu.bank.service.impl;

import edu.bank.dao.BankRepository;
import edu.bank.dao.impl.BankRepositoryImpl;
import edu.bank.entity.Bank;
import edu.bank.service.BankService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

import static edu.bank.enm.CommandParam.*;

public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository = new BankRepositoryImpl();
    private static final Logger log = Logger.getLogger(BankServiceImpl.class);
    private static final String ID_PARAM = BANK_ID.getParamName();
    private static final String NAME_PARAM = BANK_NAME.getParamName();
    private static final String IBAN_PREFIX_PARAM = IBAN_PREFIX.getParamName();
    private static final String INDIVIDUALS_FEE_PARAM = INDIVIDUALS_FEE.getParamName();
    private static final String LEGAL_ENTITIES_FEE_PARAM = LEGAL_ENTITIES_FEE.getParamName();

    @Override
    public void createBank(Map<String, String> bankInfo) throws IOException, NumberFormatException {
        if (!bankInfo.containsKey(NAME_PARAM) || !bankInfo.containsKey(IBAN_PREFIX_PARAM)
                || !bankInfo.containsKey(INDIVIDUALS_FEE_PARAM)
                || !bankInfo.containsKey(LEGAL_ENTITIES_FEE_PARAM)) throw new IOException();
        String name = bankInfo.get(NAME_PARAM);
        String ibanPrefix = bankInfo.get(IBAN_PREFIX_PARAM);
        if (isBankExistsByName(name, "create") || isBankExistsByIbanPrefix(ibanPrefix, "create")) return;
        double individualsFee = Double.parseDouble(bankInfo.get(INDIVIDUALS_FEE_PARAM));
        double legalEntitiesFee = Double.parseDouble(bankInfo.get(LEGAL_ENTITIES_FEE_PARAM));
        Bank newBank = new Bank();
        newBank.setName(name);
        newBank.setIbanPrefix(ibanPrefix);
        newBank.setIndividualsFee(individualsFee);
        newBank.setLegalEntitiesFee(legalEntitiesFee);
        bankRepository.create(newBank);
    }

    @Override
    public void updateBank(Map<String, String> newBankInfo) throws IOException {
        if (!newBankInfo.containsKey(ID_PARAM)) throw new IOException();
        long id = Long.parseLong(newBankInfo.get(ID_PARAM));
        Bank bankToUpdate = bankRepository.get(id);
        if (bankToUpdate == null) throw new IOException();
        if (newBankInfo.containsKey(NAME_PARAM)) {
            String name = newBankInfo.get(NAME_PARAM);
            if (isBankExistsByName(name, "create")) return;
            bankToUpdate.setName(name);
        }
        if (newBankInfo.containsKey(IBAN_PREFIX_PARAM)) {
            String ibanPrefix = newBankInfo.get(IBAN_PREFIX_PARAM);
            if (isBankExistsByIbanPrefix(ibanPrefix, "create")) return;
            bankToUpdate.setIbanPrefix(ibanPrefix);
        }
        if (newBankInfo.containsKey(INDIVIDUALS_FEE_PARAM))
            bankToUpdate.setIndividualsFee(Double.parseDouble(newBankInfo.get(INDIVIDUALS_FEE_PARAM)));
        if (newBankInfo.containsKey(LEGAL_ENTITIES_FEE_PARAM))
            bankToUpdate.setLegalEntitiesFee(Double.parseDouble(newBankInfo.get(LEGAL_ENTITIES_FEE_PARAM)));
        bankRepository.update(id, bankToUpdate);
    }

    private boolean isBankExistsByName(String name, String operation) {
        if (bankRepository.getByName(name) != null) {
            System.out.println("A bank with that name already exists");
            log.info(String.format("Failed to %s a bank named %s because it already exists", operation, name));
            return true;
        }
        return false;
    }

    private boolean isBankExistsByIbanPrefix(String ibanPrefix, String operation) {
        if (bankRepository.getByIbanPrefix(ibanPrefix) != null) {
            System.out.println("A bank with that iban prefix already exists");
            log.info(String.format("Failed to %s a bank with iban prefix %s because it already exists", operation, ibanPrefix));
            return true;
        }
        return false;
    }
}
