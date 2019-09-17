package com.kraken.analysis.client.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class AnalysisClientProperties {

  @NonNull
  String analysisUrl;

}
