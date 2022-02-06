package slash.code.matchanalysis.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import slash.code.matchanalysis.model.Card;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchServices implements MatchService {

    JmsTemplate jmsTemplate;

    public MatchServices(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public Map<String, List<Card>> matchingCards(Map<String, List<Card>> cardCheck) {
        List<Card> cards = cardCheck.entrySet().stream().iterator().next().getValue();
        List<Card> matchingCards = new ArrayList<>();
        Map<Integer, List<Card>> cardMap = new HashMap<>();
        Map<String, List<Card>> resultMap = new HashMap<>();


        for (Card card : cards
        ) {
            matchingCards = cards.stream().filter(card1 -> card1.getValue() + card1.getFaceVal() == card.getValue() + card.getFaceVal() && !card.equals(card1)).collect(Collectors.toList());
            if (!matchingCards.isEmpty() && !cardMap.containsKey(card.getValue() + card.getFaceVal())) {

                cardMap.put(card.getValue() + card.getFaceVal(), matchingCards);
            } else if (!matchingCards.isEmpty() && cardMap.containsKey(card.getValue() + card.getFaceVal())) {

                for (Card cardM : matchingCards
                ) {
                    if (!cardMap.get(card.getValue() + card.getFaceVal()).contains(cardM)) {
                        cardMap.get(card.getValue() + card.getFaceVal()).add(cardM);

                    }
                }
            }
        }


        //   System.out.println(cardMap);

        return analyseMatchResult(cardMap);
    }

    private Map<String, List<Card>> analyseMatchResult(Map<Integer, List<Card>> cardMap) {
        final String[] combination = {"Pair", "Two Pair", "Three Of A Kind", "Full House", "Four Of A Kind"};
        List<Integer> sortDouble = new ArrayList<>();
        List<Integer> sortThree = new ArrayList<>();
        Integer matchThree = 0;
        Integer matchFour = 0;
        Map<String, List<Card>> resultMap = new HashMap<>();
        if (cardMap.size() < 4) {
            int loopCount = 0;
            for (Map.Entry<Integer, List<Card>> entry :
                    cardMap.entrySet()) {
                Integer key = entry.getKey();
                int size = cardMap.get(entry.getKey()).size();

                if (cardMap.size() == 0) {
                    return resultMap;
                }
                //Map size 1
                if (cardMap.size() == 1) {
                    if (size == 3) {
                        resultMap.put(combination[2], cardMap.get(key));
                    } else if (size == 4) {
                        resultMap.put(combination[4], cardMap.get(key));
                    } else if (size == 2) {
                        resultMap.put(combination[0], cardMap.get(key));
                    }
                }
                //MAP SIZE 2
                if (cardMap.size() == 2) {
                    if (size == 4) {
                        matchFour = key;
                    }
                    if (size == 2) {
                        sortDouble.add(key);
                    }
                    if (size == 3) {
                        sortThree.add(key);
                    }
                    loopCount += 1;
                    if (loopCount == 2) {
                        sortDouble = sortDouble.stream().sorted().collect(Collectors.toList());
                        sortThree = sortThree.stream().sorted().collect(Collectors.toList());

                        if (size == 2 && sortDouble.size() == 2) {
                            resultMap.put(combination[1], cardRep(cardMap, sortDouble.get(0), sortDouble.get(1)));
                        }
                        if (size == 3 && sortThree.size() == 2) {
                            List<Card> doubleCard = cardRep(cardMap, sortThree.get(1), 0);
                            resultMap.put(combination[2], doubleCard);
                        }
                        if (size == 2 && sortThree.size() == 1) {
                            resultMap.put(combination[3], cardRep(cardMap, sortThree.get(0), key));
                        }
                        if (size == 3 && sortDouble.size() == 1) {
                            resultMap.put(combination[3], cardRep(cardMap, sortDouble.get(0), key));
                        }

                        if (matchFour != 0 && cardMap.get(matchFour).size() == 4) {
                            resultMap.put(combination[4], cardMap.get(matchFour));
                        }
                    }
                    //Map size 3
                } else if (cardMap.size() == 3) {
                    loopCount++;
                    if (size == 2) {
                        sortDouble.add(key);
                    }
                    if (size == 3) {
                        matchThree = key;
                    }
                    if (loopCount > 2) {
                        sortDouble = sortDouble.stream().sorted().collect(Collectors.toList());
                        if (size == 2 && matchThree == 0) {
                            resultMap.put(combination[1], cardRep(cardMap, sortDouble.get(1), sortDouble.get(2)));
                        }
                        if (size == 3 && matchThree != 0 && sortDouble.size() == 2) {
                            resultMap.put(combination[3], cardRep(cardMap, matchThree, sortDouble.get(1)));
                        }
                        if (size == 2 && matchThree != 0 && sortDouble.size() == 2) {
                            resultMap.put(combination[3], cardRep(cardMap, matchThree, sortDouble.get(1)));
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    private List<Card> cardRep(Map<Integer, List<Card>> cardMap, Integer key, Integer key2) {
        List<Card> doubleCard = new ArrayList<>();
        if (key != 0) {
            doubleCard = cardMap.get(key);
        }
        if (key2 != 0) {
            doubleCard.addAll(cardMap.get(key2));
        }
        return doubleCard;

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

    @Override
    public String mapToCrypt(Map<String, List<Card>> cardMap) {
        if (cardMap.isEmpty()) {
            return "";
        }
        String key = cardMap.keySet().stream().iterator().next();
        List<Card> cards = cardMap.get(key);
        String result = key + "--result--";
        int count = 1;
        for (Card card :
                cards
        ) {
            if (count == cards.size()) {


                result = result + card.getId().toString() + "--data--" + card.getColor() + "--data--" + card.getValue() + "--data--" + card.getFaceVal() + "--data--" + card.getDescription() + "--data--" + card.getNumber();

            } else if (count < cards.size()) {
                result = result + card.getId().toString() + "--data--" + card.getColor() + "--data--" + card.getValue() + "--data--" + card.getFaceVal() + "--data--" + card.getDescription() + "--data--" + card.getNumber() + "--card--";
            }
            count++;
        }

        return result;

    }

//    @JmsListener(destination = JmsConfig.ARTEMIS_TO_MATCH)
//    public void matchArtemis(@PathVariable String cardsMatch) {
//        Map<String, List<Card>> cardMap = decryptToMap(cardsMatch);
//        List<Card> cards = cardMap.get("test");
//        cardMap = matchingCards(cardMap);
//        System.out.println(cardMap);
//    }
}
