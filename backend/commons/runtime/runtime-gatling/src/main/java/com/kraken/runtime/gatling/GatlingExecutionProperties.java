package com.kraken.runtime.gatling;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Value
@Builder
public class GatlingExecutionProperties {

  @NonNull Path gatlingHome;
  @NonNull Path gatlingBin;
  @NonNull Path localUserFiles;
  @NonNull Path localConf;
  @NonNull Path localResult;
  @NonNull Path infoLog;
  @NonNull Path debugLog;
  @NonNull Optional<String> remoteUserFiles;
  @NonNull Optional<String> remoteConf;
  @NonNull Optional<String> remoteResult;

  @NonNull Path localHarPath;
  @NonNull String remoteHarPath;
  @NonNull String simulationPackage;
  @NonNull String simulationClass;
  @NonNull String simulation;
  @NonNull String description;
}

