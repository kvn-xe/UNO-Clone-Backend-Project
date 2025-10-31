package UNO.game.helper;

import UNO.game.game.Game;

public class GameRunner implements Runnable {

  private Game game = null;
  
  public GameRunner(Game game) {
    this.game = game;
  }

  @Override
  public void run() {
    game.start();
  }
}
