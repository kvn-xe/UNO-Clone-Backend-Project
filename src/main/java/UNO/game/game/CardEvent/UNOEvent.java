package UNO.game.game.CardEvent;

import java.util.List;

import UNO.game.game.Events.GameEvent;
import UNO.game.user.Player;

public class UNOEvent implements GameEvent {
  private Player player;

  public UNOEvent(Player player) {
    this.player = player;
  }

  synchronized public boolean call(Player caller) {
    if (caller.equals(player)) {
      return false;
    } else {
      player.draw(2);
      return true;
    }
  }

  @Override
  public List<GameEvent> play() {
    return null;
  }

  @Override
  public int getPrio() {
    return 0;
  }
}
