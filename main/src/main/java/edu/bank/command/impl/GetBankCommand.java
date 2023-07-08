package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.BankFullInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.BankFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class GetBankCommand extends BaseCommand<BankFullInfo> {

    @Override
    public CommandResult<BankFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        long id = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "id"));
        CommandResult<BankFullInfo> commandResult = executeAnyCommand(commandDescription, id);
        commandResult.setCommandResultMapper(new BankFullInfoResultMapper());
        return commandResult;
    }
}
