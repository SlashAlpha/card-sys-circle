package slash.code.straightanalysis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import slash.code.straightanalysis.model.Card;
import slash.code.straightanalysis.service.StraightService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/straight")
public class StraightController {
    StraightService straightService;

    public StraightController(StraightService straightService) {
        this.straightService = straightService;
    }

    @GetMapping("/check{cards}")
    public String straightCheck(@PathVariable String cards) {
        Map<String, List<Card>> cardMap = straightService.decryptToMap(cards);
        cardMap = straightService.straightFinder(cardMap);
        //  if(cardMap.isEmpty()){return straightService.mapToCrypt(new HashMap<>());}
        return straightService.mapToCrypt(cardMap);

    }
}
