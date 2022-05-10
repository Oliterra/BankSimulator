package edu.bank.model.command.impl;

import edu.bank.result.mapper.BankFullInfoDTOResultMapper;
import edu.bank.result.CommandResult;
import edu.bank.model.command.*;
import edu.bank.model.dto.BankFullInfoDTO;
import edu.bank.model.entity.Bank;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class CreateBankCommand extends BaseCommand<Bank, BankFullInfoDTO>{

    @Override
    public CommandExecutionInfo<Bank> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        String bankName = getParamValueByNameOrThrowException(commandDescription, "bankName");
        String ibanPrefix = getParamValueByNameOrThrowException(commandDescription, "ibanPrefix");
        double individualFee = Double.parseDouble(getParamValueByNameOrThrowException(commandDescription, "individualsFee"));
        double legalEntityFee = Double.parseDouble(getParamValueByNameOrThrowException(commandDescription, "legalEntitiesFee"));
        Bank bankToCreate = new Bank();
        bankToCreate.setName(bankName);
        bankToCreate.setIbanPrefix(ibanPrefix);
        bankToCreate.setIndividualsFee(individualFee);
        bankToCreate.setLegalEntitiesFee(legalEntityFee);
        CommandExecutionInfo<Bank> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setMethodParamInstance(bankToCreate);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<BankFullInfoDTO> executeCommand(CommandExecutionInfo<Bank> commandExecutionInfo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        CommandResult<BankFullInfoDTO> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new BankFullInfoDTOResultMapper());
        return commandResult;
    }
}
