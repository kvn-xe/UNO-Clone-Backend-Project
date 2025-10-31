package UNO.game.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import UNO.game.cards.Deck;
import UNO.game.cards.DiscardPile;
import UNO.game.game.Events.EventFactory;
import UNO.game.game.Events.GameEvent;
import UNO.game.log.GameLog;
import UNO.game.turn.Turn;
import UNO.game.user.Player;

public class Game {

  private static final int PLAYER_TIMEOUT = 10;
  private static final int START_HAND = 5;
  
  private Deck deck = null;
  private DiscardPile pile = null;
  private HashMap<String, Player> players;


  private String gameState = "ready";
  private PriorityQueue<GameEvent> eventStack = null;
  private PriorityQueue<GameEvent> nextEventStack = null;
  private PriorityQueue<GameEvent> universalEventStack = null;

  private EventFactory eventFactory = null;
  private Turn turnManager = null;
  private EventLoop loop;

  private int maxTurns;

  public Game() {
    deck = new Deck();
    pile = new DiscardPile();
    players = new HashMap<>();

    eventStack = new PriorityQueue<>(GameEvent.eventComparator);
    nextEventStack = new PriorityQueue<>(GameEvent.eventComparator);
    universalEventStack = new PriorityQueue<>(GameEvent.eventComparator);

    eventFactory = new EventFactory(this);
    GameLog.startLog();
  }

  public Game(int maxTurns) {
    deck = new Deck();
    pile = new DiscardPile();
    players = new HashMap<>();

    eventStack = new PriorityQueue<>(GameEvent.eventComparator);
    nextEventStack = new PriorityQueue<>(GameEvent.eventComparator);
    universalEventStack = new PriorityQueue<>(GameEvent.eventComparator);

    eventFactory = new EventFactory(this);
    this.maxTurns = maxTurns;
    GameLog.startLog();
  }

  public void giveHands() {
    for (Player player : players.values()) {
      player.draw(START_HAND);
    }
  }

  public synchronized void start() {
    giveHands();
    turnManager = new Turn(players);
    gameState = "Game Started";
    
    while (!gameState.equals("Game Over")) {
      if (turnManager.getTurnNum() == maxTurns) {
        break;
      }

      Player activePlayer = turnManager.getActivePlayer();
      gameState = "Turn " + turnManager.getTurnNum();

      GameLog.logPlayerTurn(activePlayer);
      turnManager.nextTurn();

      loop = new EventLoop(this);
      Thread thread = new Thread(loop);
      thread.start();

      long start = System.currentTimeMillis();
      while ((System.currentTimeMillis() - start) < PLAYER_TIMEOUT * 1000) {
        if (loop.hasEnded()) {
          break;
        }

        try {
          wait(500);
        } catch (Exception e) {
        }
      }

      loop.terminate();
      try {
        thread.join();
      } catch (Exception e) {
        System.out.println("EventLoop Thread Interrupted");
      }

      eventStack = nextEventStack;
      nextEventStack = new PriorityQueue<>(GameEvent.eventComparator);
    }
  }

  public Player getActivePlayer() {
    if (turnManager == null) {
      return null;
    }
    return turnManager.getActivePlayer();
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

    if (loop != null) {
      loop.addCurrentEvent(event);
    } else {
      eventStack.add(event);
    }
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
    return new PriorityQueue<>(eventStack);
  }

  public PriorityQueue<GameEvent> getNextEventStack() {
    return nextEventStack;
  }

  public PriorityQueue<GameEvent> getUniversalEventStack() {
    return universalEventStack;
  }

  public String getState() {
    return gameState;
  }

  public Deck getDeck() {
    return deck;
  }

  public Turn getTurnManager() {
    return turnManager;
  }

  public void setActivePlayer(Player player) {
    turnManager.setActivePlayer(player);
  }

  public List<Player> getPlayersInOrder() {
    return new ArrayList<>(turnManager.getPlayerOrder());
  }

  public void setPlayerOrder(List<Player> playerOrder) {
    turnManager.setPlayerOrder(playerOrder);
  }

  public DiscardPile getDiscard() {
    return pile;
  }

  public void addPlayer(Player player) {
    players.put(player.getId(), player);
  }

  public void removePlayer(Player player) {
    players.remove(player.getId());
  }

  public String logGameState() {
    String res = "Game:\n";
    for (Player p : players.values()) {
      res += p.string();
    }
    return res;
  }
}
