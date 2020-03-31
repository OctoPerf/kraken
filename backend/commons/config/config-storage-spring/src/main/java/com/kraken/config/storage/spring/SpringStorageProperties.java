package com.kraken.config.storage.spring;

import com.kraken.config.storage.api.StorageProperties;
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
