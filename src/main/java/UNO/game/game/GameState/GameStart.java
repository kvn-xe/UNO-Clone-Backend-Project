package UNO.game.game.GameState;

import UNO.game.game.Game;

public class GameStart extends GameState {

  private Game game;

  public GameStart(Game game) {
    super();
    this.game = game;
  }

  public void start() {
    game.initTurnManager();
    game.giveHands();
    game.initDiscard();
  }

  @Override
  public GameState nextState(Game game) {
    if (gameEnded()) {
      return new GameEnd();
    }
    return new GameTurn(game);
  }

  @Override
  public String string() {
    return this.getClass().getSimpleName();
  }
}
