package edu.bank.result.mapper.impl;

import edu.bank.model.dto.IndividualFullInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.mapper.CommandResultMapper;

import java.util.List;

public class IndividualFullInfoListResultMapper implements CommandResultMapper<List<IndividualFullInfo>> {

    @Override
    public String mapResult(List<IndividualFullInfo> individualFullInfos) {
        if (individualFullInfos.isEmpty())
            return ConsoleColor.YELLOW_BOLD + "No individuals found" + ConsoleColor.RESET;
        else {
            IndividualFullInfoResultMapper individualFullInfoResultMapper = new IndividualFullInfoResultMapper();
            StringBuilder resultString = new StringBuilder();
            resultString.append(ConsoleColor.YELLOW_BOLD + "List of all individuals:");
            individualFullInfos.forEach(i -> resultString.append(individualFullInfoResultMapper.mapResult(i)));
            return resultString.toString();
        }
    }
}
