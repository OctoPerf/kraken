package com.kraken.gatling.runner;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Value
@Builder
class GatlingProperties {

  @NonNull Path gatlingHome;
  @NonNull Path localUserFiles;
  @NonNull Path localConf;
  @NonNull Path localResults;
  @NonNull Optional<String> remoteUserFiles;
  @NonNull Optional<String> remoteConf;
  @NonNull Optional<String> remoteResult;

}

