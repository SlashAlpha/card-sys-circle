package slash.code.matchanalysis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import slash.code.matchanalysis.model.Card;
import slash.code.matchanalysis.service.MatchService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/match")
public class MatchController {

    MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/check{cards}")
    public String matchCheck(@PathVariable String cards) {
        Map<String, List<Card>> cardMap = matchService.cryptToMap(cards);
        // System.out.println(cardMap);
        cardMap = matchService.matchingCards(cardMap);
        if (!cardMap.isEmpty()) {

            return matchService.mapToCrypt(cardMap);
        }
        return "";

    }
}
