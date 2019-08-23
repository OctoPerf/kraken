package com.kraken.command.zt.logs;

import com.kraken.command.entity.CommandLog;
import lombok.NonNull;
import lombok.Value;
import reactor.core.publisher.FluxSink;

@Value
class CommandsListener {

  @NonNull
  String applicationId;
  @NonNull
  FluxSink<CommandLog> sink;

}