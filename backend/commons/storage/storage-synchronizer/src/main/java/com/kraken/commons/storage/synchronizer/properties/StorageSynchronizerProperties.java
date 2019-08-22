package com.kraken.commons.storage.synchronizer.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class StorageSynchronizerProperties {

  @NonNull
  String updateFilter;

  @NonNull
  String downloadFolder;
}
