package com.kraken.commons.command.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Value
@Builder
public class Command {

  @Wither
  String id;
  String applicationId;
  List<String> command;
  @Wither
  Map<String, String> environment;
  String path;
  List<String> onCancel;

  @JsonCreator
  Command(
      @JsonProperty("id") final String id,
      @JsonProperty("applicationId") final String applicationId,
      @JsonProperty("command") final List<String> command,
      @JsonProperty("environment") final Map<String, String> environment,
      @JsonProperty("path") final String path,
      @JsonProperty("onCancel") final List<String> onCancel
  ) {
    super();
    this.id = requireNonNull(id);
    this.applicationId = requireNonNull(applicationId);
    this.command = requireNonNull(command);
    this.environment = requireNonNull(environment);
    this.path = requireNonNull(path);
    this.onCancel = requireNonNull(onCancel);
  }

}