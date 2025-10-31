package UNO.game.game.CardEvent;

import java.util.List;

import UNO.game.cards.Card;
import UNO.game.cards.CardContainer;
import UNO.game.cards.DiscardPile;
import UNO.game.game.Game;
import UNO.game.game.Events.GameEvent;

public class ChangeCardEvent extends CardEvent {
  private final static String DEF_COLOR = "blue";

  public ChangeCardEvent(Game game, Card card, DiscardPile discard, CardContainer src) {
    super(game, card, discard, src);
  }

  @Override
  public List<GameEvent> play() {
    if (getCard().getColor().equals("any")) {
      getCard().setColor(DEF_COLOR);
    }
    return super.play();
  }
}
