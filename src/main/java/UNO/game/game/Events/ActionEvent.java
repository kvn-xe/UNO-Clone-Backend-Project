package UNO.game.game.Events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;

import UNO.game.game.Game;
import UNO.game.user.Player;
import UNO.game.user.PlayerAction;

public class ActionEvent implements GameEvent {

  private final static int PRIO = GameEvent.ACT_P;

  private Game game;
  public CompletableFuture<PlayerAction> playerAction;
  public PlayerAction action;

  public ActionEvent(Game game) {
    this.game = game;
  }

  @Override
  public List<GameEvent> play() {
    waitForAction();
    processAction();
    return new ArrayList<>();
  }

  @Override
  public int getPrio() {
    return PRIO;
  }

  private void waitForAction() {
    try {
      action = playerAction.get();
    } catch (CancellationException e) {
      System.out.println("Wait cancelled");
    } catch (InterruptedException e) {
      System.out.println("Wait interrupted");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return;
  }

  public void submitAction(PlayerAction action) {
    playerAction.complete(action);
  }

  private void processAction() {
    if (playerAction == null) {
      game.drawAction();
    }

    switch (action.getActionType()) {
      case Player.PLAYER_DRAW:
        game.drawAction();
        break;
      case Player.PLAYER_PLAY:
        game.addCurrentEvent(game.getEventFactory().createEvent(action.getCard()));
      default:
        break;
    }
  }
}
