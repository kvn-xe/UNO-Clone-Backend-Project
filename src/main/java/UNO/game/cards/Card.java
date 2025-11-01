package UNO.game.cards;

import java.util.List;

import org.json.JSONObject;

public class Card {
  public final static String NUMBER = "number";
  public final static String SKIP = "skip";
  public final static String ADD = "add";
  public final static String REVERSE = "reverse";
  public final static String CHANGE = "change";

  public final static List<String> COLORS = List.of("red", "blue", "yellow", "green", "any");
  public final static List<String> TYPES = List.of("number", "skip", "add", "reverse");
  public final static List<String> SPECIAL = List.of("add", "change");

  private String color = "";
  private String type = "";
  private int value = 0;
  private String status = "deck";

  public Card(String color, String type, int value) {
    this.color = color;
    this.type = type;
    this.value = value;
  }

  public Card(String color, int value) {
    this.color = color;
    this.type = "number";
    this.value = value;
  }

  public Card(String color, String type) {
    this.color = color;
    this.type = type;

    if (type.equals("add")) {
      this.value = 2;
    }
  }

  public Card(String type) {
    this.color = "any";
    this.type = type;
  }

  public Card(Card card) {
    this.color = card.getColor();
    this.type = card.getType();
    this.value = card.getValue();
  }

  public void discard() {
    this.status = "discard";
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String string() {
    return "{ color: " + color + ", type: " + type + ", value: " + value + " }";
  }

  public JSONObject json() {
    JSONObject res = new JSONObject();
    res.put("object", "card");
    res.put("color", color);
    res.put("type", type);
    res.put("value", value);
    return res;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Card)) {
      return false;
    }
    Card cardObj = (Card) obj;

    if (!cardObj.getColor().equals(color)) {
      return false;
    }
    if (!cardObj.getType().equals(type)) {
      return false;
    }
    if (cardObj.getValue() != value) {
      return false;
    }

    return true;
  }
}
