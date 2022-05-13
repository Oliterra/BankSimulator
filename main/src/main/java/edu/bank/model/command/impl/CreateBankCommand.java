package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.BankFullInfo;
import edu.bank.model.entity.Bank;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.BankFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateBankCommand extends BaseCommand {

    @Override
    public CommandResult<BankFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException {
        Bank bank = getBank(commandDescription);
        CommandResult<BankFullInfo> commandResult = super.executeAnyCommand(commandDescription, bank);
        commandResult.setCommandResultMapper(new BankFullInfoResultMapper());
        return commandResult;
    }

    private Bank getBank(CommandDescription commandDescription) {
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
