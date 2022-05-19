package edu.bank.command.info;

import lombok.Data;

import java.util.Map;

@Data
public class CommandDescription {

    private String name;
    private Map<String, String> params;
}
