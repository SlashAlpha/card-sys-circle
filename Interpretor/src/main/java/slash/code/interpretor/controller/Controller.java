package slash.code.interpretor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import slash.code.interpretor.model.Card;
import slash.code.interpretor.service.InterpretorService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/interpretor")
public class Controller {

    InterpretorService interpretorService;


    public Controller(InterpretorService interpretorService) {
        this.interpretorService = interpretorService;

    }



    @GetMapping("/initiatedeck")
    public void initiateDeck() {


        interpretorService.initiateDeck();
    }

    @GetMapping("/get-cards{number}")
    public ResponseEntity<String> getCards(@PathVariable Integer number) {

        return new ResponseEntity<>(interpretorService.mapToCrypt(interpretorService.getCards(number)), HttpStatus.OK);
    }

    @GetMapping("/straight-analysis/{cards}")
    public ResponseEntity<String> straightAnalysis(@PathVariable String cards) {
        if (cards != null) {
            String result = "{" + interpretorService.straightAnalysis(interpretorService.cryptToMap(cards)) + "}";
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/analysis/{cards}")
    public ResponseEntity<String> analysis(@PathVariable String cards) {
        if (cards != null) {
            Map<String, List<Card>> results = interpretorService.cryptToMap(cards);
            if (!interpretorService.analyseCards(results).isEmpty()) {
                return new ResponseEntity<>(interpretorService.mapToCrypt(interpretorService.analyseCards(results)), HttpStatus.OK);
            } else if (interpretorService.analyseCards(results).isEmpty()) {
                List<Card> bestCards = interpretorService.cryptToMap(cards).get("test");
                bestCards = bestCards.stream().sorted(Comparator.comparingInt(Card::getValue).thenComparingInt(Card::getFaceVal).reversed()).limit(1).collect(Collectors.toList());
                results.clear();
                results.put("best card", bestCards);
                return new ResponseEntity<>(interpretorService.mapToCrypt(results), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
    }

}
