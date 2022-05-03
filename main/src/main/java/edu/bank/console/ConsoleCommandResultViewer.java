package edu.bank.console;

import edu.bank.model.entity.Transaction;

public interface ConsoleCommandResultViewer {
    void showSuccessMessage(String message);

    void showFailureMessage(String message);

    void printReceipt(Transaction transaction);
}
