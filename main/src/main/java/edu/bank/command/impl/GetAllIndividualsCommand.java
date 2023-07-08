package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.IndividualFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.IndividualFullInfoListResultMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllIndividualsCommand extends BaseCommand<List<IndividualFullInfo>> {

    @Override
    public CommandResult<List<IndividualFullInfo>> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        CommandResult<List<IndividualFullInfo>> commandResult = super.executeCommand(commandDescription);
        commandResult.setCommandResultMapper(new IndividualFullInfoListResultMapper());
        return commandResult;
    }
}
