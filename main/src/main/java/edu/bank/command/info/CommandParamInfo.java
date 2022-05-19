package edu.bank.command.info;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CommandParamInfo {

    private String name;
    private boolean nullable;
    private String content;
    private int minLength;
    private int maxLength;
    private double minValue;
    private double maxValue;
}
