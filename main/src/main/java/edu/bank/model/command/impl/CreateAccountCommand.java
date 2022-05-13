package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.AccountMainInfo;
import edu.bank.model.dto.CreateAccount;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.AccountMainInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateAccountCommand extends BaseCommand {

    @Override
    public CommandResult<AccountMainInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException {
        CreateAccount createAccount = getCreateAccountDTO(commandDescription);
        CommandResult<AccountMainInfo> commandResult = super.executeAnyCommand(commandDescription, createAccount);
        commandResult.setCommandResultMapper(new AccountMainInfoResultMapper());
        return commandResult;
    }

    private CreateAccount getCreateAccountDTO(CommandDescription commandDescription) {
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
