package UNO.game.testHelper;

import java.util.HashMap;

import org.json.JSONObject;

import UNO.game.game.Game;
import UNO.game.turn.Turn;
import UNO.game.user.Player;

public class TurnExtension extends Turn {

  private JSONObject JSONlog = new JSONObject();
  private Game game;

  public TurnExtension(HashMap<String, Player> players) {
    super(players);
  }

  @Override
  public void nextTurn() {
    JSONlog.put(String.valueOf(getTurnNum()), game.getJSON());
    super.nextTurn();
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public JSONObject getLog() {
    return JSONlog;
  }
}
