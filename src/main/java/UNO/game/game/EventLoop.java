package UNO.game.game;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import UNO.game.game.Events.ActionEvent;
import UNO.game.game.Events.GameEvent;
import UNO.game.turn.Turn;
import UNO.game.user.PlayerAction;

public class EventLoop extends Thread {

  private boolean turnEnd = false;
  
  private EventScheduler scheduler;
  private GameEvent currentEvent;
  private Object eventLock = new Object();
  private CompletableFuture<GameEvent> waitForActionEvent = new CompletableFuture<>();
  private int turnNum;

  public EventLoop(Turn turnManager, EventScheduler scheduler) {
    this.scheduler = scheduler;
    turnNum = turnManager.getTurnNum();
  }

  @Override
  public void run() {
    while (!turnEnd) {
      GameEvent event = scheduler.waitForNextEvent();
      setCurrentEvent(event);

      if (event instanceof ActionEvent) {
        waitForActionEvent.complete(event);
      }

      List<GameEvent> nextEvents = event.play();

      if (nextEvents != null) {
        scheduler.addNextEvents(nextEvents);
      } else {
        terminate();
      }
    }
    return;
  }

  public void submitAction(PlayerAction action) {
    GameEvent event = getCurrentEvent();
    if (event == null || !(event instanceof ActionEvent)) {
      try {
        event = waitForActionEvent.get();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (!(event instanceof ActionEvent)) {
      return;
    }
    ((ActionEvent) event).submitAction(action);
  }

  public void terminate() {
    turnEnd = true;
  }

  public boolean hasEnded() {
    return turnEnd;
  }

  public void setCurrentEvent(GameEvent event) {
    synchronized (eventLock) {
      this.currentEvent = event;
    }
  }

  public GameEvent getCurrentEvent() {
    synchronized (eventLock) {
      return currentEvent;
    }
  }

  public int getTurnNum() {
    return turnNum;
  }
}
