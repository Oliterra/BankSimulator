package edu.bank.enm;

public enum Command {

    CREATE_BANK("createBank", "create a new bank", new CommandParam[]{CommandParam.BANK_NAME,
            CommandParam.IBAN_PREFIX, CommandParam.INDIVIDUALS_FEE, CommandParam.LEGAL_ENTITIES_FEE}),
    UPDATE_BANK("updateBank", "update an existing bank", new CommandParam[]{CommandParam.BANK_ID,
            CommandParam.BANK_NAME, CommandParam.IBAN_PREFIX, CommandParam.INDIVIDUALS_FEE, CommandParam.LEGAL_ENTITIES_FEE}),
    CREATE_INDIVIDUAL("createIndividual", "create a new individual", new CommandParam[]{CommandParam.BANK_ID,
            CommandParam.INDIVIDUAL_USER_FIRST_NAME, CommandParam.INDIVIDUAL_USER_LAST_NAME, CommandParam.INDIVIDUAL_USER_PATRONYMIC, CommandParam.USER_PHONE}),
    CREATE_LEGAL_ENTITY("createLegalEntity", "create a new legal entity", new CommandParam[]{CommandParam.BANK_ID,
            CommandParam.LEGAL_ENTITY_USER_NAME, CommandParam.USER_PHONE}),
    GET_USER_ACCOUNTS("showAcs", "get a list of all user accounts and their balance",
            new CommandParam[]{CommandParam.USER_ID, CommandParam.ACCOUNT_CURRENCY}),
    TRANSFER_MONEY("transfer", "withdraw money from one account and put it on another",
            new CommandParam[]{CommandParam.MONEY_AMOUNT, CommandParam.FROM_ACCOUNT, CommandParam.TO_ACCOUNT});

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

    public String getCommandDescription() {
        return commandDescription;
    }

    public CommandParam[] getCommandParams() {
        return commandParams;
    }
}
