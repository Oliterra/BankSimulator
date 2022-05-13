package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.LegalEntityToUpdate;
import edu.bank.model.entity.LegalEntity;
import edu.bank.result.CommandResult;
import org.springframework.stereotype.Component;

@Component
public class UpdateLegalEntityCommand extends BaseCommand{

    @Override
    public CommandResult<String> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        LegalEntityToUpdate legalEntityToUpdate = getLegalEntityToUpdate(commandDescription);
        return super.executeAnyCommand(commandDescription, legalEntityToUpdate);
    }

    private LegalEntityToUpdate getLegalEntityToUpdate(CommandDescription commandDescription) {
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
