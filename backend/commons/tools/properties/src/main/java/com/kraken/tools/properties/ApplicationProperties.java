package com.kraken.tools.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;

@Value
@Builder
public class ApplicationProperties {

  @NonNull
  Path data;

}
