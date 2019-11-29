package com.kraken.runtime.logs;

import com.kraken.runtime.entity.Log;
import com.kraken.runtime.entity.LogStatus;
import com.kraken.runtime.entity.LogType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
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

  private static final int MAX_LOGS_SIZE = 500;
  private static final Duration MAX_LOGS_TIMEOUT_MS = Duration.ofMillis(1000);
  private static final String LINE_SEP = "\r\n";
  private static final Duration INTERVAL = Duration.ofMillis(100);

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
      final var log = Log.builder().applicationId(applicationId).id(id).type(type).text("").status(LogStatus.CANCELLING).build();
      if (!this.listeners.isEmpty()) {
        this.logs.add(log);
      }
      final var subscription = this.subscriptions.get(id);
      subscription.dispose();
      return true;
    }
    return false;
  }

  @Override
  public Disposable push(final String applicationId, final String id, final LogType type, final Flux<String> stringFlux) {
    final var subscription = stringFlux
        .windowTimeout(MAX_LOGS_SIZE, MAX_LOGS_TIMEOUT_MS)
        .flatMap(window -> window.reduce((o, o2) -> o + LINE_SEP + o2))
        .map(text -> Log.builder().applicationId(applicationId).id(id).type(type).text(text).status(LogStatus.RUNNING).build())
        .concatWith(Flux.just(Log.builder().applicationId(applicationId).id(id).type(type).text("").status(LogStatus.CLOSED).build()))
        .doOnTerminate(() -> subscriptions.remove(id))
        .subscribeOn(Schedulers.elastic())
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
