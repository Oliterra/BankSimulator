package edu.bank.model.enm;

public enum ConsoleColor {
    RESET("\033[0m"),
    RED_BOLD("\033[1;31m"),
    GREEN_BOLD("\033[1;32m"),
    YELLOW_BOLD("\033[1;33m"),
    MAGENTA_BOLD("\033[1;35m");

    private final String code;

    ConsoleColor(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
    }
