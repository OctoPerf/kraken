package com.kraken.tools.configuration.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;

@Value
@Builder
public class ApplicationProperties {

  public static final ApplicationProperties DEFAULT = ApplicationProperties.builder().data(Path.of("data"))
      .hostData("hostData")
      .hostUId("1001")
      .hostGId("1001")
      .build();

  @NonNull
  Path data;
  @NonNull
  String hostData;
  @NonNull
  String hostUId;
  @NonNull
  String hostGId;

}
