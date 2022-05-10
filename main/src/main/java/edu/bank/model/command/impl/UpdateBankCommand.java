package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.model.command.*;
import edu.bank.model.dto.BankToUpdateDTO;
import edu.bank.model.entity.Bank;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class UpdateBankCommand extends BaseCommand<BankToUpdateDTO, Void> {

    @Override
    public CommandExecutionInfo<BankToUpdateDTO> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        String bankName = getParamValueByNameOrReturnNull(commandDescription, "bankName");
        String ibanPrefix = getParamValueByNameOrReturnNull(commandDescription, "ibanPrefix");
        String individualFeeString = getParamValueByNameOrReturnNull(commandDescription, "individualFee");
        double individualFee = 0;
        if(individualFeeString != null) individualFee = Double.parseDouble(individualFeeString);
        String legalEntityFeeString = getParamValueByNameOrReturnNull(commandDescription, "legalEntityFee");
        double legalEntityFee = 0;
        if(legalEntityFeeString != null) legalEntityFee = Double.parseDouble(legalEntityFeeString);
        Bank bankToUpdate = new Bank();
        bankToUpdate.setName(bankName);
        bankToUpdate.setIbanPrefix(ibanPrefix);
        bankToUpdate.setIndividualsFee(individualFee);
        bankToUpdate.setLegalEntitiesFee(legalEntityFee);
        BankToUpdateDTO bankToUpdateDTO = new BankToUpdateDTO();
        bankToUpdateDTO.setId(id);
        bankToUpdateDTO.setBank(bankToUpdate);
        CommandExecutionInfo<BankToUpdateDTO> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setMethodParamInstance(bankToUpdateDTO);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<Void> executeCommand(CommandExecutionInfo<BankToUpdateDTO> commandExecutionInfo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return super.executeCommand(commandExecutionInfo);
    }
}
