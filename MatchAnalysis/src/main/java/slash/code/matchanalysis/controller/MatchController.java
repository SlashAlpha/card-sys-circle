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
        Map<String, List<Card>> cardMap = matchService.decryptToMap(cards);
        cardMap = matchService.matchingCards(cardMap);
        //  if(cardMap.isEmpty()){return "";}
        return matchService.mapToCrypt(cardMap);

    }
}
