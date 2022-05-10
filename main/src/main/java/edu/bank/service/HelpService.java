package edu.bank.service;

import edu.bank.model.command.CommandsInfo;
import edu.bank.model.dto.CommandFullInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class HelpService {

    private CommandsInfo commandsInfo;

    private final ModelMapper modelMapper;

    @PostConstruct
    private void init() {
        Yaml yaml = new Yaml(new Constructor(CommandsInfo.class));
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("commands-list.yml");
        commandsInfo = yaml.load(inputStream);
        if (commandsInfo.getCommandsInfo().isEmpty()) throw new RuntimeException();
    }

    public Set<CommandFullInfoDTO> getAllCommands() {
        return commandsInfo.getCommandsInfo().stream().map(c -> modelMapper.map(c, CommandFullInfoDTO.class)).collect(Collectors.toSet());
    }
}
