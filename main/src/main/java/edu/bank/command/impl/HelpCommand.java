package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.CommandFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.CommandFullInfoListResultMapper;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class HelpCommand extends BaseCommand<Set<CommandFullInfo>> {

    @Override
    public CommandResult<Set<CommandFullInfo>> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        CommandResult<Set<CommandFullInfo>> commandResult = super.executeCommand(commandDescription);
        commandResult.setCommandResultMapper(new CommandFullInfoListResultMapper());
        return commandResult;
    }
}
