package UNO.game.turn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import UNO.game.user.Player;

public class Turn {

  private int turnNum = 0;
  private int turn = 0;

  private List<Player> playerOrder;
  private HashMap<String, Player> players;


  public Turn(HashMap<String, Player> players) {
    this.players = players;
    playerOrder = new ArrayList<>(players.values());
    Collections.shuffle(playerOrder);
  }

  public void nextTurn() {
    turnNum++;
    turn++;
  }

  public void reverse() {
    turn = players.size() - (turn % players.size());
    Collections.reverse(playerOrder);
  }

  public void skip() {
    turn++;
  }

  public int getNumPlayers() {
    return players.size();
  }

  public int getTurnNum() {
    return turnNum;
  }

  public int getTurn() {
    return turn;
  }

  public Player getActivePlayer() {
    return players.get(playerOrder.get(getTurn() % players.size()).getId());
  }

  public List<Player> getPlayerOrder() {
    return new ArrayList<>(playerOrder);
  }

  public void setPlayerOrder(List<Player> order) {
    playerOrder = order;
  }

  public void setActivePlayer(Player player) {
    turn = playerOrder.indexOf(player);
  }
}
