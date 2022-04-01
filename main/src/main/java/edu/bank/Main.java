package edu.bank;

import edu.bank.console.ConsoleWorker;
import edu.bank.console.ConsoleWorkerImpl;

public class Main {

    private static final ConsoleWorker CONSOLE_WORKER = new ConsoleWorkerImpl();

    public static void main(String[] args) {
        CONSOLE_WORKER.simulateBankOperations();
    }
}
