package UNO.game.game.Events;

import java.util.List;

import UNO.game.game.Game;

public class EndEvent implements GameEvent {
  private final static int PRIO = GameEvent.END_P;
  private int prio = 0;

  public EndEvent(Game game) {
    prio = PRIO;
  }

  public EndEvent(Game game, String type) {
    if (type.equals(GameEvent.FORCED_END)) {
      prio = GameEvent.ENDF_P;
    } else {
      prio = PRIO;
    }
  }

  @Override
  public List<GameEvent> play() {
    return null;
  }

  @Override
  public int getPrio() {
    return prio;
  }

}
