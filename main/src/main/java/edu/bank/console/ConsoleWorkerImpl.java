package edu.bank.console;

import edu.bank.exeption.BusinessLogicException;
import edu.bank.exeption.DAOException;
import edu.bank.model.enm.Command;
import edu.bank.service.*;
import edu.bank.service.impl.AccountServiceImpl;
import edu.bank.service.impl.BankServiceImpl;
import edu.bank.service.impl.TransactionServiceImpl;
import edu.bank.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class ConsoleWorkerImpl implements ConsoleWorker, CommandManager {

    private final BankService bankService = new BankServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final AccountService accountService = new AccountServiceImpl();
    private final TransactionService transactionService = new TransactionServiceImpl();
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final Logger log = Logger.getLogger(ConsoleWorkerImpl.class);

    @Override
    public void simulateBankOperations() {
        System.out.println("Welcome!\nTo view a list of all available commands, type \"help\"." +
                "\nTo exit the application, use \"exit\".");
        while (true) {
            try {
                String command = reader.readLine();
                if (command.equals("exit")) break;
                parseAndExecuteCommand(command);
            } catch (IOException e) {
                System.out.println("Invalid command format. To view all commands use \"help\"");
                log.error("An error occurred while reading the data: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid data format");
                log.error("An error occurred while reading the data: " + e.getMessage());
            } catch (DAOException e) {
                System.out.println("Sorry, an internal error has occurred. Try again later");
                log.error("An error occurred while working with the database: " + e.getMessage());
            } catch (BusinessLogicException e) {
                String message = e.getMessage();
                System.out.println(message);
                log.error("A business logic error has occurred: " + message);
            } catch (Exception e) {
                System.out.println("Sorry, an internal error has occurred. Try again later");
                log.error("A error has occurred: " + e.getMessage());
            }
        }
    }

    private void parseAndExecuteCommand(String fullCommand) throws IOException {
        String[] commandParts = fullCommand.split(" ");
        if (commandParts[0].equals("help") && commandParts.length == 1) {
            showAllCommands();
            return;
        }
        Command command = getCommandByName(commandParts[0]);
        Map<String, String> commandParams = getCommandParams(command, commandParts);
        executeCommand(command, commandParams);
    }

    private void executeCommand(Command command, Map<String, String> params) {
        switch (command) {
            case CREATE_BANK -> bankService.createBank(params);
            case GET_ALL_BANKS -> bankService.getAllBanks(params);
            case GET_BANK -> bankService.getBank(params);
            case UPDATE_BANK -> bankService.updateBank(params);
            case DELETE_BANK -> bankService.deleteBank(params);
            case CREATE_INDIVIDUAL -> userService.createIndividual(params);
            case GET_ALL_INDIVIDUALS -> userService.getAllIndividuals(params);
            case GET_INDIVIDUAL -> userService.getIndividual(params);
            case UPDATE_INDIVIDUAL -> userService.updateIndividual(params);
            case CREATE_LEGAL_ENTITY -> userService.createLegalEntity(params);
            case GET_ALL_LEGAL_ENTITIES -> userService.getAllLegalEntities(params);
            case GET_LEGAL_ENTITY -> userService.getLegalEntity(params);
            case UPDATE_LEGAL_ENTITY -> userService.updateLegalEntity(params);
            case DELETE_USER -> userService.deleteUser(params);
            case BECOME_NEW_BANK_CLIENT -> bankService.addExistingUser(params);
            case CREATE_ACCOUNT -> accountService.createNewAccount(params);
            case GET_USER_ACCOUNTS -> accountService.getAllByUser(params);
            case TRANSFER_MONEY -> accountService.transferMoney(params);
            case GET_TRANSACTION_HISTORY -> transactionService.getTransactionHistory(params);
        }
    }
}
