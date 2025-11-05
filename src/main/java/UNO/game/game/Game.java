package UNO.game.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class Game {

  private static final int PLAYER_TIMEOUT = 10;
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

  private int maxTurns;

  public Game() {
    this(MAX_TURNS);
  }

  public Game(int maxTurns) {
    deck = new Deck();
    pile = new DiscardPile();
    players = new HashMap<>();

    eventFactory = new EventFactory(this);
    scheduler = new EventScheduler();
    gameState = new GameStart(this);

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
    if (!(gameState instanceof GameStart)) {
      return;
    }
    ((GameStart) gameState).start();
    
    while (!(gameState instanceof GameEnd)) {
      if (turnManager.getTurnNum() == maxTurns) {
        break;
      }

      loop = new EventLoop(turnManager, scheduler);
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

      scheduler.cycleEvents();
      turnManager.nextTurn();
    }

    endGame();
  }

  public Player getActivePlayer() {
    if (turnManager == null) {
      return null;
    }
    return turnManager.getActivePlayer();
  }

  public void endGame() {
    gameState.endGame();
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
}
