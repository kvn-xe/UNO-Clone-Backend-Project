package UNO.game.game.GameState;

import UNO.game.game.Game;

public class GameEnd extends GameState {

  public GameEnd() {
    super();
  }

  @Override
  public GameState nextState(Game game) {
    return null;
  }

  @Override
  public String string() {
    return this.getClass().getSimpleName();
  }
}
