package UNO.game.game;

import java.util.List;

import UNO.game.game.Events.GameEvent;
import UNO.game.turn.Turn;

public class EventLoop extends Thread {

  private boolean turnEnd = false;
  
  private EventScheduler scheduler;
  private Turn turnManager;

  public EventLoop(Turn turnManager, EventScheduler scheduler) {
    this.turnManager = turnManager;
    this.scheduler = scheduler;
  }

  @Override
  public void run() {
    while (!turnEnd) {
      turnManager.triggerQueuedActions();

      try {
        GameEvent event = scheduler.waitForNextEvent(); 
        List<GameEvent> nextEvents = event.play();
        if (nextEvents != null) {
          scheduler.addNextEvents(nextEvents);
        } else {
          terminate();
        }

      } catch (InterruptedException e) {
        System.err.println("EventLoop Interrupted");
        Thread.currentThread().interrupt();
      }
    }
    return;
  }

  public void terminate() {
    turnEnd = true;
  }

  public boolean hasEnded() {
    return turnEnd;
  }
}
