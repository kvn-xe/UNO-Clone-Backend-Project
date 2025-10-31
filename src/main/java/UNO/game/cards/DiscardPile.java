package UNO.game.cards;

import java.util.ArrayList;
import java.util.List;

public class DiscardPile implements CardContainer {
  private List<Card> pile = new ArrayList<>();

  public void shuffleToDeck(Deck deck) {
    Card topCard = pile.remove(0);
    deck.addCards(pile);

    pile = new ArrayList<>();
    pile.add(topCard);
  }

  public Card topCard() {
    return pile.get(0);
  }

  @Override
  public Card removeCard(Card card) {
    if (!pile.remove(card)) {
      throw new Error("Card doesn't Exist");
    }
    return card;
  }

  @Override
  public void addCard(Card card) {
    pile.add(card);
  }

  public boolean isValidPlay(Card card) {
    Card top = pile.get(pile.size() - 1);

    if (card.getColor().equals("any")) {
      return true;
    }
    if (card.getType().equals(Card.CHANGE)) {
      return true;
    }
    if (card.getType().equals("number") && card.getValue() == top.getValue()) {
      return true;
    }
    if (card.getColor().equals(top.getColor())) {
      return true;
    }
    return false;
  }

  @Override
  public int getNumCards() {
    return pile.size();
  }
}
