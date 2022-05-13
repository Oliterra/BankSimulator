package edu.bank.model.command.impl;

import edu.bank.model.command.BaseCommand;
import edu.bank.model.command.CommandDescription;
import edu.bank.model.dto.TransactionFullInfo;
import edu.bank.model.dto.TransferMoneyInfo;
import edu.bank.result.CommandResult;
import edu.bank.result.mapper.TransactionFullInfoResultMapper;
import org.springframework.stereotype.Component;

@Component
public class TransferMoneyCommand extends BaseCommand {

    @Override
    public CommandResult<TransactionFullInfo> executeCommand(CommandDescription commandDescription) throws ReflectiveOperationException {
        TransferMoneyInfo transferMoneyInfo = getTransferMoneyInfo(commandDescription);
        CommandResult<TransactionFullInfo> commandResult = super.executeAnyCommand(commandDescription, transferMoneyInfo);
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
