package com.octoperf.kraken.tools.log;

import com.octoperf.kraken.security.entity.owner.Owned;
import com.octoperf.kraken.security.entity.owner.Owner;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public abstract class AbstractLogService<L extends Owned> {
  private static final Duration INTERVAL = Duration.ofMillis(100);
  private static final int MAX_LOGS_SIZE = 500;
  private static final Duration MAX_LOGS_TIMEOUT = Duration.ofMillis(1000);
  private static final String LINE_SEP = "\r\n";

  ConcurrentLinkedQueue<L> logs = new ConcurrentLinkedQueue<>();
  ConcurrentMap<Owner, FluxSink<L>> listeners = new ConcurrentHashMap<>();

  @PostConstruct
  public void init() {
    Flux.interval(INTERVAL)
        .onErrorContinue((throwable, o) -> log.error("Failed to send logs " + o, throwable))
        .subscribeOn(Schedulers.newSingle(this.getClass().getCanonicalName(), true))
        .subscribe(timestamp -> this.sendLogs());
  }

  private void sendLogs() {
    L current;
    while ((current = logs.poll()) != null) {
      for (Map.Entry<Owner, FluxSink<L>> listener : this.listeners.entrySet()) {
        if (listener.getKey().equals(current.getOwner())) {
          listener.getValue().next(current);
        }
      }
    }
  }

  public Flux<L> listen(final Owner owner) {
    return Flux.<L>create(fluxSink -> this.listeners.put(owner, fluxSink))
        .doOnTerminate(() -> this.listeners.remove(owner));
  }

  public void add(final L log) {
    if (!this.listeners.isEmpty()) {
      this.logs.add(log);
    }
  }

  public void clear() {
    logs.clear();
    listeners.clear();
  }

  protected Flux<String> concat(final Flux<String> flux) {
    return flux.windowTimeout(MAX_LOGS_SIZE, MAX_LOGS_TIMEOUT)
        .flatMap(window -> window.reduce((o, o2) -> o + LINE_SEP + o2));
  }
}
