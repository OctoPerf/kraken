package com.octoperf.kraken.config.grafana.spring;

import com.octoperf.kraken.config.grafana.api.GrafanaProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties(prefix = "kraken.grafana")
class SpringGrafanaProperties implements GrafanaProperties {
  String url;
  String publishedUrl;
  String dashboard;
  String user;
  String password;

  public SpringGrafanaProperties(@NonNull final String url,
                                 final String publishedUrl,
                                 @NonNull final String dashboard,
                                 @NonNull final String user,
                                 @NonNull String password) {
    this.url = url;
    this.publishedUrl = initPublishedUrl(url, publishedUrl);
    this.dashboard = dashboard;
    this.user = user;
    this.password = password;
  }
}
