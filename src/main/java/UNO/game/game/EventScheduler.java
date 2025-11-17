package UNO.game.game;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;

import UNO.game.game.Events.ActionEvent;
import UNO.game.game.Events.GameEvent;

public class EventScheduler {

  private PriorityQueue<GameEvent> currentEvents = new PriorityQueue<>(EVENT_CMP);
  private PriorityQueue<GameEvent> nextEvents = new PriorityQueue<>(EVENT_CMP);
  private PriorityQueue<GameEvent> universalEvents = new PriorityQueue<>(EVENT_CMP);
  private CompletableFuture<Void> hasEvents = new CompletableFuture<>();

  public final static Comparator<GameEvent> EVENT_CMP = new Comparator<>() {
    @Override
    public int compare(GameEvent o1, GameEvent o2) {
      return o1.getPrio() - o2.getPrio();
    }
  };

  public EventScheduler(Game game) {
    currentEvents.add(new ActionEvent(game));
  }

  public synchronized GameEvent waitForNextEvent() {
    if (currentEvents.size() == 0) {
      hasEvents = new CompletableFuture<>();

      try {
        hasEvents.get();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return currentEvents.poll();
  }

  public void addCurrentEvent(GameEvent event) {
    currentEvents.add(event);
    hasEvents.complete(null);
  }

  public void addCurrentEvents(Collection<GameEvent> events) {
    currentEvents.addAll(events);
    hasEvents.complete(null);
  }

  public synchronized void addNextEvent(GameEvent event) {
    nextEvents.add(event);
  }

  public synchronized void addNextEvents(Collection<GameEvent> events) {
    nextEvents.addAll(events);
  }

  public synchronized void moveToNext(GameEvent event) {
    currentEvents.remove(event);
    nextEvents.add(event);
  }

  public synchronized void addUniversalEvent(GameEvent event) {
    universalEvents.add(event);
  }

  public void cycleEvents(Game game) {
    currentEvents = nextEvents;
    currentEvents.add(new ActionEvent(game));
    nextEvents = new PriorityQueue<>(EVENT_CMP);
  }

  public int numCurrentEvents() {
    return currentEvents.size();
  }

  public int numNextEvents() {
    return nextEvents.size();
  }

  public List<GameEvent> playEvent() {
    return currentEvents.poll().play();
  }

  public <T extends GameEvent> List<GameEvent> getEvents(Class<T> eventClass) {
    return currentEvents.stream().filter(eventClass::isInstance).toList();
  }
}
