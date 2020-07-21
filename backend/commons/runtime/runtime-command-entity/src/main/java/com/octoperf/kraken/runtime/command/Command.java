package com.octoperf.kraken.runtime.command;

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
  List<String> command;
  @NonNull
  Map<KrakenEnvironmentKeys, String> environment;
  @NonNull
  String path;

}