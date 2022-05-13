package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.LegalEntityFullInfo;
import edu.bank.model.dto.LegalEntityToCreate;
import edu.bank.model.entity.LegalEntity;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.LegalEntityFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateLegalEntityCommand extends BaseCommand {

    @Override
    public CommandResult<LegalEntityFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        LegalEntityToCreate legalEntityToCreate = getLegalEntityToCreate(commandDescription);
        CommandResult<LegalEntityFullInfo> commandResult = super.executeAnyCommand(commandDescription, legalEntityToCreate);
        commandResult.setCommandResultMapper(new LegalEntityFullInfoResultMapper());
        return commandResult;
    }

    private LegalEntityToCreate getLegalEntityToCreate(CommandDescription commandDescription) {
        long bankId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "bankId"));
        String name = getParamValueByNameOrThrowException(commandDescription, "legalEntityName");
        String phone = getParamValueByNameOrThrowException(commandDescription, "phone");
        LegalEntity legalEntity = new LegalEntity();
        legalEntity.setName(name);
        legalEntity.setPhone(phone);
        LegalEntityToCreate legalEntityToCreate = new LegalEntityToCreate();
        legalEntityToCreate.setBankId(bankId);
        legalEntityToCreate.setLegalEntity(legalEntity);
        return legalEntityToCreate;
    }
}
