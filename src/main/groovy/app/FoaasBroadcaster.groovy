package app

import ratpack.func.Action

import java.util.concurrent.CopyOnWriteArrayList

@javax.inject.Singleton
class FoaasBroadcaster {

  private final List<Action<String>> listeners = new CopyOnWriteArrayList<>()

  public AutoCloseable register(Action<String> subscriber) {
    listeners << subscriber
    new AutoCloseable() {
      void close() {
        listeners.remove subscriber
      }
    }
  }

  public void broadcast(String msg) {
    listeners*.execute(msg)
  }

}
