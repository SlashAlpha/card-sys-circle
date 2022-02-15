package slash.code.decksys;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import slash.code.decksys.controller.DeckController;
import slash.code.decksys.model.Card;
import slash.code.decksys.model.Deck;
import slash.code.decksys.service.DeckService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeckSysApplicationTests {

    private static final String[] color = {"Diamond", "Spade", "Heart", "Club"};
    @Autowired
    DeckController controller;
    @Autowired
    DeckService deckService;
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;

    @Test
    public void testDeckCreation() {
        Deck deck = new Deck();
        final String[] color = {"Diamond", "Spade", "Heart", "Club"};


        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        int count4 = 0;
        for (Card card : deck.getCards()
        ) {
            switch (card.getColor()) {
                case "Diamond":
                    count1++;
                    break;
                case "Spade":
                    count2++;
                    break;
                case "Heart":
                    count3++;
                    break;
                case "Club":
                    count4++;
                    break;
            }
        }


        //   assertEquals(52, deck.getCards().size() );
        assertEquals(count1, count2, count3);
        assertEquals(count3, count4);


    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void deckSendingCardTest() throws Exception {
        controller.initiateDeck();
        List<String> combs2 = new ArrayList<>();
        combs2.add(color[0]);
        combs2.add(color[1]);
        combs2.add(color[2]);
        combs2.add(color[3]);


        assertThat(
                combs2.contains(this.restTemplate.getForObject("http://localhost:" + port + "/deck/oneCard",
                        Card.class).getColor()))
        ;
    }

    @Test
    public void testingCryptToMap() throws Exception {
        controller.initiateDeck();
        Map<String, List<Card>> cardMap = deckService.cryptToMap(this.restTemplate.getForObject("http://localhost:" + port + "/deck/cards7", String.class));
        assertThat(!cardMap.isEmpty());
    }

    @Test
    public void testingMapToCrypt() throws Exception {
        controller.initiateDeck();

        Map<String, List<Card>> cardMap = deckService.cryptToMap(this.restTemplate.getForObject("http://localhost:" + port + "/deck/cards7", String.class));
        assertThat(deckService.mapToCrypt(cardMap).contains(Arrays.stream(color).findAny().get()));

    }


}
