package com.kraken.runtime.logs;

import com.kraken.runtime.entity.Log;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
final class SpringLogsService implements LogsService {

  static final Duration INTERVAL = Duration.ofMillis(100);

  ConcurrentLinkedQueue<Log> logs = new ConcurrentLinkedQueue<>();
  ConcurrentMap<String, FluxSink<Log>> listeners = new ConcurrentHashMap<>();
  ConcurrentMap<String, Disposable> subscriptions = new ConcurrentHashMap<>();

  @PostConstruct
  void init() {
    Flux.interval(INTERVAL)
        .onErrorContinue((throwable, o) -> throwable.printStackTrace())
        .subscribeOn(Schedulers.newSingle("LogsService", true))
        .subscribe(this::sendLogs);
  }

  private void sendLogs(Long timestamp) {
    Log current;
    while ((current = logs.poll()) != null) {
      for (Map.Entry<String, FluxSink<Log>> listener : this.listeners.entrySet()) {
        if (listener.getKey().equals(current.getApplicationId())) {
          listener.getValue().next(current);
        }
      }
    }
  }

  @Override
  public Flux<Log> listen(final String applicationId) {
    return Flux.<Log>create(fluxSink -> this.listeners.put(applicationId, fluxSink))
        .doOnTerminate(() -> this.listeners.remove(applicationId));
  }

  @Override
  public boolean cancel(String id) {
    if (this.subscriptions.containsKey(id)) {
      final var subscription = this.subscriptions.get(id);
      subscription.dispose();
      return true;
    }
    return false;
  }

  @Override
  public Disposable push(final String applicationId, final String id, final Flux<String> stringFlux) {
    final var subscription = stringFlux.map(text -> Log.builder().applicationId(applicationId).id(id).text(text).build())
        .doOnTerminate(() -> subscriptions.remove(id))
        .subscribe(log -> {
          if (!this.listeners.isEmpty()) {
            this.logs.add(log);
          }
        });
    this.subscriptions.put(id, subscription);
    return subscription;
  }

  @Override
  public void clear() {
    logs.clear();
    listeners.clear();
    subscriptions.values().forEach(Disposable::dispose);
    subscriptions.clear();
  }
}
