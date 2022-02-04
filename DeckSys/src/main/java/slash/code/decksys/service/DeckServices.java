package slash.code.decksys.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import slash.code.decksys.model.Card;
import slash.code.decksys.model.Deck;

import java.util.*;

@Service
public class DeckServices implements DeckService {

    private static final List<String> combination = List.of(new String[]{"High Card", "Pair", "Two Pair", "Three Of A Kind", "Straight", "Flush", "Full house", "Four Of A Kind", "Straight Flush", "Royal Flush"});
    Deck deck = new Deck();
    JmsTemplate jmsTemplate;


    public DeckServices(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public List<Card> buildNewGameDeck(List<Card> cards) {
        deck.setCards(new ArrayList<>());
        CardData baseCards = new CardData();

        Arrays.stream(baseCards.color).forEach(c ->

                Arrays.stream(baseCards.value).forEach(
                        v -> {

                            if (v == 1)
                                deck.getCards().add(new Card(UUID.randomUUID(), c, 10, baseCards.faceValue[4], "Ace", cards.size() + 1));
                            if (v > 1 && v < 10)
                                deck.getCards().add(new Card(UUID.randomUUID(), c, v, baseCards.faceValue[0], baseCards.description[0], cards.size() + 1));
                            Arrays.stream(baseCards.faceValue).forEach(f -> {
                                if (v == 10 && f < 4)
                                    deck.getCards().add(new Card(UUID.randomUUID(), c, v, baseCards.faceValue[f], baseCards.description[f], cards.size() + 1));
                            });
                        }));
        Collections.shuffle(cards);

        if (cards.size() == 52) {
            System.out.println("Deck is full and verified : " + cards.size() + " cards");
        }
        return cards;
    }


    @Override
    public Card getOneCardFromDeck(List<Card> cards) {
        Card randomCard = new Card();

        randomCard = cards.get(0);
        cards.remove(0);


        return randomCard;
    }

    @Override
    public List<Card> getCardsFromDeck(List<Card> cards, Integer number) {
        List<Card> returnCards = new ArrayList<>();

        while (returnCards.size() < number && number < 53) {
            Card randomCard = new Card();
            returnCards.add(cards.get(0));
            cards.remove(0);
        }
        return returnCards;
    }

    ;

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

            } else {
                result = result + card.getId().toString() + "--data--" + card.getColor() + "--data--" + card.getValue() + "--data--" + card.getFaceVal() + "--data--" + card.getDescription() + "--data--" + card.getNumber() + "--card--";
            }
            count++;
        }


        return result;
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

    private class CardData {
        final String[] color = {"Diamond", "Spade", "Heart", "Club"};
        final Integer[] value = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        final Integer[] faceValue = {0, 1, 2, 3, 4};
        final String[] description = {"", "Jack", "Queen", "King"};

        public CardData() {
        }
    }

}
