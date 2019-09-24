package com.kraken.telegraf;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;
import java.util.Optional;

@Value
@Builder
class TelegrafProperties {

  @NonNull Path localConf;
  @NonNull String remoteConf;

}

