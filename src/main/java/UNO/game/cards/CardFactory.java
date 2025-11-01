package UNO.game.cards;

import java.util.Collections;
import java.util.List;

public class CardFactory {
  public static List<Card> createCards(int numCards, Card card) {
    return Collections.nCopies(numCards, new Card(card));
  }
}
