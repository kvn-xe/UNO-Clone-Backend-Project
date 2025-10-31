package UNO.game.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import UNO.game.cards.Card;
import UNO.game.cards.CardContainer;
import UNO.game.game.Game;
import UNO.game.game.Events.GameEvent;

public class Player implements CardContainer {
  private final static int DRAW_DEF = 1;
  private Game game;
  private List<Card> cards = new ArrayList<>();
  private String id;
  private Runnable queuedAction;

  public Player() {
  }

  public Player(Game game, String id) {
    this.game = game;
    this.id = id;
  }

  public void draw(int num) {
    for (int i = 0; i < num; i++) {
      addCard(game.getDeck().draw());
    }
  }

  public void actionDraw() {
    if (!game.getActivePlayer().equals(this)) {
      queueActionDraw();
      return;
    }
    game.addCurrentEvent(game.getEventFactory().createEvent(GameEvent.ADD, DRAW_DEF));
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void actionPlay(Card card) {
    if (!game.getActivePlayer().equals(this)) {
      queueActionPlay(card);
    }
    game.addCurrentEvent(game.getEventFactory().createEvent(card));
  }

  @Override
  public Card removeCard(Card card) {
    cards.remove(card);
    return card;
  }

  @Override
  public void addCard(Card card) {
    cards.add(card);
  }

  public String string() {
    String res = "Player[ " + id + " ]:\n";
    for (Card card : cards) {
      res += card.string() + "\n";
    }
    return res;
  }

  @Override
  public boolean equals(Object obj) {
    if (!obj.getClass().equals(this.getClass())) {
      return false;
    }
    if (((Player) obj).getId().equals(this.getId())) {
      return true;
    }
    return false;
  }

  public void queueActionPlay(Card card) {
    queuedAction = () -> {
      game.addCurrentEvent(game.getEventFactory().createEvent(card));
    };
  }

  public void queueActionDraw() {
    queuedAction = () -> {
      if (!game.getActivePlayer().equals(this)) {
        return;
      }
      game.addCurrentEvent(game.getEventFactory().createEvent(GameEvent.ADD, DRAW_DEF));
    };
  }

  public boolean hasQueuedAction() {
    return (queuedAction != null);
  }

  public void resetQueue() {
    queuedAction = null;
  }

  public Runnable getQueuedAction() {
    return queuedAction;
  }

  @Override
  public int getNumCards() {
    return cards.size();
  }

  public JSONObject json() {
    JSONObject res = new JSONObject();
    res.put("id", id);

    if (this.equals(game.getActivePlayer())) {
      res.put("active", true);
    } else {
      res.put("active", false);
    }

    JSONArray hand = new JSONArray();
    for (Card card : cards) {
      hand.put(card.json());
    }
    res.put("cards", hand);

    return res;
  }

  public Card getCard(Card card) {
    Card res = null;
    for (Card c : cards) {
      if (c.equals(card)) {
        res = c;
      }
    }
    return res;
  }
}
