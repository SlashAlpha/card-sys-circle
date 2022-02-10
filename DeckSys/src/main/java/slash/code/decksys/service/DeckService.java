package slash.code.decksys.service;

import slash.code.decksys.model.Card;

import java.util.List;
import java.util.Map;

public interface DeckService {


    Card getOneCardFromDeck(List<Card> cards);

    List<Card> getCardsFromDeck(List<Card> cards, Integer number);

    Map<String, List<Card>> cryptToMap(String cardMap);

    String mapToCrypt(Map<String, List<Card>> cardMap);

}

