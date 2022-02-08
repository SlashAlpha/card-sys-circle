import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'card-sys-front';
  cards: Card[] = [];
  cardsString = "";
  cardsResults: Card[] = [];
  result = "";
  timing = "";
  private BASEURL = "http://localhost:8085/interpretor/";
  private cardMap = new Map<string, Array<Card>>();

  constructor(private http: HttpClient) {

  }

  ngOnInit(): void {
    this.http.get(this.BASEURL + "initiatedeck").subscribe();


  }


  analyse() {
    var t0 = performance.now();
    this.http.get(this.BASEURL + "analysis/" + this.cardsString, {responseType: "text"}).subscribe({
        next: (res: string) => {

          this.cardsResults = this.cryptToMap(res).values().next().value;
          this.result = this.cryptToMap(res).keys().next().value;
          var t1 = performance.now();
          this.timing = "Call to Analyse cards :" + (t1 - t0) + " milliseconds.";
        },
        error: err => {
        }

      }
    );
  }


  getMyImage(number: number): string {
    return "../assets/cards/" + number + ".png";

  }


  getCards(number: number): Map<string, Array<Card>> {
    this.http.get(this.BASEURL + "initiatedeck").subscribe();
    let result = "";


    // @ts-ignore
    this.http.get<string>(this.BASEURL + "get-cards" + number, {responseType: "text"}).subscribe({
      next: (res: string) => {
        result = res;

        this.cardMap = this.cryptToMap(result);
        this.cards = this.cardMap.values().next().value;
        this.cardsString = res;
      },
      error: (err) => {

        console.log(err);

      }
    });

    return this.cardMap;
  }

  cryptToMap(cards: string): Map<string, Array<Card>> {

    let cardMap = new Map<string, Array<Card>>();
    let cardList = Array<Card>();
    if (cards != null) {
      let result = cards.split("--result--")[0];
      if (cards.split("--result--").length > 0) {
        let cardSys = cards.split("--result--")[1];
        let newCards = cardSys.split("--card--");
        newCards.forEach(c => {
          let data = c.split("--data--");
          let card = new Card(data[0], data[1], Number(data[2]), Number(data[3]), data[4], Number(data[5]), null);
          cardList.push(card);
        });
      }


      cardMap.set(result, cardList);

      return cardMap;
    }
    this.result = '';
    this.cardsResults = [];
    return new Map;
  }

  mapToCrypt(cardsMap: Map<string, Array<Card>>): String {
    let resultString: string;
    if (cardsMap.size == 1) {
      resultString = cardsMap.keys().next().value + "--result--";
      let cardList = Array<Card>();
      if (resultString != null) {
        cardList = Array.from(cardsMap.values().next().value);
        let count = 1;
        cardList.forEach(cards => {
          if (count == cardList.length) {
            resultString = resultString + cards.id
              + "--data--" + cards.color
              + "--data--" + cards.value
              + "--data--" + cards.faceVal
              + "--data--" + cards.description
              + "--data--" + cards.number;
          } else {
            resultString = resultString + cards.id
              + "--data--" + cards.color
              + "--data--" + cards.value
              + "--data--" + cards.faceVal
              + "--data--" + cards.description
              + "--data--" + cards.number
              + "--card--";
          }


          count++;
        });
      }
      alert(resultString);
      return resultString;
    }


    return "";

  }


}

class Card {
  constructor(id: string, color: string, value: number, faceVal: number, description: string, number: number, file: any) {
    this._id = id;
    this._color = color;
    this._value = value;
    this._faceVal = faceVal;
    this._description = description;
    this._number = number;
    this._file = file;
  }

  private _id: string;

  get id(): string {
    return this._id;
  }

  private _color: string;

  get color(): string {
    return this._color;
  }

  private _value: number;

  get value(): number {
    return this._value;
  }

  private _faceVal: number;

  get faceVal(): number {
    return this._faceVal;
  }

  private _description: string;

  get description(): string {
    return this._description;
  }

  private _number: number;

  get number(): number {
    return this._number;
  }

  private _file: File;

  get file(): File {
    return this._file;
  }
}
