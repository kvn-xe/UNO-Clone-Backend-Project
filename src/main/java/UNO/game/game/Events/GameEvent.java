package UNO.game.game.Events;

import java.util.Comparator;
import java.util.List;

public interface GameEvent {
  public List<GameEvent> play();

  public int getPrio();

  public final static Comparator<GameEvent> eventComparator = new Comparator<>() {
    @Override
    public int compare(GameEvent o1, GameEvent o2) {
      return o1.getPrio() - o2.getPrio();
    }
  };

  public final static String ADD = "add";
  public final static String ADD_I = "add-interactable";
  public final static String SKIP = "skip";
  public final static String REVERSE = "reverse";
  public final static String END = "end";
  public final static String FORCED_END = "forced-end";
  public final static String UNO = "uno";
}
