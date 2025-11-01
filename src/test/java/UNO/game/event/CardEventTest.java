package UNO.game.event;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import UNO.game.cards.Card;
import UNO.game.game.CardEvent.AddCardEvent;
import UNO.game.game.CardEvent.ChangeCardEvent;
import UNO.game.game.CardEvent.ReverseCardEvent;
import UNO.game.game.CardEvent.SkipCardEvent;
import UNO.game.game.Events.AddEvent;
import UNO.game.game.Events.AddInteractableEvent;
import UNO.game.helper.TestConst;
import UNO.game.helper.TurnExtension;
import UNO.game.helper.GameTestExtension;
import UNO.game.user.Player;

public class CardEventTest {
  private GameTestExtension game;
  private Player testPlayer;

  public void initTestGame() {
    game = new GameTestExtension();
    testPlayer = new Player(game, "bob");
    game.addPlayer(testPlayer);

    game.initTurnManager();
    game.setActivePlayer(testPlayer);
  }

  @Test
  public void TestAddCardEvent() {
    initTestGame();
    game.addCurrentEvent(new AddCardEvent(game, new Card(TestConst.COLOR_1, TestConst.ADD, 2), game.getDiscard(), testPlayer));
    assertTrue(game.playEvent().get(0).getClass().equals(AddInteractableEvent.class));
  }

  @Test
  public void TestChangeCardEvent() {
    initTestGame();
    game.addCurrentEvent(new ChangeCardEvent(game, new Card(TestConst.CHANGE), game.getDiscard(), testPlayer));
    game.playEvent();
    
    Card testCard = new Card(TestConst.COLOR_1, 1);
    Card failCard = new Card(TestConst.COLOR_2, 2);
    assertTrue(game.getDiscard().isValidPlay(testCard));
    assertFalse(game.getDiscard().isValidPlay(failCard));
  }

  @Test
  public void TestReverseCardEvent() {
    game = new GameTestExtension();

    Player player2 = new Player(game, "alex");
    Player player3 = new Player(game, "colt");
    Player player4 = new Player(game, "dexter");
    testPlayer = new Player(game, "bob");

    game.addPlayer(testPlayer);
    game.addPlayer(player2);
    game.addPlayer(player3);
    game.addPlayer(player4);

    game.initTurnManager();
    game.setActivePlayer(testPlayer);

    TurnExtension testTurnManager = new TurnExtension(game.getTurnManager());
    List<Player> initOrder = testTurnManager.getNextPlayers();
    game.addCurrentEvent(new ReverseCardEvent(game, new Card(TestConst.REVERSE), game.getDiscard(), testPlayer));
    game.playEvent();

    int numPlayers = initOrder.size();
    List<Player> testOrder = testTurnManager.getNextPlayers();

    // Remove active player from next cycle of players
    assertTrue(testOrder.remove(numPlayers - 1).equals(testPlayer));
    assertTrue(initOrder.remove(numPlayers - 1).equals(testPlayer));

    Collections.reverse(initOrder);
    for (int i = 0; i < numPlayers - 1; i++) {
      assertTrue(initOrder.get(i).equals(testOrder.get(i)));
    }
  }

  @Test
  public void TestSkipCardEvent() {
    game = new GameTestExtension();

    testPlayer = new Player(game, "bob");
    Player player2 = new Player(game, "albert");
    game.addPlayer(testPlayer);
    game.addPlayer(player2);
    game.initTurnManager();

    game.addCurrentEvent(new SkipCardEvent(game, new Card(TestConst.COLOR_1, TestConst.SKIP), game.getDiscard(), testPlayer));
    game.playEvent();

    TurnExtension turn = new TurnExtension(game.getTurnManager());
    assertTrue(turn.getNextPlayer().equals(testPlayer));
  }
}
