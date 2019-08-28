package com.kraken.analysis.client;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class AnalysisClientProperties {

  @NonNull
  String analysisUrl;

}
