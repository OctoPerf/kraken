package com.kraken.analysis.properties.spring;

import com.kraken.analysis.properties.api.InfluxDBProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder
@ConstructorBinding
@ConfigurationProperties("kraken.influxdb")
final class SpringInfluxDBProperties implements InfluxDBProperties {
  @NonNull String url;
  @NonNull String user;
  @NonNull String password;
  @NonNull String database;
}
