package UNO.game.helper;

import UNO.game.game.Game;

public class GameManager implements Runnable {

  private Game game;
  private int turnNum = 0;
  private boolean stateChanged = false;
  private boolean stop = false;
  private Thread gameThread;

  public GameManager(Game game) {
    this.game = game;
  }

  @Override
  public void run() {
    gameThread = new Thread(new GameRunner(game));
    gameThread.start();

    while (game.getTurnManager() == null && !stop) {
      try {
        pause();
      } catch (Exception e) {
      }
    }

    while (!stop) {
      int gameTurn = game.getTurnManager().getTurnNum();
      if (turnNum == gameTurn || stateChanged == true) {
        try {
          pause();
        } catch (Exception e) {
        }
      } else {
        turnNum = gameTurn;
        stateChanged = true;
      }
    }
  }

  public boolean isReady() {
    return stateChanged;
  }

  public void moveOn() {
    stateChanged = false;
  }

  public void terminate() {
    stop = true;
  }

  public void pause() {
    try {
      gameThread.wait(500);
      wait(500);
    } catch (Exception e) {
    }
  }
}
