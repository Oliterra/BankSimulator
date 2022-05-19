package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.LegalEntityFullInfo;
import edu.bank.model.dto.LegalEntityToCreate;
import edu.bank.model.entity.LegalEntity;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.LegalEntityFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateLegalEntityCommand extends BaseCommand<LegalEntityFullInfo> {

    @Override
    public CommandResult<LegalEntityFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        LegalEntityToCreate legalEntityToCreate = getLegalEntityToCreate(commandDescription);
        CommandResult<LegalEntityFullInfo> commandResult = executeAnyCommand(commandDescription, legalEntityToCreate);
        commandResult.setCommandResultMapper(new LegalEntityFullInfoResultMapper());
        return commandResult;
    }

    private LegalEntityToCreate getLegalEntityToCreate(CommandDescription commandDescription) throws ValidationException {
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
