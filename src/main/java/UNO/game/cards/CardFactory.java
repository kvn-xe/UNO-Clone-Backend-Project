package UNO.game.cards;

import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

import UNO.game.GameController;

public class CardFactory {
  public static List<Card> createCards(int numCards, Card card) {
    return Collections.nCopies(numCards, new Card(card));
  }

  public static Card createCard(JSONObject cardJSON) {
    return new Card(
      cardJSON.getString(GameController.CARD_COLOR_KEY), 
      cardJSON.getString(GameController.CARD_TYPE_KEY), 
      cardJSON.getInt(GameController.CARD_VAL_KEY)
    );
  }
}
