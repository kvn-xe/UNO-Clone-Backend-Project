package UNO.game.game.GameState;

import UNO.game.game.Game;

public class GameTurn extends GameState {
  
  private Game game; 

  public GameTurn(Game game) {
    super();
    this.game = game;
  }

  @Override
  public GameState nextState(Game game) {
    if (!gameEnded()) {
      return new GameTurn(game);
    }
    return new GameEnd();
  }

  @Override
  public String string() {
    return this.getClass().getSimpleName();
  }
}
