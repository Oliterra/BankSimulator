package edu.bank.model.command.impl;

import edu.bank.result.CommandResult;
import edu.bank.model.command.*;
import edu.bank.model.dto.LegalEntityToUpdateDTO;
import edu.bank.model.entity.LegalEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class UpdateLegalEntityCommand extends BaseCommand<LegalEntityToUpdateDTO, Void> {

    @Override
    public CommandExecutionInfo<LegalEntityToUpdateDTO> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        String name = getParamValueByNameOrReturnNull(commandDescription, "legalEntityName");
        String phone = getParamValueByNameOrReturnNull(commandDescription, "phone");
        LegalEntity individualToUpdate = new LegalEntity();
        individualToUpdate.setName(name);
        individualToUpdate.setPhone(phone);
        LegalEntityToUpdateDTO legalEntityToUpdateDTO = new LegalEntityToUpdateDTO();
        legalEntityToUpdateDTO.setId(id);
        legalEntityToUpdateDTO.setLegalEntity(individualToUpdate);
        CommandExecutionInfo<LegalEntityToUpdateDTO> commandExecutionInfo = new CommandExecutionInfo<>();
        commandExecutionInfo.setMethodParamInstance(legalEntityToUpdateDTO);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<Void> executeCommand(CommandExecutionInfo<LegalEntityToUpdateDTO> commandExecutionInfo) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        return super.executeCommand(commandExecutionInfo);
    }
}
