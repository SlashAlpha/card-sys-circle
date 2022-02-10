package slash.code.decksys.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import slash.code.decksys.model.Card;

import java.util.*;

@Service
public class DeckServices implements DeckService {


    JmsTemplate jmsTemplate;


    public DeckServices(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }


    @Override
    public Card getOneCardFromDeck(List<Card> cards) {
        Card randomCard;

        randomCard = cards.get(0);
        cards.remove(0);


        return randomCard;
    }

    @Override
    public List<Card> getCardsFromDeck(List<Card> cards, Integer number) {
        List<Card> returnCards = new ArrayList<>();

        while (returnCards.size() < number && number < 53) {

            returnCards.add(cards.get(0));
            cards.remove(0);
        }
        return returnCards;
    }



    @Override
    public String mapToCrypt(Map<String, List<Card>> cardMap) {
        if (cardMap.isEmpty()) {
            return "";
        }
        String key = cardMap.keySet().stream().iterator().next();
        List<Card> cards = cardMap.get(key);
        StringBuilder result = new StringBuilder(key + "--result--");
        int count = 1;
        for (Card card :
                cards
        ) {
            if (count == cards.size()) {

                result.append(card.getId().toString()).append("--data--").append(card.getColor()).append("--data--").append(card.getValue()).append("--data--").append(card.getFaceVal()).append("--data--").append(card.getDescription()).append("--data--").append(card.getNumber());

            } else {
                result.append(card.getId().toString()).append("--data--").append(card.getColor()).append("--data--").append(card.getValue()).append("--data--").append(card.getFaceVal()).append("--data--").append(card.getDescription()).append("--data--").append(card.getNumber()).append("--card--");
            }
            count++;
        }


        return result.toString();
    }

    @Override
    public Map<String, List<Card>> cryptToMap(String cardMap) {
        if (cardMap == null) {
            return new HashMap<>();
        }
        List<String> cardResult = List.of(cardMap.split("--result--"));
        List<String> objects = List.of(cardResult.get(1).split("--card--"));
        //  System.out.println(objects);
        List<Card> cardList = new ArrayList<>();
        Map<String, List<Card>> resultMap = new HashMap<>();
        for (String s : objects) {
            List<String> cardData = List.of(s.split("--data--"));

            cardList.add(Card.builder().id(UUID.fromString(cardData.get(0))).color(cardData.get(1)).value(Integer.valueOf(cardData.get(2))).faceVal(Integer.valueOf(cardData.get(3))).description(cardData.get(4)).number(Integer.valueOf(cardData.get(5))).build());
        }
        resultMap.put(cardResult.get(0), cardList);
        return resultMap;
    }



}
