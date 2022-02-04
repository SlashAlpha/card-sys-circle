package slash.code.straightanalysis.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import slash.code.straightanalysis.config.JmsConfig;
import slash.code.straightanalysis.model.Card;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StraightServices implements StraightService {

    JmsTemplate jmsTemplate;
    ObjectMapper objectMapper;
    Map<String, List<Card>> cardColors = new HashMap<>();
    RestTemplate restTemplate;

    public StraightServices(JmsTemplate jmsTemplate, ObjectMapper objectMapper, RestTemplateBuilder restTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate.build();
    }

    @Override
    public Map<String, List<Card>> straightFinder(Map<String, List<Card>> cardCheck) {
        //  System.out.println(cards);

        List<Card> cards = cardCheck.entrySet().stream().iterator().next().getValue();
        List<Card> sortedCards =

                cards.stream().sorted(Comparator.comparingInt(Card::getValue).thenComparingInt(Card::getFaceVal).reversed()).collect(Collectors.toList());


        List<Card> straightCards = suiteC(sortedCards, false);


        Map<String, List<Card>> straightMap = new HashMap<>();

        final Map<String, List<Card>> straightF = qFlush((suiteC(sortedCards, true)));

        if (!straightF.isEmpty()) {

            return straightF;
        } else if (!straightCards.isEmpty() && straightCards.size() > 4) {
            straightMap.put("Straight", straightCards);
        }
        // System.out.println(straightMap);
        return straightMap;

    }


    public List<Card> suiteC(List<Card> sortedCards, boolean doublon) {
        //sort the cards in decreasing order
        final List<Card> reverseCards = sortedCards.stream().sorted(Comparator.comparingInt(Card::getValue).thenComparingInt(Card::getFaceVal).reversed()).collect(Collectors.toList());

        List<Card> doublons = new ArrayList<>();
        List<Card> straightCards = new ArrayList<>();
        if (!sortedCards.isEmpty())
            straightCards.add(sortedCards.get(0));
        //collect aces
        final List<Card> aceCards = reverseCards.stream().filter(s -> s.getFaceVal() == 4).collect(Collectors.toList());

        //go through card
        reverseCards.forEach(card -> {
            Card compareCard = straightCards.get(straightCards.size() - 1);
            Integer cardValue2 = compareCard.getValue() + compareCard.getFaceVal();
            Integer cardValue1 = card.getValue() + card.getFaceVal();

            //verify the selected card have a lower value et the size of the list lower == 5 (double are for color sorting)

            if (((cardValue1 + 1) == (cardValue2) || cardValue1.equals(cardValue2)) && straightCards.size() < 6) {

                //Verify that the card is either a double or a lower card

                if (!cardValue1.equals(cardValue2)) {
                    straightCards.add(card);

                    //in case of a straight beginning with an ace, it close the door to the list, checking at 2

                    if (cardValue1 == 2 && straightCards.size() == 4 && !aceCards.isEmpty()) {
                        System.out.println(straightCards);
                        if (doublon && !straightCards.containsAll(aceCards)) {
                            straightCards.addAll(aceCards);
                        }
                        if (!doublon && !straightCards.contains(aceCards.get(0))) {
                            straightCards.add(aceCards.get(0));
                        }
                    }
                    //Where doubles are saved for further analyse

                } else if (!straightCards.contains(card)) {
                    if (!doublons.contains(card)) {
                        doublons.add(card);
                    }
                    ;
                }
                //clear the list in case of a miss in the straight,  restart with a new card as it is sorted

            } else if (((cardValue1 + 1) != (cardValue2) || (cardValue1) == (cardValue2)) && straightCards.size() < 5) {
                straightCards.clear();
                straightCards.add(card);
            }
        });

        //if the double argument is selected, it will add all double & selected cards  to  returning list

        if (doublon && !doublons.isEmpty()) {
            for (Card c : straightCards
            ) {
                if (!doublons.contains(c)) {
                    doublons.add(c);
                }
            }
            ;


            doublons.stream().sorted(Comparator.comparingInt(Card::getValue).thenComparingInt(Card::getFaceVal).reversed()).collect(Collectors.toList());

            return doublons;
        }

        //return the straight
        if (straightCards.size() >= 5) {

            return straightCards.stream().limit(5).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    public Map<String, List<Card>> qFlush(List<Card> straightCards) {
        Map<String, List<Card>> qflush = new HashMap<>();
        qflush.put("test", straightCards);
        List<Card> sortedCards = new ArrayList<>();
        Map<String, List<Card>> colors = new HashMap<>();
        String cardsColorCheck = "";
        if (straightCards.isEmpty()) {
            return new HashMap<>();
        }
        try {
            cardsColorCheck = restTemplate.getForObject("http://localhost:8081/color/check" + mapToCrypt(qflush), String.class);
            if (cardsColorCheck != null) {
                colors = decryptToMap(cardsColorCheck);
            }
        } catch (NoSuchElementException ignored) {
        }
        qflush.remove("test");


        try {
            sortedCards = colors.entrySet().iterator().next().getValue();
        } catch (NoSuchElementException e) {
        }


        if (!sortedCards.isEmpty()) {
            sortedCards = suiteC(sortedCards, false);

        }
        if (sortedCards.size() == 5) {

            qflush.put("Straight Flush", sortedCards.stream().limit(5).collect(Collectors.toList()));

            if (sortedCards.get(1).getFaceVal() == 3) {
                qflush.clear();
                qflush.put("Royal Flush", sortedCards.stream().limit(5).collect(Collectors.toList()));
            }
        }

        return qflush;

    }


    @JmsListener(destination = JmsConfig.ARTEMIS_TO_STRAIGHT)
    public void straightServiceCards(@Payload String cardsString) {


        Map<String, List<Card>> cardMap = decryptToMap(cardsString);

        cardMap = straightFinder(cardMap);

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


}
