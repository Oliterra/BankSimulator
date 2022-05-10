package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.result.mapper.LegalEntityFullInfoDTOResultMapper;
import edu.bank.model.command.*;
import edu.bank.model.dto.LegalEntityFullInfoDTO;
import edu.bank.model.dto.LegalEntityToCreateDTO;
import edu.bank.model.entity.LegalEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class CreateLegalEntityCommand extends BaseCommand<LegalEntityToCreateDTO, LegalEntityFullInfoDTO> {

    @Override
    public CommandExecutionInfo<LegalEntityToCreateDTO> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long bankId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "bankId"));
        String name = getParamValueByNameOrThrowException(commandDescription, "legalEntityName");
        String phone = getParamValueByNameOrThrowException(commandDescription, "phone");
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setName(name);
        legalEntity.setPhone(phone);
        LegalEntityToCreateDTO legalEntityToCreateDTO = new LegalEntityToCreateDTO();
        legalEntityToCreateDTO.setBankId(bankId);
        legalEntityToCreateDTO.setLegalEntity(legalEntity);
        CommandExecutionInfo<LegalEntityToCreateDTO> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setMethodParamInstance(legalEntityToCreateDTO);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<LegalEntityFullInfoDTO> executeCommand(CommandExecutionInfo<LegalEntityToCreateDTO> commandExecutionInfo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        CommandResult<LegalEntityFullInfoDTO> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new LegalEntityFullInfoDTOResultMapper());
        return commandResult;
    }
}
