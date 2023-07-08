package edu.bank.command.lifecycle;

import edu.bank.exeption.InternalError;
import edu.bank.command.info.CommandsInfo;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Getter
@Component
public class CommandsInfoStorage {

    private CommandsInfo commandsInfo;

    @PostConstruct
    private void init() {
        Yaml yaml = new Yaml(new Constructor(CommandsInfo.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("commands-list.yml");
        commandsInfo = yaml.load(inputStream);
        if (commandsInfo.getCommandsInfo().isEmpty()) throw new InternalError("Commands file is empty");
    }
}
