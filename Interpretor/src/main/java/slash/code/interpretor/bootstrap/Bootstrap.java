package slash.code.interpretor.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import slash.code.interpretor.model.Card;
import slash.code.interpretor.service.InterpretorService;

import java.util.*;

@Component
public class Bootstrap implements CommandLineRunner {
    RestTemplate restTemplate;
    InterpretorService interpretorService;

    public Bootstrap(RestTemplateBuilder restTemplate, InterpretorService interpretorService) {
        this.restTemplate = restTemplate.build();
        this.interpretorService = interpretorService;
    }

    @Override
    public void run(String... args) throws Exception {
        //        Map<String, List<Card>> cardMap = new HashMap<>();
        List<Card> cardList = new ArrayList<>();
        Card card2 = new Card(UUID.randomUUID(), "Heart", 9, 0, "", 2);
        cardList.add(card2);
        Card card8 = new Card(UUID.randomUUID(), "Heart", 10, 1, "Jack", 2);
        cardList.add(card8);
        Card card9 = new Card(UUID.randomUUID(), "Heart", 10, 1, "Jack", 2);
        cardList.add(card9);
        Card card10 = new Card(UUID.randomUUID(), "Heart", 10, 2, "Queen", 2);
        cardList.add(card10);
        Card card11 = new Card(UUID.randomUUID(), "Heart", 10, 2, "Queen", 2);
        cardList.add(card11);
        Card card12 = new Card(UUID.randomUUID(), "Heart", 10, 2, "Queen", 2);
        cardList.add(card12);


        //   interpretorService.initiateDeck();
        Map<String, List<Card>> cardMap = new HashMap<>();
        cardMap.put("test", cardList);
        //interpretorService.getCards(7);
        System.out.println(cardMap);
        System.out.println(interpretorService.analyseCards(cardMap));
    }
}
