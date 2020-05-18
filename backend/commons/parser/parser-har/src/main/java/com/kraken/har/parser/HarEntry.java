package com.kraken.har.parser;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

@Value
@Builder(toBuilder = true)
final class HarEntry {

  @NonNull
  Long timestamp;

  @NonNull
  Integer index;

  @NonNull
  @With
  String name;
}
