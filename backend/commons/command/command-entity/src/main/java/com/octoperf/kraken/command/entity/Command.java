package com.octoperf.kraken.command.entity;

import com.octoperf.kraken.tools.environment.KrakenEnvironmentKeys;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder(toBuilder = true)
public class Command {

  @NonNull
  List<String> args;
  @NonNull
  Map<KrakenEnvironmentKeys, String> environment;
  @NonNull
  String path;

}