package com.octoperf.kraken.config.backend.client.spring;

import com.octoperf.kraken.config.backend.client.api.BackendClientProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.nullToEmpty;
import static java.util.Optional.ofNullable;

@Slf4j
@Value
@Builder(toBuilder = true)
@ConstructorBinding
@ConfigurationProperties("kraken.backend")
class SpringBackendClientProperties implements BackendClientProperties {
  String url;
  String publishedUrl;
  String hostname;
  String ip;

  public SpringBackendClientProperties(@NonNull final String url,
                                       final String publishedUrl,
                                       final String hostname,
                                       final String ip) {
    this.url = url;
    this.publishedUrl = initPublishedUrl(url, publishedUrl);
    this.hostname = nullToEmpty(hostname);
    this.ip = Optional.ofNullable(ip)
        .or(() -> Optional.ofNullable(hostname).map(hn -> {
          try {
            return InetAddress.getByName(hn).getHostAddress();
          } catch (UnknownHostException e) {
            log.error("Failed to resolve hostname", e);
          }
          return "";
        }))
        .orElse("");
  }
}
