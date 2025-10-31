package UNO.game.cards;

public interface CardContainer {
  public static void moveCards(Card card, CardContainer src, CardContainer dest) {
    Card cardToMove = src.removeCard(card);
    dest.addCard(cardToMove);
  }

  public int getNumCards();

  public Card removeCard(Card card);

  public void addCard(Card card);
}
