package UNO.game.game.Events;

import java.util.List;

public interface GameEvent {
  public List<GameEvent> play();

  public int getPrio();

  public final static String ADD = "add";
  public final static String ADD_I = "add-interactable";
  public final static String SKIP = "skip";
  public final static String REVERSE = "reverse";
  public final static String END = "end";
  public final static String FORCED_END = "forced-end";
  public final static String CHAIN_END = "chain-end";
  public final static String UNO = "uno";

  public final static int ADD_P = 7;
  public final static int ADDI_P = 3;
  public final static int ACT_P = 1;
  public final static int ENDC_P = 5;
  public final static int END_P = 10;
  public final static int ENDF_P = 0;
  public final static int ADDC_P = 2;
  public final static int CARD_P = 6;
  public final static int UNO_P = 1;
}
