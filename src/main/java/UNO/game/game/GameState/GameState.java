package UNO.game.game.GameState;

import UNO.game.game.Game;

public abstract class GameState {

  private boolean gameEnd;

  public GameState() {
    super();
    this.gameEnd = false;
  }

  public abstract GameState nextState(Game game);

  public abstract String string();

  public void endGame() {
    gameEnd = true;
  }

  public boolean gameEnded() {
    return gameEnd;
  }
}
