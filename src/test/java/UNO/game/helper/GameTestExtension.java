package UNO.game.helper;

import java.util.List;

import UNO.game.game.Game;
import UNO.game.game.Events.GameEvent;

public class GameTestExtension extends Game {
  
  public void playEvents() {
    for (GameEvent e : getEventStack()) {
      e.play();
    }
  }

  public List<GameEvent> playEvent() {
    GameEvent e = getEventStack().poll();
    return e.play();
  }
}
