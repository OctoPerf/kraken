package com.kraken.commons.command.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class CommandLog {

  Command command;
  CommandLogStatus status;
  String text;

  @JsonCreator
  CommandLog(
      @JsonProperty("command") final Command command,
      @JsonProperty("status") final CommandLogStatus status,
      @JsonProperty("text") final String text) {
    super();
    this.command = requireNonNull(command);
    this.status = requireNonNull(status);
    this.text = requireNonNull(text);
  }
}
