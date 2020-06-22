package com.octoperf.kraken.config.influxdb.spring;

import com.octoperf.kraken.config.influxdb.api.InfluxDBProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.nullToEmpty;
import static java.util.Optional.ofNullable;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties("kraken.influxdb")
class SpringInfluxDBProperties implements InfluxDBProperties {
  String url;
  String publishedUrl;
  String user;
  String password;
  String database;

  public SpringInfluxDBProperties(@NonNull final String url,
                                  final String publishedUrl,
                                  @NonNull final String user,
                                  @NonNull final String password,
                                  String database) {
    this.url = url;
    this.publishedUrl = initPublishedUrl(url, publishedUrl);
    this.user = user;
    this.password = password;
    this.database = nullToEmpty(database);
  }

}
