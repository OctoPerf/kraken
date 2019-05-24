package com.kraken.commons.command.logs;

import com.kraken.commons.command.entity.Command;
import com.kraken.commons.command.entity.CommandLog;
import com.kraken.commons.command.entity.CommandLogStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class ConcurrentLogsQueue implements LogsQueue {

  static final Duration INTERVAL = Duration.ofMillis(100);

  ConcurrentLinkedQueue<CommandLog> logs = new ConcurrentLinkedQueue<>();
  ConcurrentMap<String, CommandsListener> listeners = new ConcurrentHashMap<>();

  @PostConstruct
  void init() {
    Flux.interval(INTERVAL)
        .onErrorContinue((throwable, o) -> throwable.printStackTrace())
        .subscribeOn(Schedulers.newSingle("ConcurrentLogsQueue", true))
        .subscribe(this::sendLogs);
  }

  @Override
  public void addListener(final String id, final String applicationId, final FluxSink<CommandLog> listener) {
    this.listeners.put(applicationId, new CommandsListener(applicationId, listener));
  }

  @Override
  public void removeListener(final String id) {
    this.listeners.remove(id);
  }

  @Override
  public CommandLog publish(final Command command, final CommandLogStatus status, final String text) {
    final var log = CommandLog.builder()
        .command(command)
        .status(status)
        .text(text)
        .build();
    if (listeners.size() > 0) {
      logs.add(log);
    }
    return log;
  }

  @Override
  public void clear() {
    logs.clear();
    listeners.clear();
  }

  private void sendLogs(Long timestamp) {
    CommandLog current;
    while ((current = logs.poll()) != null) {
      for (CommandsListener listener : this.listeners.values()) {
        if (listener.getApplicationId().equals(current.getCommand().getApplicationId())) {
          listener.getSink().next(current);
        }
      }
    }
  }

}

