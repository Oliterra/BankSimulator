package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.LegalEntityFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.LegalEntityFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class GetLegalEntityCommand extends BaseCommand {

    @Override
    public CommandResult<LegalEntityFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        CommandResult<LegalEntityFullInfo> commandResult = super.executeAnyCommand(commandDescription, id);
        commandResult.setCommandResultMapper(new LegalEntityFullInfoResultMapper());
        return commandResult;
    }
}
