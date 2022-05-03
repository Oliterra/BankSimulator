package edu.bank.command.model;

import lombok.Data;

import static edu.bank.model.enm.ConsoleColor.*;

@Data
public class CommandParam implements Cloneable {

    private String name;
    private String description;
    private String value;

    @Override
    public String toString() {
        return "\t" + GREEN_BOLD + name + RESET + " - " + description + "\n";
    }

    @Override
    public CommandParam clone() {
        try {
            return (CommandParam) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
