package slash.code.coloranalysis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import slash.code.coloranalysis.model.Card;
import slash.code.coloranalysis.service.ColorService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/color")
public class ColorController {

    ColorService colorService;

    public ColorController(ColorService colorService) {
        this.colorService = colorService;
    }

    @GetMapping("/check{cards}")
    public String verifyColor(@PathVariable String cards) {

        Map<String, List<Card>> cardMap = colorService.decryptToMap(cards);
        cardMap = colorService.color(cardMap);

        return colorService.mapToCrypt(cardMap);

    }
}
