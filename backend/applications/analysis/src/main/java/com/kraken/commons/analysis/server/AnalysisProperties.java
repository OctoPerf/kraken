package com.kraken.commons.analysis.server;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class AnalysisProperties {

  @NonNull
  RunProperties runProperties;
  @NonNull
  RunProperties debugProperties;
  @NonNull
  RunProperties recordProperties;
  @NonNull
  String analysisUrl;

}
