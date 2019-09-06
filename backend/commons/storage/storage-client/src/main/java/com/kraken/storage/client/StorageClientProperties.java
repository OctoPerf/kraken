package com.kraken.storage.client;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class StorageClientProperties {

  @NonNull
  String storageUrl;

}
