package com.kraken.runtime.command;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class Command {

  @NonNull
  List<String> command;
  @NonNull
  Map<String, String> environment;
  @NonNull
  String path;

}