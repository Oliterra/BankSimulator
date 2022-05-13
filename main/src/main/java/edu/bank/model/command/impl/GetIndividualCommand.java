package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.IndividualFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.IndividualFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class GetIndividualCommand extends BaseCommand {

    @Override
    public CommandResult<IndividualFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException  {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        CommandResult<IndividualFullInfo> commandResult = super.executeAnyCommand(commandDescription, id);
        commandResult.setCommandResultMapper(new IndividualFullInfoResultMapper());
        return commandResult;
    }
}
