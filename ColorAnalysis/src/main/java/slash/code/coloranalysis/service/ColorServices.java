package slash.code.coloranalysis.service;


import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import slash.code.coloranalysis.model.Card;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ColorServices implements ColorService {


    private static final String[] color = {"Diamond", "Spade", "Heart", "Club"};
    JmsTemplate jmsTemplate;

    public ColorServices(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public Map<String, List<Card>> color(Map<String, List<Card>> cardMap) {
        List<Card> cards = cardMap.values().stream().iterator().next();
        cards = cards.stream().sorted(Comparator.comparingInt(Card::getValue).thenComparingInt(Card::getFaceVal).reversed()).collect(Collectors.toList());

        Map<String, List<Card>> colorMap = new HashMap<>();
        List<Card> chosenCard = new ArrayList<>();
        for (String colour : color
        ) {
            chosenCard.clear();

            chosenCard = cards.stream().filter(card -> card.getColor().equals(colour)).collect(Collectors.toList());

            if (chosenCard.size() >= 5) {

                colorMap.put(colour, chosenCard.stream().limit(5).collect(Collectors.toList()));

                return colorMap;
            }
        }
        return colorMap;
    }



    @Override
    public Map<String, List<Card>> decryptToMap(String cardMap) {
        if (cardMap == null) {
            return new HashMap<>();
        }
        List<String> cardResult = List.of(cardMap.split("--result--"));


        List<String> objects = List.of(cardResult.get(1).split("--card--"));

        List<Card> cardList = new ArrayList<>();
        Map<String, List<Card>> resultMap = new HashMap<>();
        for (String s : objects) {
            List<String> cardData = List.of(s.split("--data--"));

            cardList.add(Card.builder().id(UUID.fromString(cardData.get(0))).color(cardData.get(1)).value(Integer.valueOf(cardData.get(2))).faceVal(Integer.valueOf(cardData.get(3))).description(cardData.get(4)).number(Integer.valueOf(cardData.get(5))).build());
        }
        resultMap.put(cardResult.get(0), cardList);
        return resultMap;
    }


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

            } else if (count < cards.size()) {
                result.append(card.getId().toString()).append("--data--").append(card.getColor()).append("--data--").append(card.getValue()).append("--data--").append(card.getFaceVal()).append("--data--").append(card.getDescription()).append("--data--").append(card.getNumber()).append("--card--");
            }
            count++;
        }

        return result.toString();

    }

}
