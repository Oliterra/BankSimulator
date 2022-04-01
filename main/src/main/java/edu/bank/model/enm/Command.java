package edu.bank.model.enm;

import java.util.Arrays;

import static edu.bank.model.enm.CommandParam.*;

public enum Command {

    CREATE_BANK("createBank", "create a new bank", new CommandParam[]{BANK_NAME,
            IBAN_PREFIX, INDIVIDUALS_FEE, LEGAL_ENTITIES_FEE}),
    UPDATE_BANK("updateBank", "update an existing bank", new CommandParam[]{BANK_ID,
            BANK_NAME, IBAN_PREFIX, INDIVIDUALS_FEE, LEGAL_ENTITIES_FEE}),
    CREATE_INDIVIDUAL("createIndividual", "create a new individual", new CommandParam[]{BANK_ID,
            INDIVIDUAL_USER_FIRST_NAME, INDIVIDUAL_USER_LAST_NAME, INDIVIDUAL_USER_PATRONYMIC, USER_PHONE}),
    CREATE_LEGAL_ENTITY("createLegalEntity", "create a new legal entity", new CommandParam[]{BANK_ID,
            LEGAL_ENTITY_USER_NAME, USER_PHONE}),
    CREATE_ACCOUNT("createAcc", "create a new account for a bank customer", new CommandParam[]{
            BANK_ID, USER_ID, ACCOUNT_CURRENCY}),
    GET_USER_ACCOUNTS("showAcs", "get a list of all user accounts and their balance",
            new CommandParam[]{USER_ID, ACCOUNT_CURRENCY}),
    TRANSFER_MONEY("transfer", "withdraw money from one account and put it on another",
            new CommandParam[]{MONEY_AMOUNT, FROM_ACCOUNT, TO_ACCOUNT}),
    GET_TRANSACTION_HISTORY("history", "get transaction history of a user for a certain period of time",
            new CommandParam[]{ACCOUNT_IBAN, FROM_DATE, TO_DATE});

    private final String commandName;
    private final String commandDescription;
    private final CommandParam[] commandParams;

    Command(String commandName, String commandDescription, CommandParam[] commandParams) {
        this.commandName = commandName;
        this.commandDescription = commandDescription;
        this.commandParams = commandParams;
    }

    public String getCommandName() {
        return commandName;
    }

    public CommandParam[] getCommandParams() {
        return commandParams;
    }

    @Override
    public String toString() {
        return commandName + " - " + commandDescription + "\nparams:\n" + Arrays.toString(commandParams);
    }
}
