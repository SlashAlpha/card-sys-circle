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
    //  S3Service s3Service;

    public Controller(InterpretorService interpretorService) {
        this.interpretorService = interpretorService;

    }

    //    @GetMapping("/card/image/{keyname}")
//    public ResponseEntity downloadFile(@PathVariable String keyname) {
//        ByteArrayOutputStream downloadInputStream = s3Service.downloadFile(keyname);
//
//        return ResponseEntity.ok()
//                .contentType(contentType(keyname))
//                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + keyname + "\"")
//                .body(downloadInputStream.toByteArray());
//    }
//    private MediaType contentType(String keyname) {
//        String[] arr = keyname.split("\\.");
//        String type = arr[arr.length - 1];
//        switch (type) {
//            case "txt":
//                return MediaType.TEXT_PLAIN;
//            case "png":
//                return MediaType.IMAGE_PNG;
//            case "jpg":
//                return MediaType.IMAGE_JPEG;
//            default:
//                return MediaType.APPLICATION_OCTET_STREAM;
//        }
//    }
//    @GetMapping("/list/file/all")
//    public List listAllFiles(){
//        return s3Service.listFiles();
//    }

    @GetMapping("/initiatedeck")
    public void initiateDeck() {

        // System.out.println("deck initiated");
        interpretorService.initiateDeck();
    }

    @GetMapping("/get-cards{number}")
    public ResponseEntity<String> getCards(@PathVariable Integer number) {
        //  interpretorService.initiateDeck();
        //  System.out.println("card request received");
        return new ResponseEntity<String>(interpretorService.mapToCrypt(interpretorService.getCards(number)).toString(), HttpStatus.OK);
    }

    @GetMapping("/straight-analysis/{cards}")
    public ResponseEntity<String> straightAnalysis(@PathVariable String cards) {
        if (cards != null) {
            String result = "{" + interpretorService.straightAnalysis(interpretorService.cryptToMap(cards)) + "}";
            return new ResponseEntity<String>(result, HttpStatus.OK);
        }
        return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/analysis/{cards}")
    public ResponseEntity<String> analysis(@PathVariable String cards) {
        if (cards != null) {
            Map<String, List<Card>> results = interpretorService.cryptToMap(cards);
            if (!interpretorService.analyseCards(results).isEmpty()) {
                return new ResponseEntity<String>(interpretorService.mapToCrypt(interpretorService.analyseCards(results)), HttpStatus.OK);
            } else if (interpretorService.analyseCards(results).isEmpty()) {
                List<Card> bestCards = interpretorService.cryptToMap(cards).get("test");
                bestCards = bestCards.stream().sorted(Comparator.comparingInt(Card::getValue).thenComparingInt(Card::getFaceVal).reversed()).limit(1).collect(Collectors.toList());
                results.clear();
                results.put("best card", bestCards);
                return new ResponseEntity<String>(interpretorService.mapToCrypt(results), HttpStatus.OK);
            }
        }
        return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
    }

}
