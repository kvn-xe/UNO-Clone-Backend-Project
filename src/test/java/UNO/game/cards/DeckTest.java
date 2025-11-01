package UNO.game.cards;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

public class DeckTest {

  private Deck testDeck = new Deck();
  private final static String CARD_REGEX = "\\{ color: ([^}]*), type: ([^}]*), value: ([^}]*) \\}";

  @Test
  public void testStandardDeck() {
    String[] cardStrings = testDeck.string().split("\n");

    int numRed = 0;
    int numBlue = 0;
    int numGreen = 0;
    int numYellow = 0;

    assertEquals(cardStrings[0], "[Deck]:");
    for (int i = 1; i < cardStrings.length - 1; i++) {
      Pattern cardPattern = Pattern.compile(CARD_REGEX);
      Matcher matcher = cardPattern.matcher(cardStrings[i]);
      assertTrue(matcher.matches());

      switch (matcher.group(1)) {
        case "red":
          numRed++;
          break;

        case "yellow":
          numYellow++;
          break;

        case "blue":
          numBlue++;
          break;

        case "green":
          numGreen++;
          break;

        default:
          break;
      }
    }

    assertEquals(cardStrings[cardStrings.length - 1], "[End of Deck]");
    assertTrue((numRed == numYellow && numYellow == numBlue && numBlue == numGreen));
  }

  @Test
  public void testShuffleDiscardToDeck() {
    int initialSize = testDeck.getNumCards();
    DiscardPile discard = new DiscardPile();
    for (int i = 0; i < 10; i++) {
      Card card = testDeck.draw();
      discard.addCard(card);
    }

    discard.shuffleToDeck(testDeck);
    assertEquals(initialSize - 1, testDeck.getNumCards());
    // Discard always keeps 1 card.
  }
}
