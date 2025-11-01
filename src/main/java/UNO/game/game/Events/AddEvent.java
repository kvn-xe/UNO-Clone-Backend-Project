package UNO.game.game.Events;

import java.util.ArrayList;
import java.util.List;

import UNO.game.game.Game;
import UNO.game.user.Player;

public class AddEvent implements GameEvent {

  private final static int PRIO = 1;

  private Game game = null;
  private Player activePlayer = null;
  private int val = 0;

  public AddEvent(Game game, Player activePlayer, int val) {
    this.activePlayer = activePlayer;
    this.val = val;
    this.game = game;
  }

  @Override
  public List<GameEvent> play() {
    activePlayer.draw(val);
    
    List<GameEvent> res = new ArrayList<>();
    res.add(game.getEventFactory().createEvent(GameEvent.FORCED_END));
    return res;
  }

  @Override
  public int getPrio() {
    return PRIO;
  }

  public int getVal() {
    return val;
  }

  public Player getActivePlayer() {
    return activePlayer;
  }

  public Game getGame() {
    return game;
  }
}
