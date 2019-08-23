package com.kraken.command.zt.executor;

import com.kraken.command.entity.CommandLog;
import com.kraken.command.entity.Command;
import com.kraken.command.entity.CommandLogStatus;
import com.kraken.command.zt.logs.LogsQueue;
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
