package slash.code.decksys.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import slash.code.decksys.config.JmsConfig;
import slash.code.decksys.model.Card;
import slash.code.decksys.model.Deck;
import slash.code.decksys.service.DeckService;

import java.util.*;

@Component
public class Bootstrap implements CommandLineRunner {

    JmsTemplate jmsTemplate;
    DeckService deckService;
    RestTemplate restTemplate;

    public Bootstrap(JmsTemplate jmsTemplate, DeckService deckService, RestTemplateBuilder restTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.deckService = deckService;
        this.restTemplate = restTemplate.build();
    }

    @Override
    public void run(String... args) throws Exception {
        Deck deck = new Deck();

        Map<String, List<Card>> cardMap = new HashMap<>();
        List<Card> cardList = new ArrayList<>();
        Card card2 = new Card(UUID.randomUUID(), "Spade", 9, 0, "", 2);
        cardList.add(card2);
        Card card8 = new Card(UUID.randomUUID(), "Heart", 5, 0, "", 2);
        cardList.add(card8);
        Card card9 = new Card(UUID.randomUUID(), "Heart", 10, 2, "Queen", 2);
        cardList.add(card9);
        Card card10 = new Card(UUID.randomUUID(), "Heart", 8, 0, "", 2);
        cardList.add(card10);
        Card card11 = new Card(UUID.randomUUID(), "Heart", 10, 4, "Ace", 2);
        cardList.add(card11);
        Card card12 = new Card(UUID.randomUUID(), "Heart", 2, 0, "", 2);
        cardList.add(card12);


//        cardList.add(deckService.getOneCardFromDeck(deck.getCards()));
//        cardList.add(deckService.getOneCardFromDeck(deck.getCards()));
//        cardList.add(deckService.getOneCardFromDeck(deck.getCards()));
//        cardList.add(deckService.getOneCardFromDeck(deck.getCards()));
//        cardList.add(deckService.getOneCardFromDeck(deck.getCards()));
//        cardList.add(deckService.getOneCardFromDeck(deck.getCards()));
//        cardList.add(deckService.getOneCardFromDeck(deck.getCards()));


        cardMap.put("test", cardList);

        jmsTemplate.convertAndSend(JmsConfig.ARTEMIS_TO_MATCH, deckService.mapToCrypt(cardMap));
        System.out.println(deckService.cryptToMap(restTemplate.getForObject("http://localhost:8083/match/check" + deckService.mapToCrypt(cardMap), String.class)));
        System.out.println(deckService.cryptToMap(restTemplate.getForObject("http://localhost:8081/color/check" + deckService.mapToCrypt(cardMap), String.class)));
        System.out.println(deckService.cryptToMap(restTemplate.getForObject("http://localhost:8084/straight/check" + deckService.mapToCrypt(cardMap), String.class)));
        //  System.out.println(deckService.mapToCrypt(cardMap));
        //   System.out.println(deckService.cryptToMap(deckService.mapToCrypt(cardMap)));

    }
}
