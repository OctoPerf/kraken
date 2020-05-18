package com.kraken.config.gatling.spring;

import com.kraken.config.api.LocalRemoteProperties;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.google.common.base.Strings.nullToEmpty;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
final class GatlingLocalRemoteProps implements LocalRemoteProperties {
  static final GatlingLocalRemoteProps DEFAULT_LOCAL_REMOTE = builder().build();

  String local;
  String remote;

  GatlingLocalRemoteProps(final String local, final String remote) {
    this.local = nullToEmpty(local);
    this.remote = nullToEmpty(remote);
  }
}