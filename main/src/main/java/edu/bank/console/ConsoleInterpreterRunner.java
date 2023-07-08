package edu.bank.console;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ConsoleInterpreterRunner {

    private final ConsoleInterpreter consoleInterpreter;

    @PostConstruct
    public void initConsoleInterpreter(){
        consoleInterpreter.interpretConsoleInput();
    }
}
