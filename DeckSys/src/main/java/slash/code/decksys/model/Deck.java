package slash.code.decksys.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor
@Getter
@Setter
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

        if (cards.size() == 52) {
            System.out.println("Deck is full and verified : " + cards.size() + " cards");
        }
        return cards;
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

