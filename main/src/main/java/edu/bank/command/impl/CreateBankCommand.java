package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.BankFullInfo;
import edu.bank.model.entity.Bank;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.BankFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateBankCommand extends BaseCommand<BankFullInfo> {

    @Override
    public CommandResult<BankFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        Bank bank = getBank(commandDescription);
        CommandResult<BankFullInfo> commandResult = executeAnyCommand(commandDescription, bank);
        commandResult.setCommandResultMapper(new BankFullInfoResultMapper());
        return commandResult;
    }

    private Bank getBank(CommandDescription commandDescription) throws ValidationException {
        String bankName = getParamValueByNameOrThrowException(commandDescription, "name");
        String ibanPrefix = getParamValueByNameOrThrowException(commandDescription, "ibanPrefix");
        double individualFee = Double.parseDouble(getParamValueByNameOrThrowException(commandDescription, "individualsFee"));
        double legalEntityFee = Double.parseDouble(getParamValueByNameOrThrowException(commandDescription, "legalEntitiesFee"));
        Bank bankToCreate = new Bank();
        bankToCreate.setName(bankName);
        bankToCreate.setIbanPrefix(ibanPrefix);
        bankToCreate.setIndividualsFee(individualFee);
        bankToCreate.setLegalEntitiesFee(legalEntityFee);
        return bankToCreate;
    }
}
