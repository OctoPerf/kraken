package com.kraken.commons.command.executor;

import com.kraken.commons.command.entity.CommandLog;
import com.kraken.commons.command.entity.Command;
import com.kraken.commons.command.entity.CommandLogStatus;
import com.kraken.commons.command.logs.LogsQueue;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.atomic.AtomicInteger;

public class ErrorLogsQueue implements LogsQueue {
  final AtomicInteger removedCount = new AtomicInteger(0);

  public void addListener(final String id, final String applicationId, final FluxSink<CommandLog> listener) {
    listener.error(new RuntimeException("FAIL"));
  }

  public void removeListener(final String id) {
    removedCount.incrementAndGet();
  }

  public CommandLog publish(Command command, CommandLogStatus status, String text) {
    return null;
  }

  public void clear() {}
}
