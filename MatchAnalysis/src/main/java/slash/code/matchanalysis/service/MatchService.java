package slash.code.matchanalysis.service;

import slash.code.matchanalysis.model.Card;

import java.util.List;
import java.util.Map;

public interface MatchService {
    Map<String, List<Card>> matchingCards(Map<String, List<Card>> cardCheck);

    String mapToCrypt(Map<String, List<Card>> cardMap);

    Map<String, List<Card>> decryptToMap(String cardMap);
}
