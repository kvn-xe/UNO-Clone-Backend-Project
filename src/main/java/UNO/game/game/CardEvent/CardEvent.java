package UNO.game.game.CardEvent;

import java.util.ArrayList;
import java.util.List;

import UNO.game.cards.Card;
import UNO.game.cards.CardContainer;
import UNO.game.cards.DiscardPile;
import UNO.game.game.Game;
import UNO.game.game.Events.EndEvent;
import UNO.game.game.Events.GameEvent;
import UNO.game.user.Player;

public class CardEvent implements GameEvent {

  private final static int PRIO = GameEvent.CARD_P;

  private Game game = null;
  private Card card = null;
  private DiscardPile discard = null;
  private CardContainer src = null;

  public CardEvent(Game game, Card card, DiscardPile discard, CardContainer src) {
    this.game = game;
    this.card = card;
    this.discard = discard;
    this.src = src;
  }

  @Override
  public List<GameEvent> play() {
    card.discard();
    CardContainer.moveCards(card, src, discard);

    List<GameEvent> res = new ArrayList<>();
    if (src instanceof Player && src.getNumCards() == 1) {
      game.addUniversalEvent(game.getEventFactory().createEvent(GameEvent.UNO));
    }
    if (src instanceof Player && src.getNumCards() == 0) {
      game.endGame();
    }

    res.add(new EndEvent(game));
    return res;
  }

  @Override
  public int getPrio() {
    return PRIO;
  }

  public Game getGame() {
    return game;
  }

  public Card getCard() {
    return card;
  }
}
