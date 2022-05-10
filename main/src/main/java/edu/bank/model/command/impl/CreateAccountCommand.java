package edu.bank.model.command.impl;

import edu.bank.result.mapper.AccountMainInfoDTOResultMapper;
import edu.bank.result.CommandResult;
import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.command.CommandExecutionInfo;
import edu.bank.model.command.CommandInfo;
import edu.bank.model.dto.AccountMainInfoDTO;
import edu.bank.model.dto.CreateAccountDTO;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class CreateAccountCommand extends BaseCommand<CreateAccountDTO, AccountMainInfoDTO> {

    @Override
    public CommandExecutionInfo<CreateAccountDTO> prepareCommand(CommandDescription commandDescription, CommandInfo commandInfo) {
        long userId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "userId"));
        long bankId = Long.parseLong(getParamValueByNameOrThrowException(commandDescription, "bankId"));
        String currency = getParamValueByNameOrReturnNull(commandDescription, "currency");
        CommandExecutionInfo<CreateAccountDTO> commandExecutionInfo = new CommandExecutionInfo<>();
        CreateAccountDTO createAccountDTO = new CreateAccountDTO();
        createAccountDTO.setUserId(userId);
        createAccountDTO.setBankId(bankId);
        createAccountDTO.setCurrency(currency);
        commandExecutionInfo.setMethodParamInstance(createAccountDTO);
        commandExecutionInfo.setCommandInfo(commandInfo);
        return commandExecutionInfo;
    }

    @Override
    public CommandResult<AccountMainInfoDTO> executeCommand(CommandExecutionInfo<CreateAccountDTO> commandExecutionInfo) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CommandResult<AccountMainInfoDTO> commandResult = super.executeCommand(commandExecutionInfo);
        commandResult.setCommandResultMapper(new AccountMainInfoDTOResultMapper());
        return commandResult;
    }
}
