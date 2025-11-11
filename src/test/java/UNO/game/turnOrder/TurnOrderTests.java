package UNO.game.turnOrder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import UNO.game.GameController;
import UNO.game.cards.Card;
import UNO.game.game.Game;
import UNO.game.testHelper.TestHelper;
import UNO.game.testHelper.TurnExtension;
import UNO.game.user.Player;

public class TurnOrderTests {
  
  private final static Card testCard = new Card("blue", "number", 5);
  private final static Card testReverse = new Card("blue", "reverse");

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

  // Null Arg causes draw action
  private JSONObject JSONAction(Card card) {
    return helper.createAction(testGame.getActivePlayerId(), Player.PLAYER_PLAY, card);
  }

  private void playAction(Card card) {
    controller.playerAction(JSONAction(new Card(card)));
    helper.waitTillTurnFin();
  }

  private JSONObject getJSONLog() {
    return ((TurnExtension) testGame.getTurnManager()).getLog();
  }
  
  @Test
  public void testNormalTurnOrder() {
    initHelper();
    startGame();

    for (int i = 0; i < 8; i++) {
      playAction(testCard);
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

    List<String> playerIds = new ArrayList<>();
    for (int i = 0; i < 7; i++) {
      if (i != 3) {
        playAction(testCard);
      } else {
        playAction(testReverse);
      }
    }

    assertTrue(playerIds.equals(playerIds.reversed()));
  }
}
