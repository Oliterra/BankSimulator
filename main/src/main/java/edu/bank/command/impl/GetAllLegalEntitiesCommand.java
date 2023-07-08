package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.LegalEntityFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.LegalEntityFullInfoListResultMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllLegalEntitiesCommand extends BaseCommand<List<LegalEntityFullInfo>> {

    @Override
    public CommandResult<List<LegalEntityFullInfo>> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        CommandResult<List<LegalEntityFullInfo>> commandResult = super.executeCommand(commandDescription);
        commandResult.setCommandResultMapper(new LegalEntityFullInfoListResultMapper());
        return commandResult;
    }
}
