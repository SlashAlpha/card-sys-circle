package slash.code.matchanalysis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import slash.code.matchanalysis.controller.MatchController;
import slash.code.matchanalysis.model.Card;
import slash.code.matchanalysis.service.MatchService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MatchAnalysisApplicationTests {

    @Autowired
    MatchService matchService;
    @Autowired
    MatchController controller;
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    void cardMatchPoised() {


        Card card2 = new Card(UUID.randomUUID(), "Spade", 10, 4, "Ace", 2);
        Card card8 = new Card(UUID.randomUUID(), "Spade", 10, 4, "Ace", 2);
        Card card9 = new Card(UUID.randomUUID(), "Spade", 10, 4, "Ace", 2);
        Card card10 = new Card(UUID.randomUUID(), "Spade", 10, 4, "Ace", 2);
        Card card11 = new Card(UUID.randomUUID(), "Spade", 10, 2, "Queen", 2);
        Card card12 = new Card(UUID.randomUUID(), "Spade", 10, 2, "Queen", 2);

        List<Card> cards = new ArrayList<>();
        cards.add(card2);
        cards.add(card8);
        Map<String, List<Card>> cardMap = new HashMap<>();
        cardMap.put("test", cards);

        assertTrue(matchService.matchingCards(cardMap).containsKey("Pair"));
        cardMap.remove("test");
        cards.add(card11);
        cards.add(card12);
        cardMap.put("test", cards);
        assertTrue(matchService.matchingCards(cardMap).containsKey("Two Pair"));
        cardMap.remove("test");
        cards.remove(card11);
        cards.remove(card12);
        cards.add(card9);
        cardMap.put("test", cards);
        assertTrue(matchService.matchingCards(cardMap).containsKey("Three Of A Kind"));
        cardMap.remove("test");
        cards.add(card10);
        cardMap.put("test", cards);
        assertTrue(matchService.matchingCards(cardMap).containsKey("Four Of A Kind"));
        cardMap.remove("test");
        cards.remove(card10);
        cards.add(card11);
        cards.add(card12);
        cardMap.put("test", cards);
        assertTrue(matchService.matchingCards(cardMap).containsKey("Full House"));


    }

    @Test
    void cardMatchRandom() {
        Deck deck = new Deck();
        final String[] combination = {"Pair", "Two Pair", "Three Of A Kind", "Full House", "Four Of A Kind"};

        int one = 0;
        List<String> combs2 = new ArrayList<>();
        combs2.add(combination[0]);
        combs2.add(combination[1]);
        combs2.add(combination[2]);
        combs2.add(combination[3]);
        combs2.add(combination[4]);


        List<Card> cards = new ArrayList<>();
        Map<String, List<Card>> cardMap = new HashMap<>();
        String result = "";
        for (String s : combination
        ) {


            while (!Objects.equals(result, s)) {
                result = "";
                if (deck.cards.isEmpty() || deck.cards.size() < 10) {
                    deck = new Deck();
                }
                cards.clear();
                cards.add(deck.getOneCardFromDeck(deck.cards));
                cards.add(deck.getOneCardFromDeck(deck.cards));
                cards.add(deck.getOneCardFromDeck(deck.cards));
                cards.add(deck.getOneCardFromDeck(deck.cards));
                cards.add(deck.getOneCardFromDeck(deck.cards));
                cards.add(deck.getOneCardFromDeck(deck.cards));
                cards.add(deck.getOneCardFromDeck(deck.cards));
                cardMap.put("test", cards);
                cardMap = matchService.cryptToMap(restTemplate.getForObject("http://localhost:" + port + "/match/check" + matchService.mapToCrypt(cardMap), String.class));

                ;
                List<Card> sortedPair = new ArrayList<>();

                try {
                    result = cardMap.keySet().iterator().next();
                    sortedPair = cardMap.entrySet().iterator().next().getValue();
                    cardMap.clear();
                    cards.clear();
                } catch (NoSuchElementException e) {
                }


                //  System.out.println(result + " cards:" + sortedPair);
                if (!Objects.equals(result, s)) cards.clear();
                cardMap.clear();
            }
            if (combs2.contains(result)) {
                one++;
                combs2.remove(result);

            }
            result = "";
        }

        assertEquals(5, one);
    }


    public class Deck {

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

    private static class CardData {
        static final String[] color = {"Diamond", "Spade", "Heart", "Club"};
        static final Integer[] value = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        static final Integer[] faceValue = {0, 1, 2, 3, 4};
        static final String[] description = {"", "Jack", "Queen", "King"};

        public CardData() {
        }
    }


}
