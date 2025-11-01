package UNO.game.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import UNO.game.game.Game;
import UNO.game.helper.TestHelper;
import UNO.game.user.Player;

public class PlayerTest {
  
  @Test
  public void testTurnOrder() {
    TestHelper helper = new TestHelper();
    Game game = helper.createFullGame();

    helper.loadGame(game);
    helper.startGame();
    helper.waitTillStart();

    Player p1 = game.getActivePlayer();
    p1.actionDraw();
    helper.waitTillNextTurn();

    Player p2 = game.getActivePlayer();
    p2.actionDraw();
    helper.waitTillNextTurn();

    Player p3 = game.getActivePlayer();
    p3.actionDraw();
    helper.waitTillNextTurn();

    Player p4 = game.getActivePlayer();
    p4.actionDraw();
    helper.waitTillNextTurn();

    assertNotEquals(p1, p2);
    assertNotEquals(p2, p3);
    assertNotEquals(p3, p4);
    assertNotEquals(p4, p1);

    assertEquals(p1, game.getActivePlayer());
    p1.actionDraw();
    helper.waitTillNextTurn();

    assertEquals(p2, game.getActivePlayer());
    p2.actionDraw();
    helper.waitTillNextTurn();

    assertEquals(p3, game.getActivePlayer());
    p3.queueActionDraw();
    helper.waitTillNextTurn();

    helper.terminate();
  }
}
