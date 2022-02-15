package slash.code.coloranalysis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import slash.code.coloranalysis.controller.ColorController;
import slash.code.coloranalysis.model.Card;
import slash.code.coloranalysis.service.ColorService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ColorAnalysisApplicationTests {

    @Autowired
    ColorService colorService;

    @Autowired
    ColorController controller;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }


    @Test
    public void testGlobalColor() {
        Deck deck = new Deck();

        String color = "";
        Map<String, List<Card>> cardMap = new HashMap<>();
        List<Card> checkedCards = new ArrayList<>();
        final Card card = deck.getOneCardFromDeck(deck.cards);
        color = card.getColor();
        while (checkedCards.size() < 6) {
            Card colorCard = deck.getOneCardFromDeck(deck.cards);
            if (colorCard.getColor().equals(card.getColor())) {
                checkedCards.add(colorCard);
            }

        }
        String colorCheck = "";
        cardMap.put("test", checkedCards);
        cardMap = colorService.color(cardMap);
        for (String key : cardMap.keySet()
        ) {
            colorCheck = key;

        }
        assertTrue(colorCheck.equals(color) && cardMap.get(color).size() == 5);

    }

    @Test
    public void testGlobalColorRest() {
        Deck deck = new Deck();

        String color = "";
        Map<String, List<Card>> cardMap = new HashMap<>();
        List<Card> checkedCards = new ArrayList<>();
        final Card card = deck.getOneCardFromDeck(deck.cards);
        color = card.getColor();
        while (checkedCards.size() < 6) {
            Card colorCard = deck.getOneCardFromDeck(deck.cards);
            if (colorCard.getColor().equals(card.getColor())) {
                checkedCards.add(colorCard);
            }

        }
        String colorCheck = "";
        cardMap.put("test", checkedCards);
        String colorRest = colorService.mapToCrypt(cardMap);
        cardMap = colorService.cryptToMap(restTemplate.getForObject("http://localhost:" + port + "/color/check" + colorRest, String.class));
        for (String key : cardMap.keySet()
        ) {
            colorCheck = key;

        }
        assertTrue(colorCheck.equals(color) && cardMap.get(color).size() == 5);

    }

    private static class CardData {
        static final String[] color = {"Diamond", "Spade", "Heart", "Club"};
        static final Integer[] value = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        static final Integer[] faceValue = {0, 1, 2, 3, 4};
        static final String[] description = {"", "Jack", "Queen", "King"};

        public CardData() {
        }
    }

    private class Deck {

        List<Card> cards;

        public Deck() {
            this.cards = buildNewGameDeck();
        }

        public List<Card> buildNewGameDeck() {
            this.cards = new ArrayList<>();


            Arrays.stream(CardData.color).forEach(c ->
                    Arrays.stream(CardData.value).forEach(
                            v -> {
                                if (v == 1)
                                    cards.add(new Card(UUID.randomUUID(), c, 10, CardData.faceValue[4], "Ace", cards.size() + 1));
                                if (v > 1 && v < 10)
                                    cards.add(new Card(UUID.randomUUID(), c, v, CardData.faceValue[0], CardData.description[0], cards.size() + 1));
                                Arrays.stream(CardData.faceValue).forEach(f -> {
                                    if (v == 10 && f < 4)
                                        cards.add(new Card(UUID.randomUUID(), c, v, CardData.faceValue[f], CardData.description[f], cards.size() + 1));
                                });
                            }));
            Collections.shuffle(cards);

//            if (cards.size() == 52) {
//                System.out.println("Deck is full and verified : " + cards.size() + " cards");
//            }
            return cards;
        }

        public Card getOneCardFromDeck(List<Card> cards) {
            Card randomCard;

            randomCard = cards.get(0);
            cards.remove(0);


            return randomCard;
        }

    }


}
