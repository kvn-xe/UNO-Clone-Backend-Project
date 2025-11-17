package UNO.game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import UNO.game.cards.Card;
import UNO.game.game.Game;
import UNO.game.testHelper.TestHelper;
import UNO.game.testHelper.TurnExtension;
import UNO.game.user.Player;

public class GameControllerTests {

  private final static Card testCard = new Card("blue", "number", 5);
  private final static Card testReverse = new Card("blue", "reverse");
  private final static Card testSkip = new Card("blue", "skip");
  private final static Card testAdd = new Card("blue", "add");

  private TestHelper helper;
  private Game testGame;
  private GameController controller;

  private void initHelper() {
    helper = new TestHelper();
    testGame = helper.getTestGame();
    controller = new GameController(testGame);
  }

  private void startGame() {
    Thread gameThread = createGameThread(controller);
    gameThread.start();
    testGame.waitTillReady();
  }

  private Thread createGameThread(GameController controller) {
    return new Thread() {
      @Override
      public void run() {
        controller.gameStart();
      }
    };
  }

  private List<String> getPlayerIds(JSONObject log) {
    List<String> playerList = new ArrayList<>();
    for (String key : (Iterable<String>) () -> log.keys()) {
      playerList.add(log.getJSONObject(key).getString(GameController.GAME_ACT_PLAYER_KEY));
    }
    return playerList;
  }

  private JSONObject JSONAction(Card card) {
    return helper.createAction(testGame.getActivePlayerId(), Player.PLAYER_PLAY, card);
  }

  private JSONObject JSONAction() {
    return helper.createAction(testGame.getActivePlayerId(), Player.PLAYER_DRAW);
  }

  private void playAction(Card card, int turnNum) {
    testGame.getActivePlayer().addCard(card);
    controller.playerAction(JSONAction(new Card(card)));
    helper.waitTillTurnReady(turnNum);
  }

  private void playAction(int turnNum) {
    controller.playerAction(JSONAction());
    helper.waitTillTurnReady(turnNum);
  }

  private JSONObject getJSONLog() {
    return ((TurnExtension) testGame.getTurnManager()).getLog();
  }

  @Test
  public void testOnce() {
    initHelper();
    startGame();
    
    for (int i = 0; i < 8; i++) {
      playAction(testCard, i + 1);
    }
  }

  @Test
  public void testNormalTurnOrder() {
    initHelper();
    startGame();

    // playAction(testCard);
    for (int i = 0; i < 8; i++) {
      playAction(testCard, i + 1);
    }

    JSONObject logObject = getJSONLog();
    List<String> playerList = new ArrayList<>();
    for (String key : (Iterable<String>) () -> logObject.keys()) {
      playerList.add(logObject.getJSONObject(key).getString(GameController.GAME_ACT_PLAYER_KEY));
    }

    // Order stays consistent
    for (int i = 0; i < 4; i++) {
      assertTrue(playerList.get(i).equals(playerList.get(i + 4)));
    }

    // Players do not repeat unnecessarily
    Set<String> playerSet = new HashSet<>(playerList);
    assertTrue(playerSet.size() == 4);
  }

  @Test
  public void testReverseTurnOrder() {
    initHelper();
    startGame();

    for (int i = 0; i < 7; i++) {
      if (i == 3) {
        playAction(testReverse, i + 1);
        continue;
      }
      playAction(testCard, i + 1);
    }

    List<String> playerIds = getPlayerIds(getJSONLog());
    assertTrue(playerIds.equals(playerIds.reversed()));
  }

  @Test
  public void testSkipTurnOrder() {
    initHelper();
    startGame();

    for (int i = 0; i < 8; i++) {
      if (i == 4) {
        playAction(testSkip, i + 1);
      } else {
        playAction(testCard, i + 1);
      }
    }

    List<String> playerList = getPlayerIds(getJSONLog());
    assertTrue(playerList.get(2).equals(playerList.get(5)));
  }

  @Test
  public void testAddChaining() {
    initHelper();
    startGame();

    JSONObject player = new JSONObject();
    player.put(GameController.PLAYER_ID_KEY, testGame.getActivePlayerId());

    for (int i = 0; i < 4; i++) {
      playAction(testAdd, i + 1);
    }
    playAction(5);

    String playerId =
    getJSONLog().getJSONObject("4").getString(GameController.GAME_ACT_PLAYER_KEY);
    assertTrue(testGame.getPlayer(playerId).getNumCards() == 13);
  }
}
