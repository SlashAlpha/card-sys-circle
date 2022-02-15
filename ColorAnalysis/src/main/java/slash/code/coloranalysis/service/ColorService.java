package slash.code.coloranalysis.service;

import slash.code.coloranalysis.model.Card;

import java.util.List;
import java.util.Map;

public interface ColorService {

    Map<String, List<Card>> color(Map<String, List<Card>> cardMap);

    Map<String, List<Card>> cryptToMap(String cardMap);

    String mapToCrypt(Map<String, List<Card>> cardMap);
}

