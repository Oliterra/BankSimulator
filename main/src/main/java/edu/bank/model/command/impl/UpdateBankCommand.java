package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.BankToUpdate;
import edu.bank.model.entity.Bank;
import edu.bank.result.CommandResult;
import org.springframework.stereotype.Component;

@Component
public class UpdateBankCommand extends BaseCommand {

    @Override
    public CommandResult<String> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException {
        BankToUpdate bankToUpdate = getBankToUpdate(commandDescription);
        return super.executeAnyCommand(commandDescription, bankToUpdate);
    }

    private BankToUpdate getBankToUpdate(CommandDescription commandDescription) {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        String bankName = getParamValueByNameOrReturnNull(commandDescription, "name");
        String ibanPrefix = getParamValueByNameOrReturnNull(commandDescription, "ibanPrefix");
        String individualFeeString = getParamValueByNameOrReturnNull(commandDescription, "individualFee");
        double individualFee = 0;
        if (individualFeeString != null) {
            individualFee = Double.parseDouble(individualFeeString);
        }
        String legalEntityFeeString = getParamValueByNameOrReturnNull(commandDescription, "legalEntityFee");
        double legalEntityFee = 0;
        if (legalEntityFeeString != null) {
            legalEntityFee = Double.parseDouble(legalEntityFeeString);
        }
        Bank bankToUpdate = new Bank();
        bankToUpdate.setName(bankName);
        bankToUpdate.setIbanPrefix(ibanPrefix);
        bankToUpdate.setIndividualsFee(individualFee);
        bankToUpdate.setLegalEntitiesFee(legalEntityFee);
        BankToUpdate bankToUpdateDTO = new BankToUpdate();
        bankToUpdateDTO.setId(id);
        bankToUpdateDTO.setBank(bankToUpdate);
        return bankToUpdateDTO;
    }
}
