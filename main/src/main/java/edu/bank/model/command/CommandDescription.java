package edu.bank.model.command;

import lombok.Data;

import java.util.Map;

@Data
public class CommandDescription {

    private String name;
    private Map<String, String> params;
}
