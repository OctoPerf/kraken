package com.kraken.config.storage.client.spring;

import com.kraken.config.storage.client.api.StorageClientProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties("kraken.storage")
final class SpringStorageClientProperties implements StorageClientProperties {
  @NonNull
  String url;
}
