package com.kraken.storage.client.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class StorageClientProperties {

  @NonNull
  String storageUrl;

}
