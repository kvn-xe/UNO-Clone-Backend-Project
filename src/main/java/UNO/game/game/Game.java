package UNO.game.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONArray;
import org.json.JSONObject;

import UNO.game.GameController;
import UNO.game.cards.Card;
import UNO.game.cards.Deck;
import UNO.game.cards.DiscardPile;
import UNO.game.game.Events.EventFactory;
import UNO.game.game.Events.GameEvent;
import UNO.game.game.GameState.GameEnd;
import UNO.game.game.GameState.GameStart;
import UNO.game.game.GameState.GameState;
import UNO.game.turn.Turn;
import UNO.game.user.Player;
import UNO.game.user.PlayerAction;

public class Game {

  private static final int PLAYER_TIMEOUT = 100;
  private static final int START_HAND = 5;
  private static final int MAX_TURNS = 500;
  private static final int DRAW_DEF = 2;
  
  private Deck deck = null;
  private DiscardPile pile = null;
  private HashMap<String, Player> players;
  private GameState gameState;
  private EventScheduler scheduler;
  private EventFactory eventFactory = null;
  private Turn turnManager = null;
  private EventLoop loop;
  private ExecutorService eventExecutor = Executors.newSingleThreadExecutor();
  private CompletableFuture<Void> ready = new CompletableFuture<>();
  private volatile CompletableFuture<Void> turnFin = new CompletableFuture<>();
  private final Object turnLock = new Object();
  private final Object readyLock = new Object();
  private int maxTurns;

  private volatile int completedTurns = 0;

  public Game() {
    this(MAX_TURNS);
  }

  public Game(int maxTurns) {
    deck = new Deck();
    pile = new DiscardPile();
    players = new HashMap<>();
    eventFactory = new EventFactory(this);
    scheduler = new EventScheduler(this);
    gameState = new GameStart(this);

    this.maxTurns = maxTurns;
  }

  public Game(int maxTurns, Deck deck, DiscardPile pile, HashMap<String, Player> players, Turn turnManager, EventScheduler scheduler) {
    this.deck = (deck != null) ? deck : new Deck();
    this.pile = (pile != null) ? pile : new DiscardPile();
    this.players = (players != null) ? players : new HashMap<>();
    this.turnManager = (turnManager != null) ? turnManager : null;
    this.scheduler = (scheduler != null) ? scheduler : new EventScheduler(this);
    gameState = new GameStart(this);
    eventFactory = new EventFactory(this);

    this.maxTurns = maxTurns;
  }

  public void giveHands() {
    for (Player player : players.values()) {
      player.draw(START_HAND);
    }
  }

  public void waitTillReady() {
    try {
      ready.get();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void initTurnManager() {
    if (turnManager == null) {
      turnManager = new Turn(players);
    }
  }

  public void initDiscard() {
    pile.init(deck);
  }

  public void start() {
    if (!(gameState instanceof GameStart)) {
      return;
    }
    ((GameStart) gameState).start();

    while (!(gameState instanceof GameEnd)) {
      if (turnManager.getTurnNum() == maxTurns) {
        break;
      }

      loop = new EventLoop(turnManager, scheduler);
      if (!ready.isDone()) {
        ready.complete(null);
      }

      Future<?> eventThread = eventExecutor.submit(loop);
      synchronized (readyLock) {
        readyLock.notifyAll();
      }

      try {
        eventThread.get(PLAYER_TIMEOUT, TimeUnit.SECONDS);
      } catch (TimeoutException e) {
        System.out.println("Timeout");
        eventThread.cancel(true);
      } catch (InterruptedException e) {
        System.err.println("Turn Interrupted");
      } catch (Exception e) {
        e.printStackTrace();
      }

      scheduler.cycleEvents(this);
      turnManager.nextTurn();
      synchronized (turnLock) {
        completedTurns++;     
        turnLock.notifyAll();
      }
    }

    endGame();
  }

  public void waitTillTurnReady(int turnNum) {
    try {
      while (loop == null || loop.getTurnNum() < turnNum) {
        synchronized (readyLock) {
          readyLock.wait();
        }
      } 
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void waitTillTurnReady() {
    int turnNumRunning = loop.getTurnNum();
    try {
      while (loop == null || turnNumRunning > completedTurns) {
        synchronized (readyLock) {
          readyLock.wait();
        }
      } 
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void waitTillTurn(int turnNum) {
    synchronized (turnLock) {
      // Completed Turns start at 1, turnNum is 0-indexed
      while (completedTurns <= turnNum) {
        try {
          turnLock.wait();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return;
        }
      }
    }
  }

  public void waitTillTurnFin() {
    waitTillTurn(turnManager.getTurnNum());
  }

  public void waitTillTurnFin(int turnNum) {
    waitTillTurn(turnNum);
  }

  public CompletableFuture<Void> getNextTurnFin() {
    synchronized (turnLock) {
      return turnFin;
    }
  }

  public Player getActivePlayer() {
    if (turnManager == null) {
      return null;
    }
    return turnManager.getActivePlayer();
  }

  public String getActivePlayerId() {
    return getActivePlayer().getId();
  }

  public void endGame() {
    gameState.endGame();
    cleanup();
  }

  public EventFactory getEventFactory() {
    return eventFactory;
  }

  public GameState getState() {
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

  public JSONObject getJSON() {
    JSONObject res = new JSONObject();
    JSONArray playerArray = new JSONArray();
    for (Player player : players.values()) {
      playerArray.put(player.json());
    }

    res.put(GameController.GAME_STATE_KEY, gameState.string());
    res.put(GameController.GAME_ACT_PLAYER_KEY, getActivePlayer().getId());
    res.put(GameController.GAME_TOP_CARD_KEY, getDiscard().getTopCard().json());
    res.put(GameController.GAME_REM_CARDS_KEY, String.valueOf(getDeck().getNumCards()));
    return res;
  }

  public void registerPlayerAction(String playerId, PlayerAction action) {
    if (!turnManager.getActivePlayer().getId().equals(playerId)) {
      return;
    }
    loop.submitAction(action);
  }

  public void addCurrentEvent(GameEvent event) {
    scheduler.addCurrentEvent(event);
  }

  public EventScheduler getSchedule() {
    return scheduler;
  }

  public Player getPlayer(String id) {
    return players.get(id);
  }

  public void drawAction() {
    scheduler.addCurrentEvent(eventFactory.createEvent(GameEvent.ADD, DRAW_DEF));
  }

  public void playAction(Card card) {
    scheduler.addCurrentEvent(eventFactory.createEvent(card));
  }

  public void addUniversalEvent(GameEvent event) {
    scheduler.addUniversalEvent(event);
  }

  public void cleanup() {
    eventExecutor.shutdown();
  }
}
