package UNO.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import UNO.game.cards.Deck;
import UNO.game.cards.DiscardPile;
import UNO.game.user.Player;

public class Game {
  
  private Deck deck = null;
  private DiscardPile pile = null;
  private HashMap<String, Player> players;
  private List<String> playerOrder;

  private String gameState = null;
  private Player activePlayer = null;

  public Game() {
    deck = new Deck();
    pile = new DiscardPile();
    players = new HashMap<>();
  }

  public synchronized void start() {
    playerOrder = new ArrayList<>(players.keySet());
    Collections.shuffle(playerOrder);

    int turnNum = 0;
    while (!gameState.equals("Game Over")) {
      activePlayer = players.get(playerOrder.get(turnNum % (players.size())));

      try {
        wait(10);
      } catch (Exception e) {
        break;
      }
    }
  }
}
