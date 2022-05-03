package edu.bank.model.command;

import lombok.Data;

@Data
public class CommandParam implements Cloneable {

    private String name;
    private String description;
    private String value;

    /*@Override
    public String toString() {
        return "\t" + GREEN_BOLD + name + RESET + " - " + description + "\n";
    }*/

    @Override
    public String toString() {
        return "CommandParam{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", value='" + value + '\'' +
                '}';
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
