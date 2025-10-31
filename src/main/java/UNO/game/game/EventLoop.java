package UNO.game.game;

import java.util.List;
import java.util.PriorityQueue;

import UNO.game.game.Events.GameEvent;
import UNO.game.log.GameLog;
import UNO.game.user.Player;

public class EventLoop extends Thread {

  private final static int SLEEP_TIME = 5000;

  private boolean turnEnd = false;
  private Game game = null;
  private PriorityQueue<GameEvent> eventStack;

  public EventLoop(Game game) {
    this.game = game;
    this.eventStack = game.getEventStack();
  }

  @Override
  public void run() {
    while (!turnEnd) {
      Player activePlayer = game.getActivePlayer();
      if (activePlayer.hasQueuedAction()) {
        activePlayer.getQueuedAction().run();
        activePlayer.resetQueue();
      }

      if (eventStack.size() == 0) {
        try {
          sleep(SLEEP_TIME);
        } catch (Exception e) {
          System.err.println("Event Loop Terminated");
          break;
        }

      } else {
        eventStack.add(game.getEventFactory().createEvent(GameEvent.END));

        // Run through Events
        GameEvent event = eventStack.poll();
        List<GameEvent> nextEvents = event.play();
        GameLog.logEventOccurence(event);

        if (nextEvents == null) {
          terminate();
        } else {
          game.addNextEvents(nextEvents);
        }
      }
    }
    return;
  }

  public synchronized void addCurrentEvent(GameEvent event) {
    if (event == null) {
      return;
    }
    eventStack.add(event);
  }

  public synchronized void addNextEvent(GameEvent event) {
    game.addNextEvent(event);
  }

  public void terminate() {
    turnEnd = true;
  }

  public boolean hasEnded() {
    return turnEnd;
  }
}
