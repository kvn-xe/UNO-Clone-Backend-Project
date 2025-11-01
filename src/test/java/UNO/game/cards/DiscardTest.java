package UNO.game.cards;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class DiscardTest {

  @Test
  public void testCorrectCardIsPlayable() {
    DiscardPile pile = new DiscardPile();
    pile.addCard(new Card("blue", "number", 2));

    List<Card> cards = new ArrayList<>();
    cards.add(new Card("green", 2));
    cards.add(new Card("blue", 3));

    for (Card card : cards) {
      assertTrue(pile.isValidPlay(card));
    }
  }

  @Test
  public void testIncorrectCardIsPlayable() {
    DiscardPile pile = new DiscardPile();
    pile.addCard(new Card("blue", "number", 2));

    List<Card> cards = new ArrayList<>();
    cards.add(new Card("green", 3));
    cards.add(new Card("red", 3));

    for (Card card : cards) {
      assertTrue(!pile.isValidPlay(card));
    }
  }
}
