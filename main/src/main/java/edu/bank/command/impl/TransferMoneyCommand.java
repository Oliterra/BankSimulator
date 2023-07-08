package edu.bank.command.impl;

import edu.bank.exeption.ValidationException;
import edu.bank.command.info.CommandDescription;
import edu.bank.model.dto.TransactionFullInfo;
import edu.bank.model.dto.TransferMoneyInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.impl.TransactionFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class TransferMoneyCommand extends BaseCommand<TransactionFullInfo> {

    @Override
    public CommandResult<TransactionFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException, ValidationException {
        TransferMoneyInfo transferMoneyInfo = getTransferMoneyInfo(commandDescription);
        CommandResult<TransactionFullInfo> commandResult = executeAnyCommand(commandDescription, transferMoneyInfo);
        commandResult.setCommandResultMapper(new TransactionFullInfoResultMapper());
        return commandResult;
    }

    private TransferMoneyInfo getTransferMoneyInfo(CommandDescription commandDescription) {
        String fromIban = getParamValueByNameOrReturnNull(commandDescription, "fromIban");
        String toIban = getParamValueByNameOrReturnNull(commandDescription, "toIban");
        double sum = Double.parseDouble(getParamValueByNameOrReturnNull(commandDescription, "sum"));
        TransferMoneyInfo transferMoneyInfo = new TransferMoneyInfo();
        transferMoneyInfo.setFromIban(fromIban);
        transferMoneyInfo.setToIban(toIban);
        transferMoneyInfo.setSum(sum);
        return transferMoneyInfo;
    }
}
