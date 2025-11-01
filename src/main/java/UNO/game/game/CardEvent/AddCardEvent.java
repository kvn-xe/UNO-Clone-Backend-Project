package UNO.game.game.CardEvent;

import java.util.List;

import UNO.game.cards.Card;
import UNO.game.cards.CardContainer;
import UNO.game.cards.DiscardPile;
import UNO.game.game.Game;
import UNO.game.game.Events.AddInteractableEvent;
import UNO.game.game.Events.GameEvent;

public class AddCardEvent extends CardEvent {

  public AddCardEvent(Game game, Card card, DiscardPile discard, CardContainer src) {
    super(game, card, discard, src);
  }

  @Override
  public List<GameEvent> play() {
    List<GameEvent> res = super.play();
    res.add(getGame().getEventFactory().createEvent(GameEvent.ADD_I, getCard().getValue()));

    List<GameEvent> pendingAddEvents = getGame().getEventStack().stream().filter((e) -> e instanceof AddInteractableEvent).toList();
    for (GameEvent event : pendingAddEvents) {
      getGame().remCurrentEvent(event);
      getGame().addNextEvent(event);
    }
    return res;
  }
}
