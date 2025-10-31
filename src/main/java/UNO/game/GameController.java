package UNO.game;

import org.json.JSONObject;

import UNO.game.cards.Card;
import UNO.game.game.Game;
import UNO.game.user.Player;

public class GameController {
  private Game unoGame;

  public GameController(int maxTurns) {
    unoGame = new Game(maxTurns);
  }

  public void playerJoin(JSONObject json) {
    unoGame.addPlayer(new Player(unoGame, json.getString("id")));
  }

  public void playerLeave(JSONObject json) {
    unoGame.removePlayer(json.getString("id"));
  }

  public JSONObject getPlayerState(JSONObject json) {
    return unoGame.getPlayerState(json.getString("id"));
  }

  public JSONObject getGameState(JSONObject json) {
    return unoGame.getGameState();
  }

  public boolean gameStart() {
    unoGame.start();
    return true;
  }

  public void playerDraw(JSONObject json) {
    unoGame.getPlayer(json.getString("id")).actionDraw();
  }

  public void playerPlay(JSONObject json) {
    JSONObject cardJSON = json.getJSONObject("card");
    Player player = unoGame.getPlayer(json.getString("id"));
    player.actionPlay(player.getCard(new Card(
      cardJSON.getString("color"), 
      cardJSON.getString("type"), 
      cardJSON.getInt("value")
    )));
  }
}
