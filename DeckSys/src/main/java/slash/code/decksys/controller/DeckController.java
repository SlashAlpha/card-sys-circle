package slash.code.decksys.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import slash.code.decksys.model.Card;
import slash.code.decksys.model.Deck;
import slash.code.decksys.service.DeckService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deck")
public class DeckController {

    private Deck deck;
    DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @GetMapping("/initiate")
    public Deck initiateDeck() {
        return this.deck = new Deck();
    }

    @GetMapping("/oneCard")
    public Card getOneCard() {
        return deckService.getOneCardFromDeck(this.deck.getCards());
    }

    @GetMapping("/cards{number}")
    public String getCards(@PathVariable Integer number) {
        if (deck.getCards().size() < 8) {
            renewDeck();
        }
        List<Card> cards = deckService.getCardsFromDeck(this.deck.getCards(), number);
        Map<String, List<Card>> cardMap = new HashMap<>();
        cardMap.put("test", cards);
        return deckService.mapToCrypt(cardMap);
    }


    private Deck renewDeck() {
        return this.deck = new Deck();
    }


}
