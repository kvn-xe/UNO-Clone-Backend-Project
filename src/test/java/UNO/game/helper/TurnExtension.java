package UNO.game.helper;

import java.util.ArrayList;
import java.util.List;

import UNO.game.turn.Turn;
import UNO.game.user.Player;

public class TurnExtension {
  
  private Turn turnManager;

  public TurnExtension(Turn turnManager) {
    this.turnManager = turnManager;
  }

  public List<Player> getNextPlayers() {
    List<Player> playerOrder = turnManager.getPlayerOrder();
    Player activePlayer = turnManager.getActivePlayer();
    List<Player> res = new ArrayList<>();

    int nextIndex = playerOrder.indexOf(activePlayer) + 1;
    for (int i = 0; i < turnManager.getNumPlayers(); i++) {
      res.add(playerOrder.get((nextIndex + i) % turnManager.getNumPlayers()));
    }
    return res;
  }

  public Player getNextPlayer() {
    turnManager.nextTurn();
    return turnManager.getActivePlayer();
  }
}
