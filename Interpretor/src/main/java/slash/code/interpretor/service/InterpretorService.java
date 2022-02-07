package slash.code.interpretor.service;

import slash.code.interpretor.model.Card;

import java.util.List;
import java.util.Map;

public interface InterpretorService {

    void initiateDeck();

    Card getOneCard();

    String straightAnalysis(Map<String, List<Card>> cardMap);

    Map<String, List<Card>> getCards(Integer number);

    Map<String, List<Card>> analyseCards(Map<String, List<Card>> cardMap);

    String mapToCrypt(Map<String, List<Card>> cardMap);

    Map<String, List<Card>> cryptToMap(String cardMap);


}
