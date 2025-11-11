package UNO.game.user;

import org.json.JSONObject;

import UNO.game.GameController;
import UNO.game.cards.Card;
import UNO.game.cards.CardFactory;

public class PlayerAction {

  private String actionType;
  private Card card;

  public PlayerAction(JSONObject actionJSON) {
    actionType = actionJSON.getString(GameController.ACTION_KEY);
    addActionArgs(actionJSON);
  }

  public PlayerAction(String actionType, Card card) {
    this.actionType = actionType;
    this.card = (card != null) ? card : null;
  }

  private void addActionArgs(JSONObject actionJSON) {
    if (actionType.equals(Player.PLAYER_PLAY)) {
      card = CardFactory.createCard(actionJSON.getJSONObject(GameController.CARD_KEY));
    }
  }

  public String getActionType() {
    return actionType;
  }

  public Card getCard() {
    return card;
  }

  public JSONObject getJSON() {
    JSONObject res = new JSONObject();
    res.put(GameController.ACTION_KEY, actionType);
    if (card != null) {
      res.put(GameController.CARD_KEY, card.json());
    }
    return res;
  }
}
