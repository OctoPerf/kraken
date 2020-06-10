package com.octoperf.kraken.config.influxdb.spring;

import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.google.common.base.Strings.nullToEmpty;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties("kraken.influxdb")
final class SpringInfluxDBProperties implements InfluxDBProperties {
  String url;
  String user;
  String password;
  String database;

  public SpringInfluxDBProperties(@NonNull final String url,
                                  @NonNull final String user,
                                  @NonNull final String password,
                                  String database) {
    this.url = url;
    this.user = user;
    this.password = password;
    this.database = nullToEmpty(database);
  }

}
