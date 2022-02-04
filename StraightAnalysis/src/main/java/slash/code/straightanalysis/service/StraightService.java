package slash.code.straightanalysis.service;


import slash.code.straightanalysis.model.Card;

import java.util.List;
import java.util.Map;

public interface StraightService {

    Map<String, List<Card>> straightFinder(Map<String, List<Card>> cardCheck);

    String mapToCrypt(Map<String, List<Card>> cardMap);

    Map<String, List<Card>> decryptToMap(String cardMap);

}
