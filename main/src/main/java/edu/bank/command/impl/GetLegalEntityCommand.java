package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.LegalEntityFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.LegalEntityFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class GetLegalEntityCommand extends BaseCommand<LegalEntityFullInfo> {

    @Override
    public CommandResult<LegalEntityFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        CommandResult<LegalEntityFullInfo> commandResult = executeAnyCommand(commandDescription, id);
        commandResult.setCommandResultMapper(new LegalEntityFullInfoResultMapper());
        return commandResult;
    }
}
