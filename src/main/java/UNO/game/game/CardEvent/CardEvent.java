package UNO.game.game.CardEvent;

import java.util.ArrayList;
import java.util.List;

import UNO.game.cards.Card;
import UNO.game.cards.CardContainer;
import UNO.game.cards.DiscardPile;
import UNO.game.game.Game;
import UNO.game.game.Events.GameEvent;
import UNO.game.user.Player;

public class CardEvent implements GameEvent {

  private final static int PRIO = 5;

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
    if (src instanceof Player) {
      game.getUniversalEventStack().add(game.getEventFactory().createEvent(GameEvent.UNO));
    }
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
