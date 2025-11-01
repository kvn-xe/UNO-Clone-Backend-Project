package UNO.game.helper;

import UNO.game.game.Game;
import UNO.game.user.Player;

public class TestHelper {

  private Game game = null;
  private GameManager manager = null;
  private Thread managerThread = null;

  public Game createFullGame() {
    Game game = new Game(8);
    game.addPlayer(new Player(game, "p1"));
    game.addPlayer(new Player(game, "p2"));
    game.addPlayer(new Player(game, "p3"));
    game.addPlayer(new Player(game, "p4"));
    return game;
  }

  public void loadGame(Game game) {
    this.game = game;
  }

  public void startGame() {
    manager = new GameManager(game);
    managerThread = new Thread(manager);
    managerThread.start();
    System.out.println("Manager thread started");
  }

  public boolean isReady() {
    return manager.isReady();
  }

  public boolean isDone() {
    return !managerThread.isAlive();
  }

  public void waitTillStart() {
    while (game.getActivePlayer() == null) {
      try {
        wait(200);
      } catch (Exception e) {
      }
    }
  }

  public void waitTillNextTurn() {
    if (manager.isReady()) {
      manager.moveOn();
    }

    while (!manager.isReady()) {
      try {
        wait(1000);
      } catch (Exception e) {
      }
    }
  }

  public void pause(int num) {
    try {
      wait(num);
    } catch (Exception e) {
    }
  }

  public void terminate() {
    manager.terminate();
  }
}
