package UNO.game.cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Deck implements CardContainer {

  private final int STANDARD_MULTI = 2;
  private final int
   SPECIAL_MULTI = 4;

  private List<Card> cards = new ArrayList<>();

  public Deck() {
    initDefault();
  }

  private void initDefault() {
    for (String color : Card.COLORS) {
      for (int i = 0; i < 10; i++) {
        cards.addAll(CardFactory.createCards(STANDARD_MULTI, new Card(color, i)));
      }

      for (String type : Card.TYPES) {
        if (type.equals("number")) {
          continue;
        }

        cards.addAll(CardFactory.createCards(SPECIAL_MULTI, new Card(color, type)));
      }
    }

    for (String type : Card.SPECIAL) {
      cards.addAll(CardFactory.createCards(SPECIAL_MULTI * 2, new Card(type)));
    }
  }

  public Card draw() {
    if (cards.size() == 0) {
      throw new Error("Deck Empty");
    }
    
    int randNum = (int) (Math.random() * (cards.size()));
    Card randCard = cards.get(randNum);
    cards.remove(randNum);
    return randCard;
  }

  public String string() {
    String res = "[Deck]:\n";
    for (Card card : cards) {
      res += card.string() + "\n";
    }
    return res += "[End of Deck]";
  }

  public void addCards(Collection<Card> newCards) {
    cards.addAll(newCards);
  }

  @Override
  public Card removeCard(Card card) {
    if (!cards.remove(card)) {
      throw new Error("Card doesn't Exist");
    }
    return card;
  }

  @Override
  public void addCard(Card card) {
    cards.add(card);
  }

  public int getNumCards() {
    return cards.size();
  }
}
