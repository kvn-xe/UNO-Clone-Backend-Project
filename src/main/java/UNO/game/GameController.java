package UNO.game;

import org.json.JSONObject;

import UNO.game.cards.Card;
import UNO.game.game.Game;
import UNO.game.user.Player;

public class GameController {
  // Game fields
  public final static String GAME_STATE_KEY = "game-state";
  public final static String GAME_ACT_PLAYER_KEY = "active-player";
  public final static String GAME_TOP_CARD_KEY = "top-card";
  public final static String GAME_REM_CARDS_KEY = "deck-size";

  // Player fields
  public final static String PLAYER_ID_KEY = "id";
  public final static String PLAYER_ACT_KEY = "active";

  // Card fields
  public final static String CARD_KEY = "card";
  public final static String CARD_COLOR_KEY = "color";
  public final static String CARD_TYPE_KEY = "type";
  public final static String CARD_VAL_KEY = "value";

  private Game unoGame;

  public GameController(int maxTurns) {
    unoGame = new Game(maxTurns);
  }

  public void playerJoin(JSONObject json) {
    unoGame.addPlayer(new Player(unoGame, json.getString(PLAYER_ID_KEY)));
  }

  public void playerLeave(JSONObject json) {
    unoGame.removePlayer(json.getString(PLAYER_ID_KEY));
  }

  public JSONObject getPlayerState(JSONObject json) {
    return unoGame.getPlayerState(json.getString(PLAYER_ID_KEY));
  }

  public JSONObject getGameState() {
    return unoGame.getJSON();
  }

  public boolean gameStart() {
    unoGame.start();
    return true;
  }

  public void playerDraw(JSONObject json) {
    unoGame.getPlayer(json.getString(PLAYER_ID_KEY)).actionDraw();
  }

  public void playerPlay(JSONObject json) {
    JSONObject cardJSON = json.getJSONObject(CARD_KEY);
    Player player = unoGame.getPlayer(json.getString(PLAYER_ID_KEY));
    player.actionPlay(player.getCard(new Card(
        cardJSON.getString(CARD_COLOR_KEY),
        cardJSON.getString(CARD_TYPE_KEY),
        cardJSON.getInt(CARD_VAL_KEY))));
  }
}
