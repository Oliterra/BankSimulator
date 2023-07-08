package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.AccountMainInfo;
import edu.bank.model.dto.CreateBankClient;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.AccountMainInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class BecomeNewBankClientCommand extends BaseCommand<AccountMainInfo> {

    @Override
    public CommandResult<AccountMainInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        CreateBankClient createBankClient = getCreateBankClientDTO(commandDescription);
        CommandResult<AccountMainInfo> commandResult = executeAnyCommand(commandDescription, createBankClient);
        commandResult.setCommandResultMapper(new AccountMainInfoResultMapper());
        return commandResult;
    }

    private CreateBankClient getCreateBankClientDTO(CommandDescription commandDescription) throws ValidationException {
        long userId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "userId"));
        long bankId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "bankId"));
        CreateBankClient createBankClient = new CreateBankClient();
        createBankClient.setUserId(userId);
        createBankClient.setBankId(bankId);
        return createBankClient;
    }
}
