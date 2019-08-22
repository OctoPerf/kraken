package com.kraken.commons.command.logs;

import com.kraken.commons.command.entity.CommandLog;
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