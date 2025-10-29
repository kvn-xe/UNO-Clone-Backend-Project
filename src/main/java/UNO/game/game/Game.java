package UNO.game.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import UNO.game.cards.Deck;
import UNO.game.cards.DiscardPile;
import UNO.game.game.Events.EventFactory;
import UNO.game.game.Events.GameEvent;
import UNO.game.turn.Turn;
import UNO.game.user.Player;

public class Game {

  private static final int PLAYER_TIMEOUT = 10;
  
  private Deck deck = null;
  private DiscardPile pile = null;
  private HashMap<String, Player> players;
  private List<String> playerOrder;

  private String gameState = null;
  private Player activePlayer = null;
  private PriorityQueue<GameEvent> eventStack = null;
  private PriorityQueue<GameEvent> nextEventStack = null;
  private PriorityQueue<GameEvent> universalEventStack = null;

  private EventFactory eventFactory = null;
  private Turn turnManager = null;

  public Game() {
    deck = new Deck();
    pile = new DiscardPile();
    players = new HashMap<>();

    eventStack = new PriorityQueue<>(GameEvent.eventComparator);
    nextEventStack = new PriorityQueue<>(GameEvent.eventComparator);
    universalEventStack = new PriorityQueue<>(GameEvent.eventComparator);

    eventFactory = new EventFactory(this);
  }

  public synchronized void start() {
    playerOrder = new ArrayList<>(players.keySet());
    Collections.shuffle(playerOrder);

    turnManager = new Turn(players.size());
    while (!gameState.equals("Game Over")) {
      activePlayer = players.get(playerOrder.get((turnManager.getTurn()) % (players.size())));

      EventLoop thread = new EventLoop(this);
      thread.run();

      long start = System.currentTimeMillis();
      while ((System.currentTimeMillis() - start) < PLAYER_TIMEOUT * 1000) {
        try {
          wait(500);
        } catch (Exception e) {
          break;
        }
      }
      thread.terminate();

      eventStack = nextEventStack;
      nextEventStack = new PriorityQueue<>(GameEvent.eventComparator);
    }
  }

  public Player getActivePlayer() {
    return activePlayer;
  }

  public synchronized void addNextEvent(GameEvent event) {
    if (event == null) {
      return;
    }
    nextEventStack.add(event);
    return;
  }

  public synchronized void addCurrentEvent(GameEvent event) {
    if (event == null) {
      return;
    }
    eventStack.add(event);
    return;
  }

  public void addNextEvents(Collection<GameEvent> events) {
    for (GameEvent event : events) {
      addNextEvent(event);
    }
  }

  public EventFactory getEventFactory() {
    return eventFactory;
  }

  public PriorityQueue<GameEvent> getEventStack() {
    return eventStack;
  }

  public PriorityQueue<GameEvent> getNextEventStack() {
    return nextEventStack;
  }

  public PriorityQueue<GameEvent> getUniversalEventStack() {
    return universalEventStack;
  }

  public Deck getDeck() {
    return deck;
  }

  public Turn getTurnManager() {
    return turnManager;
  }

  public List<String> getPlayerOrder() {
    return new ArrayList<>(playerOrder);
  }

  public void setPlayerOrder(List<String> playerOrder) {
    this.playerOrder = playerOrder;
  }

  public DiscardPile getDiscard() {
    return pile;
  }

  public String logGameState() {
    String res = "Game:\n";
    for (Player p : players.values()) {
      res += p.string();
    }
    return res;
  }
}
