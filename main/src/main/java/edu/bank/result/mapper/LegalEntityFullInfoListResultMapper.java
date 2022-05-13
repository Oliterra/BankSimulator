package edu.bank.result.mapper;

import edu.bank.model.dto.LegalEntityFullInfo;
import edu.bank.model.enm.ConsoleColor;
import edu.bank.result.CommandResultMapper;

import java.util.List;

public class LegalEntityFullInfoListResultMapper implements CommandResultMapper<List<LegalEntityFullInfo>> {

    @Override
    public String mapResult(List<LegalEntityFullInfo> legalEntityFullInfos) {
        if (legalEntityFullInfos.isEmpty())
            return ConsoleColor.YELLOW_BOLD + "No legal entities found" + ConsoleColor.RESET;
        else {
            LegalEntityFullInfoResultMapper legalEntityFullInfoResultMapper = new LegalEntityFullInfoResultMapper();
            StringBuilder resultString = new StringBuilder();
            resultString.append(ConsoleColor.YELLOW_BOLD + "List of all legal entities:");
            legalEntityFullInfos.forEach(i -> resultString.append(legalEntityFullInfoResultMapper.mapResult(i)));
            return resultString.toString();
        }
    }
}
