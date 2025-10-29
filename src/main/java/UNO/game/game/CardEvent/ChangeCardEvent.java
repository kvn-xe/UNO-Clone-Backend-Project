package UNO.game.game.CardEvent;

import java.util.List;

import UNO.game.cards.Card;
import UNO.game.cards.CardContainer;
import UNO.game.cards.DiscardPile;
import UNO.game.game.Game;
import UNO.game.game.Events.GameEvent;

public class ChangeCardEvent extends CardEvent {

  public ChangeCardEvent(Game game, Card card, DiscardPile discard, CardContainer src) {
    super(game, card, discard, src);
  }

  @Override
  public List<GameEvent> play() {
    getCard().setColor(getCard().getColor());
    return super.play();
  }
}
