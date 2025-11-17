package UNO.game.testHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import UNO.game.GameController;
import UNO.game.cards.Card;
import UNO.game.game.Game;
import UNO.game.user.Player;
import UNO.game.user.PlayerAction;

public class TestHelper {
  
  public final static String PLAYER_1 = "andy";
  public final static String PLAYER_2 = "bob";
  public final static String PLAYER_3 = "chile";
  public final static String PLAYER_4 = "dain";

  private Player player1 = new Player(PLAYER_1);
  private Player player2 = new Player(PLAYER_2);
  private Player player3 = new Player(PLAYER_3);
  private Player player4 = new Player(PLAYER_4);
  private List<Player> players = new ArrayList<>(List.of(player1, player2, player3, player4));

  private Game testGame;
  private TurnExtension testTurnManager;

  public void createTestGame() {
    HashMap<String, Player> playerMap = getPlayerMap();
    testTurnManager = new TurnExtension(playerMap);
    testGame = new Game(100, null, null, playerMap, testTurnManager,null);
  }

  private HashMap<String, Player> getPlayerMap() {
    HashMap<String, Player> players = new HashMap<>();
    players.put(PLAYER_1, player1);
    players.put(PLAYER_2, player2);
    players.put(PLAYER_3, player3);
    players.put(PLAYER_4, player4);

    return players;
  }

  public TurnExtension getTestTurnManager() {
    return testTurnManager;
  }

  public Game getTestGame() {
    if (testGame == null) {
      createTestGame();
    }

    for (Player player : players) {
      player.setGame(testGame);
    }
    testTurnManager.setGame(testGame);

    return testGame;
  }

  public void nextTurn() {
    testTurnManager.nextTurn();
  }

  public void waitTillTurnFin() {
    testGame.waitTillTurnFin();
  }

  public void waitTillTurnReady() {
    testGame.waitTillTurnReady();
  }

  public void waitTillTurnReady(int turnNum) {
    testGame.waitTillTurnReady(turnNum);
  }

  public void waitTillTurnFin(int turnNum) {
    testGame.waitTillTurnFin(turnNum);
  }

  public JSONObject createAction(String playerId, String actionType, Card card) {
    JSONObject res = new JSONObject();
    res.put(GameController.ACTION_KEY, new PlayerAction(actionType, card).getJSON());
    res.put(GameController.PLAYER_ID_KEY, playerId);
    return res;
  }

  public JSONObject createAction(String playerId, String actionType) {
    JSONObject res = new JSONObject();
    res.put(GameController.ACTION_KEY, new PlayerAction(actionType, null).getJSON());
    res.put(GameController.PLAYER_ID_KEY, playerId);
    return res;
  }
}
