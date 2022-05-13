package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.LegalEntityFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.LegalEntityFullInfoListResultMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllLegalEntitiesCommand extends BaseCommand {

    @Override
    public CommandResult<List<LegalEntityFullInfo>> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        CommandResult<List<LegalEntityFullInfo>> commandResult = super.executeAnyCommand(commandDescription);
        commandResult.setCommandResultMapper(new LegalEntityFullInfoListResultMapper());
        return commandResult;
    }
}
