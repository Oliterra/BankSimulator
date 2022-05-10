package edu.bank;

import edu.bank.console.ConsoleInterpreter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        ConsoleInterpreter consoleInterpreter = context.getBean(ConsoleInterpreter.class);
        consoleInterpreter.interpretConsoleInput();
    }
}
