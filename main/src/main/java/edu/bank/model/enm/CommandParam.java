package edu.bank.model.enm;

public enum CommandParam {

    BANK_ID("bId", "set the bank id"),
    BANK_NAME("name", "set the bank name"),
    IBAN_PREFIX("ibanP", "set the iban prefix of the bank"),
    INDIVIDUALS_FEE("indFee", "set a fee for bank customers (individuals)"),
    LEGAL_ENTITIES_FEE("legFee", "set a fee for bank customers (legal entities)"),
    USER_ID("uId", "set the id of the user"),
    LEGAL_ENTITY_USER_NAME("leName", "set the first name of the user or the name of legal bank.entity"),
    INDIVIDUAL_USER_FIRST_NAME("fName", "set the first name of the user or the name of legal bank.entity"),
    INDIVIDUAL_USER_LAST_NAME("lName", "set the last name of the user"),
    INDIVIDUAL_USER_PATRONYMIC("patr", "set the patronymic of the user"),
    USER_PHONE("phone", "set the phone of the user"),
    ACCOUNT_CURRENCY("cur", "get/create user accounts with a certain currency"),
    FROM_ACCOUNT("from", "the account from which the money is withdrawn"),
    TO_ACCOUNT("to", "the account to which the money is deposited"),
    MONEY_AMOUNT("sum", "set the amount of money to transfer"),
    FROM_DATE("from", "the date from which the list of transactions begins(yyyy-mm-dd)"),
    TO_DATE("to", "the date on which the list of transactions ends(yyyy-mm-dd)"),
    ACCOUNT_IBAN("acc", "the account for which the transaction list is requested");

    private final String paramName;
    private final String paramDescription;

    CommandParam(String paramName, String paramDescription) {
        this.paramName = paramName;
        this.paramDescription = paramDescription;
    }

    public String getParamName() {
        return paramName;
    }

    @Override
    public String toString() {
        return "\t\t-" + paramName + " - " + paramDescription + "\n";
    }
}
