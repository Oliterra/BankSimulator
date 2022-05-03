package edu.bank.model.enm;

public enum ConsoleColors {
    RESET("\033[0m"),
    RED_BOLD("\033[1;31m"),
    GREEN_BOLD("\033[1;32m"),
    YELLOW_BOLD("\033[1;33m"),
    BLUE_BOLD("\033[1;34m"),
    MAGENTA_BOLD("\033[1;35m"),
    CYAN_BOLD("\033[1;36m"),
    WHITE_BOLD("\033[1;37m");

    private final String code;

    ConsoleColors(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
