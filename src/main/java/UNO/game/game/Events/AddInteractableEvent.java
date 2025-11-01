package UNO.game.game.Events;

import UNO.game.game.Game;
import UNO.game.user.Player;

public class AddInteractableEvent extends AddEvent {

  public final static int PRIO = 7;

  public AddInteractableEvent(Game game, Player activePlayer, int val) {
    super(game, activePlayer, val);
  }

  @Override
  public int getPrio() {
    return PRIO;
  }
}
