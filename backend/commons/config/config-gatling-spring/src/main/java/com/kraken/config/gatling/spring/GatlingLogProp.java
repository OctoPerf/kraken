package com.kraken.config.gatling.spring;

import com.kraken.config.gatling.api.GatlingLog;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.context.properties.ConstructorBinding;

import static com.google.common.base.Strings.nullToEmpty;

@Value
@Builder(toBuilder = true)
@ConstructorBinding
final class GatlingLogProp implements GatlingLog {
  static final GatlingLogProp DEFAULT_LOG = builder().build();

  String info;
  String debug;

  GatlingLogProp(final String info, final String debug) {
    this.info = nullToEmpty(info);
    this.debug = nullToEmpty(debug);
  }
}