package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.LegalEntityToUpdate;
import edu.bank.model.entity.LegalEntity;
import edu.bank.result.CommandResult;
import org.springframework.stereotype.Component;

@Component
public class UpdateLegalEntityCommand extends BaseCommand<String>{

    @Override
    public CommandResult<String> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        LegalEntityToUpdate legalEntityToUpdate = getLegalEntityToUpdate(commandDescription);
        return executeAnyCommand(commandDescription, legalEntityToUpdate);
    }

    private LegalEntityToUpdate getLegalEntityToUpdate(CommandDescription commandDescription) throws ValidationException {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        String name = getParamValueByNameOrReturnNull(commandDescription, "legalEntityName");
        String phone = getParamValueByNameOrReturnNull(commandDescription, "phone");
        LegalEntity individualToUpdate = new LegalEntity();
        individualToUpdate.setName(name);
        individualToUpdate.setPhone(phone);
        LegalEntityToUpdate legalEntityToUpdate = new LegalEntityToUpdate();
        legalEntityToUpdate.setId(id);
        legalEntityToUpdate.setLegalEntity(individualToUpdate);
        return legalEntityToUpdate;
    }
}
