package UNO.game.game.Events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import UNO.game.cards.Card;
import UNO.game.game.EventScheduler;
import UNO.game.game.Game;
import UNO.game.game.CardEvent.AddCardEvent;
import UNO.game.game.CardEvent.CardEvent;
import UNO.game.game.CardEvent.ChangeCardEvent;
import UNO.game.game.CardEvent.ReverseCardEvent;
import UNO.game.game.CardEvent.SkipCardEvent;
import UNO.game.game.CardEvent.UNOEvent;
import UNO.game.user.Player;
import UNO.game.user.PlayerAction;

public class EventFactory {
  private Game game;

  public EventFactory(Game game) {
    this.game = game;
  }

  public GameEvent createEvent(Card card) {
    GameEvent event = null;

    switch (card.getType()) {
      case Card.ADD:
        event = new AddCardEvent(game, card, game.getDiscard(), game.getActivePlayer());
        break;
      case Card.CHANGE:
        event = new ChangeCardEvent(game, card, game.getDiscard(), game.getActivePlayer());
        break;
      case Card.REVERSE:
        event = new ReverseCardEvent(game, card, game.getDiscard(), game.getActivePlayer());
        break;
      case Card.SKIP:
        event = new SkipCardEvent(game, card, game.getDiscard(), game.getActivePlayer());
        break;
      case Card.NUMBER:
        event = new CardEvent(game, card, game.getDiscard(), game.getActivePlayer());
        break;
      default:
        break;
    }

    return event;
  }

  public GameEvent createEvent(String eventType) {
    GameEvent event = null;

    switch (eventType) {
      case GameEvent.ADD:
        event = new AddEvent(game, null, 2);
        break;
      case GameEvent.END:
        event = new EndEvent(game);
        break;
      case GameEvent.FORCED_END:
        event = new EndEvent(game, GameEvent.FORCED_END);
        break;
      case GameEvent.UNO:
        event = new UNOEvent(game.getActivePlayer());
        break;
    }
    return event;
  }

  public GameEvent createEvent(PlayerAction action) {
    GameEvent res = null;
    
    switch (action.getActionType()) {
      case Player.PLAYER_DRAW:
        res = new AddEvent(game, game.getActivePlayer(), 1);
      case Player.PLAYER_PLAY:
        res = createEvent(action.getCard());
      default:
        break;
    }
    return res;
  }

  public GameEvent createEvent(String eventType, int numArg) {
    GameEvent event = null;
    if (eventType.equals(GameEvent.ADD)) {
      event = new AddEvent(game, game.getActivePlayer(), numArg);
    } else if (eventType.equals(GameEvent.ADD_I)) {
      event = new AddInteractableEvent(game, game.getActivePlayer(), numArg);
    }
    return event;
  }

  public List<GameEvent> createEvents(Collection<String> eventTypes) {
    List<GameEvent> events = new ArrayList<>();
    for (String eventType : eventTypes) {
      events.add(createEvent(eventType));
    }
    return events;
  }

  public static void createUniversalEvent(String eventType, EventScheduler scheduler) {
    switch (eventType) {
      case GameEvent.UNO:
        scheduler.addCurrentEvent(new UNOEvent(null));
        break;
      default:
        break;
    }
  }
}
