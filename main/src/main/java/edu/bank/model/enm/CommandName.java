package edu.bank.model.enm;

import java.util.Arrays;

import static edu.bank.model.enm.CommandParam.*;

public enum CommandName {

    CREATE_BANK("createBank", "create a new bank", new CommandParam[]{BANK_NAME,
            IBAN_PREFIX, INDIVIDUALS_FEE, LEGAL_ENTITIES_FEE}),
    GET_ALL_BANKS("allBanks", "get all banks", new CommandParam[]{}),
    GET_BANK("getBank", "get information about a bank", new CommandParam[]{BANK_ID}),
    UPDATE_BANK("updateBank", "update an existing bank", new CommandParam[]{BANK_ID,
            BANK_NAME, IBAN_PREFIX, INDIVIDUALS_FEE, LEGAL_ENTITIES_FEE}),
    DELETE_BANK("deleteBank", "delete a bank from the system, all accounts of its users " +
            "their transaction history(the user is deleted if he belonged only to this bank)", new CommandParam[]{BANK_ID}),
    CREATE_INDIVIDUAL("createIndividual", "create a new individual", new CommandParam[]{BANK_ID,
            INDIVIDUAL_USER_FIRST_NAME, INDIVIDUAL_USER_LAST_NAME, INDIVIDUAL_USER_PATRONYMIC, USER_PHONE}),
    GET_ALL_INDIVIDUALS("allIndividuals", "get all individuals", new CommandParam[]{}),
    GET_INDIVIDUAL("getIndividual", "get information about an individual", new CommandParam[]{USER_ID}),
    UPDATE_INDIVIDUAL("updateIndividual", "update an existing individual", new CommandParam[]{USER_ID,
            INDIVIDUAL_USER_FIRST_NAME, INDIVIDUAL_USER_LAST_NAME, INDIVIDUAL_USER_PATRONYMIC, USER_PHONE}),
    CREATE_LEGAL_ENTITY("createLegalEntity", "create a new legal entity", new CommandParam[]{BANK_ID,
            LEGAL_ENTITY_USER_NAME, USER_PHONE}),
    GET_ALL_LEGAL_ENTITIES("allLegalEntities", "get all legal entities", new CommandParam[]{}),
    GET_LEGAL_ENTITY("getLegalEntity", "get information about a legal entity", new CommandParam[]{USER_ID}),
    UPDATE_LEGAL_ENTITY("updateLegalEntity", "update an existing legal entity", new CommandParam[]{USER_ID,
            LEGAL_ENTITY_USER_NAME, USER_PHONE}),
    DELETE_USER("deleteUser", "delete a user from the system(all her/his accounts and transaction history)"
            , new CommandParam[]{USER_ID}),
    BECOME_NEW_BANK_CLIENT("newBank", "an opportunity for an existing user to register with a new bank",
            new CommandParam[]{BANK_ID, USER_ID}),
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

    CommandName(String commandName, String commandDescription, CommandParam[] commandParams) {
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
