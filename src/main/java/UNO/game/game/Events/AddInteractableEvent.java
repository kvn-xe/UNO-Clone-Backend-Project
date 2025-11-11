package UNO.game.game.Events;

import java.util.List;

import UNO.game.game.Game;
import UNO.game.user.Player;

public class AddInteractableEvent extends AddEvent {

  public final static int PRIO = GameEvent.ADDI_P;

  public AddInteractableEvent(Game game, Player activePlayer, int val) {
    super(game, activePlayer, val);
  }

  @Override
  public List<GameEvent> play() {
    return super.play();
  }

  @Override
  public int getPrio() {
    return PRIO;
  }
}
