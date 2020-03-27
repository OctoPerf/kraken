package com.kraken.gatling.properties.spring;

import com.kraken.tools.properties.api.LocalRemoteProperties;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.google.common.base.Strings.nullToEmpty;

@Value
@Builder
@ConstructorBinding
final class GatlingLocalRemoteProp implements LocalRemoteProperties {
  static final LocalRemoteProperties DEFAULT_LOCAL_REMOTE = builder().build();

  String local;
  String remote;

  GatlingLocalRemoteProp(final String local, final String remote) {
    this.local = nullToEmpty(local);
    this.remote = nullToEmpty(remote);
  }
}