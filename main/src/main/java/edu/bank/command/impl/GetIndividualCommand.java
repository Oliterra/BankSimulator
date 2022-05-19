package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.IndividualFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.IndividualFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class GetIndividualCommand extends BaseCommand<IndividualFullInfo> {

    @Override
    public CommandResult<IndividualFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        CommandResult<IndividualFullInfo> commandResult = executeAnyCommand(commandDescription, id);
        commandResult.setCommandResultMapper(new IndividualFullInfoResultMapper());
        return commandResult;
    }
}
