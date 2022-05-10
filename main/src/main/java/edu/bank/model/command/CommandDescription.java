package edu.bank.model.command;

import lombok.Data;

import java.util.Map;

@Data
public class CommandDescription {

    protected String name;
    private Map<String, String> params;
}
