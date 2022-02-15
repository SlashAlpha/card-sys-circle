package slash.code.interpretor.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import slash.code.interpretor.model.Card;

import java.util.*;


@Service
public class InterpretorServices implements InterpretorService {

    private static final List<String> combination = List.of(new String[]{"High Card", "Pair", "Two Pair", "Three Of A Kind", "Straight", "Flush", "Full House", "Four Of A Kind", "Straight Flush", "Royal Flush"});
    private static final List<String> color = List.of(new String[]{"Diamond", "Spade", "Heart", "Club"});

    RestTemplate restTemplate;

/*
    @Value("${deck.host}")
    public String deckHost;

    @Value("${match.host}")
    private  String matchHost;

    @Value("${color.host}")
    private  String colorHost;

    @Value("${straight.host}")
    private  String straightHost;
*/

    private static final String DECK_ROUTE = "http://" + "deck" + ":8082";
    private static final String COLOR_ROUTE = "http://" + "color" + ":8081";
    private static final String MATCH_ROUTE = "http://" + "match" + ":8083";
    private static final String STRAIGHT_ROUTE = "http://" + "straight" + ":8084";

    public InterpretorServices(RestTemplateBuilder restTemplate) {

        this.restTemplate = restTemplate.build();
    }

    @Override
    public void initiateDeck() {
        restTemplate.getForObject(DECK_ROUTE + "/deck/initiate", String.class);
    }

    @Override
    public Card getOneCard() {
        return null;
    }

    @Override
    public Map<String, List<Card>> analyseCards(Map<String, List<Card>> cardMap) {

        List<Map<String, List<Card>>> results = new ArrayList<>();
        int resultMaps = 0;
        Map<String, List<Card>> matchCards = cryptToMap(restTemplate.getForObject(MATCH_ROUTE + "/match/check" + mapToCrypt(cardMap), String.class));
        Map<String, List<Card>> colorCards = cryptToMap(restTemplate.getForObject(COLOR_ROUTE + "/color/check" + mapToCrypt(cardMap), String.class));
        Map<String, List<Card>> straightCards = cryptToMap(restTemplate.getForObject(STRAIGHT_ROUTE + "/straight/check" + mapToCrypt(cardMap), String.class));


        if (!matchCards.isEmpty()) {
            String matchResult = matchCards.keySet().stream().iterator().next();
            if (combination.contains(matchResult)) {
                resultMaps = combination.indexOf(matchResult);
                results.add(matchCards);
            }
        }
        if (!colorCards.isEmpty()) {
            String coloResult = colorCards.keySet().stream().iterator().next();
            if (color.contains(coloResult) && resultMaps < combination.indexOf("Flush")) {
                resultMaps = combination.indexOf("Flush");
                if (!results.isEmpty()) {
                    results.clear();
                    resultMaps = combination.indexOf("Flush");

                    results.add(colorCards);
                } else {
                    results.add(colorCards);
                }

            }
        }
        if (!straightCards.isEmpty()) {
            String straightResult = straightCards.keySet().stream().iterator().next();
            if (combination.contains(straightResult) && resultMaps < combination.indexOf(straightResult)) {


                if (!results.isEmpty()) {
                    results.clear();
                    results.add(straightCards);
                } else {
                    results.add(straightCards);
                }
            }

        }
        if (!results.isEmpty()) {
            return results.get(0);
        }


        return new HashMap<>();
    }


    @Override
    public String straightAnalysis(Map<String, List<Card>> cardMap) {
        Map<String, List<Card>> straightCards = cryptToMap(restTemplate.getForObject("http://localhost:8084/straight/check" + mapToCrypt(cardMap), String.class));
        return mapToCrypt(straightCards);

    }

    @Override
    public Map<String, List<Card>> getCards(Integer number) {

        String cards = restTemplate.getForObject(DECK_ROUTE + "/deck/cards" + number, String.class);
        return cryptToMap(cards);
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
