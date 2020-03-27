package com.kraken.storage.client.properties;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.storage")
final class SpringStorageProperties implements StorageProperties {
  @NonNull
  String url;
}
