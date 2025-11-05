package UNO.game.helper;

import java.util.List;

import UNO.game.game.Game;
import UNO.game.game.Events.GameEvent;

public class GameTestExtension extends Game {

  public List<GameEvent> playEvent() {
    return getSchedule().playEvent();
  }
}
