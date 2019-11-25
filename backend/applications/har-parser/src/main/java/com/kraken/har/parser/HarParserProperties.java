package com.kraken.har.parser;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;

@Value
@Builder
public class HarParserProperties {

  @NonNull Path localHarPath;
  @NonNull String remoteHarPath;

}

