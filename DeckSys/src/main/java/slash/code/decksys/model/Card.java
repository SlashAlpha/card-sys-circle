package slash.code.decksys.model;

import lombok.*;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class Card {

    UUID id;
    String color;
    Integer value;
    Integer faceVal;
    String description;
    Integer number;

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
