package slash.code.straightanalysis.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Card {

    UUID id;
    String color;
    Integer value;
    Integer faceVal;
    String description;
    Integer number;

    public Card(UUID id, String color, Integer value, Integer faceVal, String description, Integer number) {
        this.id = id;
        this.color = color;
        this.value = value;
        this.faceVal = faceVal;
        this.description = description;
        this.number = number;
    }

    @Override
    public String toString() {
        if (description != "") {
            return description + " of " + color + " card number : " + number;
        } else {
            return value + " of " + color + " card number : " + number;
        }
    }

    public String stringify() {
        String card = this.value + "/" + this.color + "/" + this.description + "/" + this.faceVal;
        return card;
    }
}
