package com.kraken.analysis;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;

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
