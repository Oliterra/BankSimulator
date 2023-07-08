package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.AccountMainInfo;
import edu.bank.model.dto.UserAndAccountMainInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.AccountMainInfoListResultMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetUserAccountsCommand extends BaseCommand<List<AccountMainInfo>> {

    @Override
    public CommandResult<List<AccountMainInfo>> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        UserAndAccountMainInfo userAndAccountMainInfo = getUserAndAccountMainInfo(commandDescription);
        CommandResult<List<AccountMainInfo>> commandResult = executeAnyCommand(commandDescription, userAndAccountMainInfo);
        commandResult.setCommandResultMapper(new AccountMainInfoListResultMapper());
        return commandResult;
    }

    private UserAndAccountMainInfo getUserAndAccountMainInfo(CommandDescription commandDescription) throws ValidationException {
        long userId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "userId"));
        String currency = getParamValueByNameOrReturnNull(commandDescription, "currency");
        UserAndAccountMainInfo userAndAccountMainInfo = new UserAndAccountMainInfo();
        userAndAccountMainInfo.setUserId(userId);
        userAndAccountMainInfo.setCurrency(currency);
        return userAndAccountMainInfo;
    }
}
