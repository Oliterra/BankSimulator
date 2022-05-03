package edu.bank.command;

import edu.bank.command.model.Command;
import edu.bank.command.model.CommandParam;
import edu.bank.service.AccountService;
import edu.bank.service.BankService;
import edu.bank.service.TransactionService;
import edu.bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandExecutorImpl implements CommandExecutor {

    private final BankService bankService;
    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @Override
    public void executeCommand(Command command) {
        String name = command.getName();
        Set<CommandParam> params = command.getParams();
        switch (name) {
            case "createBank" -> bankService.createBank(params);
            case "allBanks" -> bankService.getAllBanks(params);
            case "getBank" -> bankService.getBank(params);
            case "updateBank" -> bankService.updateBank(params);
            case "deleteBank" -> bankService.deleteBank(params);
            case "createIndividual" -> userService.createIndividual(params);
            case "allIndividuals" -> userService.getAllIndividuals(params);
            case "getIndividual" -> userService.getIndividual(params);
            case "updateIndividual" -> userService.updateIndividual(params);
            case "createLegalEntity" -> userService.createLegalEntity(params);
            case "allLegalEntities" -> userService.getAllLegalEntities(params);
            case "getLegalEntity" -> userService.getLegalEntity(params);
            case "updateLegalEntity" -> userService.updateLegalEntity(params);
            case "deleteUser" -> userService.deleteUser(params);
            case "newBank" -> bankService.addExistingUser(params);
            case "createAcc" -> accountService.createNewAccount(params);
            case "showAcs" -> accountService.getAllByUser(params);
            case "transfer" -> accountService.transferMoney(params);
            case "history" -> transactionService.getTransactionHistory(params);
            default -> throw new RuntimeException();
        }
    }
}
