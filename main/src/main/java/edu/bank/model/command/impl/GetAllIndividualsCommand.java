package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.IndividualFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.IndividualFullInfoListResultMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllIndividualsCommand extends BaseCommand {

    @Override
    public CommandResult<List<IndividualFullInfo>> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        CommandResult<List<IndividualFullInfo>> commandResult = super.executeAnyCommand(commandDescription);
        commandResult.setCommandResultMapper(new IndividualFullInfoListResultMapper());
        return commandResult;
    }
}
