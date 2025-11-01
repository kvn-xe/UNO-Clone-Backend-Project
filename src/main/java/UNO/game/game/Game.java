package UNO.game.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import UNO.game.GameController;
import UNO.game.cards.Deck;
import UNO.game.cards.DiscardPile;
import UNO.game.game.Events.EventFactory;
import UNO.game.game.Events.GameEvent;
import UNO.game.turn.Turn;
import UNO.game.user.Player;

public class Game {

  private static final int PLAYER_TIMEOUT = 10;
  private static final int START_HAND = 5;
  
  private Deck deck = null;
  private DiscardPile pile = null;
  private HashMap<String, Player> players;


  private String gameState = "";
  private PriorityQueue<GameEvent> eventStack = null;
  private PriorityQueue<GameEvent> nextEventStack = null;
  private PriorityQueue<GameEvent> universalEventStack = null;

  private EventFactory eventFactory = null;
  private Turn turnManager = null;
  private EventLoop loop;

  private int maxTurns;
  private JSONObject endResult;

  public Game() {
    deck = new Deck();
    pile = new DiscardPile();
    players = new HashMap<>();

    eventStack = new PriorityQueue<>(GameEvent.eventComparator);
    nextEventStack = new PriorityQueue<>(GameEvent.eventComparator);
    universalEventStack = new PriorityQueue<>(GameEvent.eventComparator);

    eventFactory = new EventFactory(this);
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
  }

  public void giveHands() {
    for (Player player : players.values()) {
      player.draw(START_HAND);
    }
  }

  public void initTurnManager() {
    turnManager = new Turn(players);
  }

  public synchronized void start() {
    giveHands();
    initTurnManager();
    gameState = "Game Started";
    
    while (!gameState.equals("Game Over")) {
      if (turnManager.getTurnNum() == maxTurns) {
        break;
      }

      Player activePlayer = turnManager.getActivePlayer();
      gameState = "Turn " + turnManager.getTurnNum();

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
      turnManager.nextTurn();
    }
  }

  public Player getActivePlayer() {
    if (turnManager == null) {
      return null;
    }
    return turnManager.getActivePlayer();
  }

  public void endGame() {
    gameState = "Game Over";
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

  public synchronized void remCurrentEvent(GameEvent event) {
    if (event == null) {
      return;
    }

    if (loop != null) {
      loop.remCurrentEvent(event);
    } else {
      eventStack.remove(event);
    }
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
    return new PriorityQueue<>(nextEventStack);
  }

  public PriorityQueue<GameEvent> getUniversalEventStack() {
    return new PriorityQueue<>(universalEventStack);
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

  public void removePlayer(String playerId) {
    players.remove(playerId);
  }

  public JSONObject getPlayerState(String playerId) {
    Player player = players.get(playerId);
    return player.json();
  }

  public JSONObject getGameState() {
    JSONObject res = new JSONObject();
    JSONArray playerArray = new JSONArray();
    for (Player player : players.values()) {
      playerArray.put(player.json());
    }

    res.put(GameController.GAME_ACT_PLAYER_KEY, getActivePlayer().getId());
    res.put(GameController.GAME_TOP_CARD_KEY, getDiscard().getTopCard().json());
    res.put(GameController.GAME_REM_CARDS_KEY, String.valueOf(getDeck().getNumCards()));
    return res;
  }

  public Player getPlayer(String id) {
    return players.get(id);
  }
}
