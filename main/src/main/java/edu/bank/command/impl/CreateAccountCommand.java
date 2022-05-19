package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.AccountMainInfo;
import edu.bank.model.dto.CreateAccount;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.AccountMainInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountCommand extends BaseCommand<AccountMainInfo> {

    @Override
    public CommandResult<AccountMainInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        CreateAccount createAccount = getCreateAccountDTO(commandDescription);
        CommandResult<AccountMainInfo> commandResult = executeAnyCommand(commandDescription, createAccount);
        commandResult.setCommandResultMapper(new AccountMainInfoResultMapper());
        return commandResult;
    }

    private CreateAccount getCreateAccountDTO(CommandDescription commandDescription) throws ValidationException {
        long userId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "userId"));
        long bankId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "bankId"));
        String currency = getParamValueByNameOrReturnNull(commandDescription, "currency");
        CreateAccount createAccount = new CreateAccount();
        createAccount.setUserId(userId);
        createAccount.setBankId(bankId);
        createAccount.setCurrency(currency);
        return createAccount;
    }
}
